/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package podsistem3;

import entities.Grad;
import entities.Korisnik;
import entities.Korpa;
import entities.KorpaArtikal;
import entities.Narudzbina;
import entities.Stavka;
import entities.Transakcija;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import requests.Request1;
import requests.Request2;
import requests.Request3;

/**
 *
 * @author Andrea
 */
public class Podsistem3 {

    @Resource(lookup="jms/__defaultConnectionFactory")
    private static ConnectionFactory connFactory;
    
    @Resource(lookup="queue_sub3")
    private static Queue queue_sub3;
    
    @Resource(lookup="queue_sub1")
    private static Queue queue_sub1;
    
    @Resource(lookup="queue_sub2")
    private static Queue queue_sub2;
    
    @Resource(lookup="queue_server")
    private static Queue queue_server;
    
    public static void main(String[] args) {
        JMSContext context = connFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(queue_sub3);
            
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Podsistem3PU");
        EntityManager em = emf.createEntityManager();
        
        //variables
        TextMessage txtMsg;
        Korisnik korisnik;
        Grad grad;
        Korpa korpa;
        KorpaArtikal korpa_artikal;
        ArrayList<KorpaArtikal> korpa_artikli;
        Narudzbina narudzbina;
        Transakcija transakcija;
        Stavka stavka;
        ArrayList<Narudzbina> narudzbine;
        ArrayList<Transakcija> transakcije;
        
        while(true){
            Message msg = consumer.receive();
            
            if(msg instanceof ObjectMessage){
                try {
                    ObjectMessage objMsg = (ObjectMessage)msg;
                    
                    if(objMsg.getObject() instanceof Request3){
                        Request3 request = (Request3)objMsg.getObject();
                        
                        System.out.println("request_no : " + request.getRequest_no());
                        switch(request.getRequest_no()){
                            case 1: //placanje
                                //kreiraj narudzbinu
                                korisnik = em.createNamedQuery("Korisnik.findByKorisnickoIme", Korisnik.class)
                                        .setParameter("korisnickoIme", request.getKorisnicko_ime()).getSingleResult();
                                korpa = em.createNamedQuery("Korpa.findBySifK", Korpa.class)
                                        .setParameter("sifK", korisnik.getSifK()).getSingleResult();
                                
                                korpa_artikli = new ArrayList<>(em.createQuery("select ka from KorpaArtikal ka where ka.sifK=:sifK", KorpaArtikal.class)
                                        .setParameter("sifK", korpa).getResultList());
                                
                                if(korisnik.getNovac().doubleValue() < korpa.getCena().doubleValue()){
                                    txtMsg = context.createTextMessage("korisnik nema dovoljno novca na racunu.");
                                    producer.send(queue_server, txtMsg);
                                } else {
                                
                                narudzbina = new Narudzbina();
                                narudzbina.setCena(korpa.getCena());
                                narudzbina.setVremeKreiranja(new Date());
                                narudzbina.setAdresa(korisnik.getAdresa());
                                narudzbina.setSifG(korisnik.getSifG());
                                narudzbina.setSifK(korisnik);
                                
                                transakcija = new Transakcija();
                                transakcija.setSuma(korpa.getCena());
                                transakcija.setVremePlacanja(new Date());
                                transakcija.setSifN(narudzbina);
                                
                                em.getTransaction().begin();
                                
                                em.persist(narudzbina);
                                em.persist(transakcija);
                                for (KorpaArtikal korpaArtikal : korpa_artikli) {
                                    stavka = new Stavka();
                                    stavka.setCena(korpaArtikal.getCena());
                                    stavka.setSifA(korpaArtikal.getSifA());
                                    stavka.setKolicina(korpaArtikal.getKolicina());
                                    stavka.setSifN(narudzbina);
                                    
                                    em.persist(stavka);
                                }
                                //update korisnik
                                korisnik.setNovac(korisnik.getNovac().subtract(korpa.getCena()));
                                
                                //update korpa_artikal & korpa
                                korpa.setCena(new BigDecimal(0));
                                em.createQuery("delete from KorpaArtikal where sifK=:sifK").setParameter("sifK", korpa).executeUpdate();

                                em.getTransaction().commit();
                                
                                //update podsistem1
                                Request1 req = new Request1();
                                req.setRequest_no(7);
                                req.setKorisnicko_ime(request.getKorisnicko_ime());
                                req.setNovac(korisnik.getNovac().doubleValue());
                                producer.send(queue_sub1, req);
                                
                                //update podsistem2
                                request.setRequest_no(13);
                                producer.send(queue_sub2, request);
                                        
                                txtMsg = context.createTextMessage("uspesno placanje.");
                                producer.send(queue_server, txtMsg);
                                }
                                break;
                            case 2: //dohvati moje narudzbine
                                String korisnicko_ime = (String) emf.getProperties().get("javax.persistence.jdbc.user");
                                korisnik = em.createNamedQuery("Korisnik.findByKorisnickoIme", Korisnik.class)
                                    .setParameter("korisnickoIme", korisnicko_ime).getSingleResult();
                                narudzbine = new ArrayList<>(em.createQuery("select n from Narudzbina n where n.sifK=:sifK", Narudzbina.class)
                                        .setParameter("sifK", korisnik).getResultList());
                                objMsg = context.createObjectMessage(narudzbine);
                                producer.send(queue_server, objMsg);
                                break;
                            case 3: //dovtati narudzbine
                                narudzbine = new ArrayList<>(em.createNamedQuery("Narudzbina.findAll", Narudzbina.class).getResultList());
                                objMsg = context.createObjectMessage(narudzbine);
                                producer.send(queue_server, objMsg);
                                break;
                            case 4: //dohvati transakcije
                                transakcije = new ArrayList<>(em.createNamedQuery("Transakcija.findAll", Transakcija.class).getResultList());
                                objMsg = context.createObjectMessage(transakcije);
                                producer.send(queue_server, objMsg);
                                break;
                        }
                    } else if(objMsg.getObject() instanceof Request1){
                        Request1 request = (Request1)objMsg.getObject();
                        
                        System.out.println("request_no : " + request.getRequest_no());
                        switch(request.getRequest_no()){
                            case 5:
                                case 10:
                                korisnik = new Korisnik();
                                korisnik.setKorisnickoIme(request.getKorisnicko_ime());
                                korisnik.setSifra(request.getSifra());
                                korisnik.setIme(request.getIme());
                                korisnik.setPrezime(request.getPrezime());
                                korisnik.setAdresa(request.getAdresa());
                                korisnik.setNovac(new BigDecimal(request.getNovac()));
                                grad = em.createNamedQuery("Grad.findBySifG", Grad.class).setParameter("sifG", request.getSif_grada()).getSingleResult();
                                korisnik.setSifG(grad);
                            
                                em.getTransaction().begin();
                                em.persist(korisnik);
                                em.getTransaction().commit();
                                
                                korpa = new Korpa();
                                korisnik = em.createNamedQuery("Korisnik.findByKorisnickoIme", Korisnik.class)
                                    .setParameter("korisnickoIme", request.getKorisnicko_ime()).getSingleResult();
                                korpa.setSifK(korisnik.getSifK());
                                korpa.setCena(new BigDecimal(0));
                                
                                em.getTransaction().begin();
                                em.persist(korpa);
                                em.getTransaction().commit();
                                break;
                            case 6:
                                korisnik = em.createNamedQuery("Korisnik.findByKorisnickoIme", Korisnik.class)
                                    .setParameter("korisnickoIme", request.getKorisnicko_ime()).getSingleResult();
                            
                                em.getTransaction().begin();
                                korisnik.setNovac(korisnik.getNovac().add(new BigDecimal(request.getNovac())));
                                em.getTransaction().commit();
                                break;
                            case 7:
                                korisnik = em.createNamedQuery("Korisnik.findByKorisnickoIme", Korisnik.class)
                                    .setParameter("korisnickoIme", request.getKorisnicko_ime()).getSingleResult();
                                grad = em.createNamedQuery("Grad.findBySifG", Grad.class)
                                    .setParameter("sifG", request.getSif_grada()).getSingleResult();
                            
                                em.getTransaction().begin();
                                korisnik.setAdresa(request.getAdresa());
                                korisnik.setSifG(grad);
                                em.getTransaction().commit();
                                break;
                            case 12:
                                grad = new Grad();
                                grad.setNaziv(request.getNaziv_grada());
                            
                                em.getTransaction().begin();
                                em.persist(grad);
                                em.getTransaction().commit();
                                break;
                        }
                    } else if(objMsg.getObject() instanceof Request2){
                        Request2 request = (Request2)objMsg.getObject();
                        
                        System.out.println("request_no : " + request.getRequest_no());
                        switch(request.getRequest_no()){
                            case 8:
                                korisnik = em.createNamedQuery("Korisnik.findByKorisnickoIme", Korisnik.class)
                                            .setParameter("korisnickoIme", request.getKorisnicko_ime()).getSingleResult();
                                korpa = em.createNamedQuery("Korpa.findBySifK", Korpa.class)
                                            .setParameter("sifK", korisnik.getSifK()).getSingleResult();
                                
                                korpa_artikli = new ArrayList<>(em.createQuery("select ka from KorpaArtikal ka where ka.sifA=:sifA and ka.sifK=:sifK", KorpaArtikal.class)
                                        .setParameter("sifA", request.getSif_artikal()).setParameter("sifK", korpa).getResultList());
                                
                                if(korpa_artikli.size() == 0){
                                    //insert
                                    korpa_artikal = new KorpaArtikal();
                                    korpa_artikal.setSifA(request.getSif_artikal());
                                    korpa_artikal.setSifK(korpa);
                                    korpa_artikal.setKolicina(request.getKolicina());
                                    korpa_artikal.setCena(new BigDecimal( request.getJedinicna_cena()));
                                    em.getTransaction().begin();
                                    em.persist(korpa_artikal);
                                    em.getTransaction().commit();
                                } else {
                                    //update
                                    korpa_artikal = korpa_artikli.get(0);
                                    em.getTransaction().begin();
                                    korpa_artikal.setKolicina(korpa_artikal.getKolicina() + request.getKolicina());
                                    em.getTransaction().commit();
                                }
                                
                                //update korpa
                                em.getTransaction().begin();
                                korpa.setCena(new BigDecimal(request.getCena()));
                                em.getTransaction().commit();
                                break;
                            case 9: 
                                korisnik = em.createNamedQuery("Korisnik.findByKorisnickoIme", Korisnik.class)
                                            .setParameter("korisnickoIme", request.getKorisnicko_ime()).getSingleResult();
                                korpa = em.createNamedQuery("Korpa.findBySifK", Korpa.class)
                                            .setParameter("sifK", korisnik.getSifK()).getSingleResult();
                                
                                korpa_artikal = em.createQuery("select ka from KorpaArtikal ka where ka.sifA=:sifA and ka.sifK=:sifK", KorpaArtikal.class)
                                        .setParameter("sifA", request.getSif_artikal()).setParameter("sifK", korpa).getSingleResult();
                                
                                if(request.getKolicina() >= korpa_artikal.getKolicina()){
                                    //delete
                                    request.setKolicina(korpa_artikal.getKolicina());
                                    em.getTransaction().begin();
                                    em.remove(korpa_artikal); 
                                    em.getTransaction().commit();
                                } else {
                                    //update
                                    em.getTransaction().begin();
                                    korpa_artikal.setKolicina(korpa_artikal.getKolicina() - request.getKolicina());
                                    em.getTransaction().commit();
                                }
                                
                                //update korpa
                                em.getTransaction().begin();
                                korpa.setCena(new BigDecimal(request.getCena()));
                                em.getTransaction().commit();
                                break;
                            case 10:                                
                                //update korpa_artikal & korpa
                                korpa_artikli = new ArrayList<>(em.createQuery("select ka from KorpaArtikal ka where ka.sifA=:sifA", KorpaArtikal.class)
                                            .setParameter("sifA", request.getSif_artikal()).getResultList());
                                em.getTransaction().begin();
                                for (KorpaArtikal korpaArtikal : korpa_artikli) {
                                    korpaArtikal.getSifK().setCena(korpaArtikal.getSifK().getCena().subtract(korpaArtikal.getCena().multiply(new BigDecimal(korpaArtikal.getKolicina()))));
                                    korpaArtikal.setCena(new BigDecimal(request.getJedinicna_cena()));
                                    korpaArtikal.getSifK().setCena(korpaArtikal.getSifK().getCena().add(korpaArtikal.getCena().multiply(new BigDecimal(korpaArtikal.getKolicina()))));
                                }
                                em.getTransaction().commit();
                                break;
                            case 11:                                
                                //update korpa_artikal & korpa
                                korpa_artikli = new ArrayList<>(em.createQuery("select ka from KorpaArtikal ka where ka.sifA=:sifA", KorpaArtikal.class)
                                            .setParameter("sifA", request.getSif_artikal()).getResultList());
                                em.getTransaction().begin();
                                for (KorpaArtikal korpaArtikal : korpa_artikli) {
                                    korpaArtikal.getSifK().setCena(korpaArtikal.getSifK().getCena().subtract(korpaArtikal.getCena().multiply(new BigDecimal(korpaArtikal.getKolicina()))));
                                    korpaArtikal.setCena(new BigDecimal(request.getJedinicna_cena()));
                                    korpaArtikal.getSifK().setCena(korpaArtikal.getSifK().getCena().add(korpaArtikal.getCena().multiply(new BigDecimal(korpaArtikal.getKolicina()))));
                                }
                                em.getTransaction().commit();
                                break;
                        }
                    }
                } catch (JMSException ex) {
                    Logger.getLogger(Podsistem3.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        }
    }
    
}
