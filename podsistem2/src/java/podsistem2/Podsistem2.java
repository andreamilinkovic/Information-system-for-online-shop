/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package podsistem2;

import entities.Artikal;
import entities.Grad;
import entities.Kategorija;
import entities.Korisnik;
import entities.Korpa;
import entities.KorpaArtikal;
import java.math.BigDecimal;
import java.util.ArrayList;
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
public class Podsistem2 {

    @Resource(lookup="jms/__defaultConnectionFactory")
    private static ConnectionFactory connFactory;
    
    @Resource(lookup="queue_sub2")
    private static Queue queue_sub2;
    
    @Resource(lookup="queue_sub3")
    private static Queue queue_sub3;
    
    @Resource(lookup="queue_server")
    private static Queue queue_server;
    
    public static void main(String[] args) {
        JMSContext context = connFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(queue_sub2);
            
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Podsistem2PU");
        EntityManager em = emf.createEntityManager();
        
        //variables
        TextMessage txtMsg;
        Kategorija kategorija;
        Artikal artikal;
        Korisnik korisnik;
        Grad grad;
        Korpa korpa;
        KorpaArtikal korpa_artikal;
        ArrayList<KorpaArtikal>korpa_artikli;
        ArrayList<Artikal> artikli;
        String korisnicko_ime;
        
        while(true){
            Message msg = consumer.receive();
            
            if(msg instanceof ObjectMessage){
                try {
                    ObjectMessage objMsg = (ObjectMessage)msg;
                    
                    if(objMsg.getObject() instanceof Request2){
                        Request2 request = (Request2)objMsg.getObject();
                        
                        System.out.println("request_no : " + request.getRequest_no());
                        switch(request.getRequest_no()){
                            case 1: //kreiranje kategorije
                                kategorija = new Kategorija();
                                kategorija.setNaziv(request.getNaziv());
                                if(request.getNatkategorija_sif() != -1){
                                    Kategorija natkategorija = em.createNamedQuery("Kategorija.findBySifK", Kategorija.class)
                                            .setParameter("sifK", request.getNatkategorija_sif()).getSingleResult();
                                    kategorija.setSifKNad(natkategorija);
                                }

                                em.getTransaction().begin();
                                em.persist(kategorija);
                                em.getTransaction().commit();

                                txtMsg = context.createTextMessage("kategorija je uspesno kreirana.");
                                producer.send(queue_server, txtMsg);
                                break;
                            case 2: //kreiranje artikla
                                artikal = new Artikal();
                                artikal.setNaziv(request.getNaziv());
                                artikal.setOpis(request.getOpis());
                                artikal.setCena(new BigDecimal(request.getCena()));
                                artikal.setPopust(request.getPopust());
                                kategorija = em.createNamedQuery("Kategorija.findBySifK", Kategorija.class)
                                        .setParameter("sifK", request.getNatkategorija_sif()).getSingleResult();
                                artikal.setSifK(kategorija);
                                korisnik = em.createNamedQuery("Korisnik.findByKorisnickoIme", Korisnik.class)
                                        .setParameter("korisnickoIme", request.getKorisnicko_ime()).getSingleResult();
                                artikal.setSifP(korisnik);

                                em.getTransaction().begin();
                                em.persist(artikal);
                                em.getTransaction().commit();

                                txtMsg = context.createTextMessage("artikal je uspesno kreiran.");
                                producer.send(queue_server, txtMsg);
                                break;
                            case 3: //promena cene artikla
                                artikal = em.createNamedQuery("Artikal.findBySifA", Artikal.class)
                                        .setParameter("sifA", request.getSif_artikal()).getSingleResult();

                                em.getTransaction().begin();
                                artikal.setCena(new BigDecimal(request.getCena()));
                                em.getTransaction().commit();
                                
                                //update korpa_artikal & korpa
                                korpa_artikli = new ArrayList<>(em.createQuery("select ka from KorpaArtikal ka where ka.sifA=:sifA", KorpaArtikal.class)
                                            .setParameter("sifA", artikal).getResultList());
                                em.getTransaction().begin();
                                for (KorpaArtikal korpaArtikal : korpa_artikli) {
                                    korpaArtikal.getSifK().setCena(korpaArtikal.getSifK().getCena().subtract(korpaArtikal.getCena().multiply(new BigDecimal(korpaArtikal.getKolicina()))));
                                    korpaArtikal.setCena(new BigDecimal((100 - artikal.getPopust())*0.01).multiply(artikal.getCena()));
                                    korpaArtikal.getSifK().setCena(korpaArtikal.getSifK().getCena().add(korpaArtikal.getCena().multiply(new BigDecimal(korpaArtikal.getKolicina()))));
                                }
                                em.getTransaction().commit();
                                    
                                //update podsistem3
                                request.setRequest_no(10);
                                request.setJedinicna_cena((new BigDecimal((100 - artikal.getPopust())*0.01).multiply(artikal.getCena())).doubleValue());
                                producer.send(queue_sub3, request);
                                    
                                txtMsg = context.createTextMessage("cena artikla je uspesno promenjena.");
                                producer.send(queue_server, txtMsg);
                                break;
                            case 4: //postavljanje popusta
                                artikal = em.createNamedQuery("Artikal.findBySifA", Artikal.class)
                                        .setParameter("sifA", request.getSif_artikal()).getSingleResult();

                                em.getTransaction().begin();
                                artikal.setPopust(request.getPopust());
                                em.getTransaction().commit();
                                
                                //update korpa_artikal & korpa
                                korpa_artikli = new ArrayList<>(em.createQuery("select ka from KorpaArtikal ka where ka.sifA=:sifA", KorpaArtikal.class)
                                            .setParameter("sifA", artikal).getResultList());
                                em.getTransaction().begin();
                                for (KorpaArtikal korpaArtikal : korpa_artikli) {
                                    korpaArtikal.getSifK().setCena(korpaArtikal.getSifK().getCena().subtract(korpaArtikal.getCena().multiply(new BigDecimal(korpaArtikal.getKolicina()))));
                                    korpaArtikal.setCena(new BigDecimal((100 - artikal.getPopust())*0.01).multiply(artikal.getCena()));
                                    korpaArtikal.getSifK().setCena(korpaArtikal.getSifK().getCena().add(korpaArtikal.getCena().multiply(new BigDecimal(korpaArtikal.getKolicina()))));
                                }
                                em.getTransaction().commit();
                                
                                //update podsistem3
                                request.setRequest_no(11);
                                request.setJedinicna_cena((new BigDecimal((100 - artikal.getPopust())*0.01).multiply(artikal.getCena())).doubleValue());
                                producer.send(queue_sub3, request);

                                txtMsg = context.createTextMessage("popust na artikal je uspesno postavljen.");
                                producer.send(queue_server, txtMsg);
                                break;
                            case 5: //dodavanje artikla u korpu
                                artikal = em.createNamedQuery("Artikal.findBySifA", Artikal.class)
                                        .setParameter("sifA", request.getSif_artikal()).getSingleResult(); 
                                korisnik = em.createNamedQuery("Korisnik.findByKorisnickoIme", Korisnik.class)
                                            .setParameter("korisnickoIme", request.getKorisnicko_ime()).getSingleResult();
                                korpa = em.createNamedQuery("Korpa.findBySifK", Korpa.class)
                                            .setParameter("sifK", korisnik.getSifK()).getSingleResult();
                                
                                korpa_artikli = new ArrayList<>(em.createQuery("select ka from KorpaArtikal ka where ka.sifA=:sifA and ka.sifK=:sifK", KorpaArtikal.class)
                                        .setParameter("sifA", artikal).setParameter("sifK", korpa).getResultList());
                                
                                if(korpa_artikli.size() == 0){
                                    //insert
                                    korpa_artikal = new KorpaArtikal();
                                    korpa_artikal.setSifA(artikal);
                                    korpa_artikal.setSifK(korpa);
                                    korpa_artikal.setKolicina(request.getKolicina());
                                    korpa_artikal.setCena(artikal.getCena().multiply(new BigDecimal((100 - artikal.getPopust())*0.01)));
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
                                korpa.setCena(new BigDecimal(request.getKolicina())
                                        .multiply(artikal.getCena()).multiply(new BigDecimal((100 - artikal.getPopust())*0.01)).add(korpa.getCena()));
                                em.getTransaction().commit();
                                
                                //update podsistem3
                                request.setRequest_no(8);
                                request.setCena(korpa.getCena().doubleValue());
                                request.setJedinicna_cena(korpa_artikal.getCena().doubleValue());
                                producer.send(queue_sub3, request);

                                txtMsg = context.createTextMessage("artikal je uspesno dodat u korpu.");
                                producer.send(queue_server, txtMsg);
                                break;
                            case 6: //izbacivanje artikla iz korpe
                                artikal = em.createNamedQuery("Artikal.findBySifA", Artikal.class)
                                        .setParameter("sifA", request.getSif_artikal()).getSingleResult(); 
                                korisnik = em.createNamedQuery("Korisnik.findByKorisnickoIme", Korisnik.class)
                                            .setParameter("korisnickoIme", request.getKorisnicko_ime()).getSingleResult();
                                korpa = em.createNamedQuery("Korpa.findBySifK", Korpa.class)
                                            .setParameter("sifK", korisnik.getSifK()).getSingleResult();
                                
                                korpa_artikal = em.createQuery("select ka from KorpaArtikal ka where ka.sifA=:sifA and ka.sifK=:sifK", KorpaArtikal.class)
                                        .setParameter("sifA", artikal).setParameter("sifK", korpa).getSingleResult();
                                
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
                                korpa.setCena(korpa.getCena().subtract(new BigDecimal(request.getKolicina())
                                        .multiply(artikal.getCena()).multiply(new BigDecimal((100 - artikal.getPopust())*0.01))));
                                em.getTransaction().commit();
                                
                                //update podsistem3
                                request.setRequest_no(9);
                                request.setCena(korpa.getCena().doubleValue());
                                producer.send(queue_sub3, request);

                                txtMsg = context.createTextMessage("artikal je uspesno izbacen iz korpe.");
                                producer.send(queue_server, txtMsg);
                                break;
                            case 7: //dohvati kategorije
                                ArrayList<Kategorija> kategorije = new ArrayList<>(em.createNamedQuery("Kategorija.findAll", Kategorija.class).getResultList());
                                objMsg = context.createObjectMessage(kategorije);
                                producer.send(queue_server, objMsg);
                                break;
                            case 8: //dohvati moje artikle
                                korisnicko_ime = (String) emf.getProperties().get("javax.persistence.jdbc.user");
                                korisnik = em.createNamedQuery("Korisnik.findByKorisnickoIme", Korisnik.class)
                                    .setParameter("korisnickoIme", korisnicko_ime).getSingleResult();
                                artikli = new ArrayList<>(em.createQuery("select a from Artikal a where a.sifP=:sifP", Artikal.class)
                                        .setParameter("sifP", korisnik).getResultList());
                                objMsg = context.createObjectMessage(artikli);
                                producer.send(queue_server, objMsg);
                                break;
                            case 9: //dohvati moju korpu
                                korisnicko_ime = (String) emf.getProperties().get("javax.persistence.jdbc.user");
                                korisnik = em.createNamedQuery("Korisnik.findByKorisnickoIme", Korisnik.class)
                                    .setParameter("korisnickoIme", korisnicko_ime).getSingleResult();
                                korpa = em.createNamedQuery("Korpa.findBySifK", Korpa.class)
                                            .setParameter("sifK", korisnik.getSifK()).getSingleResult();
                                artikli = new ArrayList<>(em.createQuery("select ka.sifA from KorpaArtikal ka where ka.sifK=:sifK", Artikal.class)
                                        .setParameter("sifK", korpa).getResultList());
                                objMsg = context.createObjectMessage(artikli);
                                producer.send(queue_server, objMsg);
                                break;
                        }
                    } else if(objMsg.getObject() instanceof Request1){
                        Request1 request = (Request1)objMsg.getObject();
                        
                        System.out.println("request_no : " + request.getRequest_no());
                        switch(request.getRequest_no()){
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
                            case 11:
                                korisnik = em.createNamedQuery("Korisnik.findByKorisnickoIme", Korisnik.class)
                                    .setParameter("korisnickoIme", request.getKorisnicko_ime()).getSingleResult();
                            
                                em.getTransaction().begin();
                                korisnik.setNovac(korisnik.getNovac().add(new BigDecimal(request.getNovac())));
                                em.getTransaction().commit();
                                break;
                            case 12:
                                korisnik = em.createNamedQuery("Korisnik.findByKorisnickoIme", Korisnik.class)
                                    .setParameter("korisnickoIme", request.getKorisnicko_ime()).getSingleResult();
                                grad = em.createNamedQuery("Grad.findBySifG", Grad.class)
                                    .setParameter("sifG", request.getSif_grada()).getSingleResult();
                            
                                em.getTransaction().begin();
                                korisnik.setAdresa(request.getAdresa());
                                korisnik.setSifG(grad);
                                em.getTransaction().commit();
                                break;
                            case 14:
                                grad = new Grad();
                                grad.setNaziv(request.getNaziv_grada());
                            
                                em.getTransaction().begin();
                                em.persist(grad);
                                em.getTransaction().commit();
                                break;
                        }
                    } else if(objMsg.getObject() instanceof Request3){
                        Request3 request = (Request3)objMsg.getObject();
                        
                        System.out.println("request_no : " + request.getRequest_no());
                        switch(request.getRequest_no()){
                            case 13:
                                korisnik = em.createNamedQuery("Korisnik.findByKorisnickoIme", Korisnik.class)
                                        .setParameter("korisnickoIme", request.getKorisnicko_ime()).getSingleResult();
                                korpa = em.createNamedQuery("Korpa.findBySifK", Korpa.class)
                                        .setParameter("sifK", korisnik.getSifK()).getSingleResult();
                                
                                em.getTransaction().begin();
                                
                                //update korisnik
                                korisnik.setNovac(korisnik.getNovac().subtract(korpa.getCena()));
                                
                                //update korpa_artikal & korpa
                                korpa.setCena(new BigDecimal(0));
                                em.createQuery("delete from KorpaArtikal where sifK=:sifK").setParameter("sifK", korpa).executeUpdate();

                                em.getTransaction().commit();
                                break;
                        }
                    }
                } catch (JMSException ex) {
                    Logger.getLogger(Podsistem2.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
}
