/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package podsistem1;

import entities.Grad;
import entities.Korisnik;
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

/**
 *
 * @author Andrea
 */
public class Podsistem1 {

    @Resource(lookup="jms/__defaultConnectionFactory")
    private static ConnectionFactory connFactory;
    
    @Resource(lookup="queue_sub1")
    private static Queue queue_sub1;
    
    @Resource(lookup="queue_sub2")
    private static Queue queue_sub2;
    
    @Resource(lookup="queue_sub3")
    private static Queue queue_sub3;
    
    @Resource(lookup="queue_server")
    private static Queue queue_server;
    
    
    public static void main(String[] args) {
        JMSContext context = connFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(queue_sub1);
            
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Podsistem1PU");
        EntityManager em = emf.createEntityManager();
        
        //variables
        TextMessage txtMsg;
        Grad grad;
        Korisnik korisnik;
        
        while(true){
            Message msg = consumer.receive();
            
            if(msg instanceof ObjectMessage){
                try {
                    ObjectMessage objMsg = (ObjectMessage)msg;
                    Request1 request = (Request1)objMsg.getObject();
                    
                    System.out.println("request_no : " + request.getRequest_no());
                    switch(request.getRequest_no()){
                        case 1: //kreiraj grad
                            grad = new Grad();
                            grad.setNaziv(request.getNaziv_grada());
                            
                            em.getTransaction().begin();
                            em.persist(grad);
                            em.getTransaction().commit();
                            
                            //update podsistem2
                            request.setRequest_no(14);
                            objMsg = context.createObjectMessage(request);
                            producer.send(queue_sub2, objMsg);  
                            
                            //update podsistem3
                            request.setRequest_no(12);
                            objMsg = context.createObjectMessage(request);
                            producer.send(queue_sub3, objMsg);
                            
                            txtMsg = context.createTextMessage("grad je kreiran.");
                            producer.send(queue_server, txtMsg);
                            break;
                        case 2: //dohvati gradove
                            ArrayList<Grad> gradovi = new ArrayList<>(em.createNamedQuery("Grad.findAll", Grad.class).getResultList());
                            objMsg = context.createObjectMessage(gradovi);
                            producer.send(queue_server, objMsg);
                            break;
                        case 3: //kreiraj korisnika
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
                            
                            //update podsistem2
                            request.setRequest_no(10);
                            objMsg = context.createObjectMessage(request);
                            producer.send(queue_sub2, objMsg);  
                            
                            //update podsistem3
                            request.setRequest_no(5);
                            objMsg = context.createObjectMessage(request);
                            producer.send(queue_sub3, objMsg);
                            
                            txtMsg = context.createTextMessage("korisnik je kreiran.");
                            producer.send(queue_server, txtMsg);
                            break;
                        case 4: //dohvati korisnike
                            ArrayList<Korisnik> korisnici = new ArrayList<>(em.createNamedQuery("Korisnik.findAll", Korisnik.class).getResultList());
                            objMsg = context.createObjectMessage(korisnici);
                            producer.send(queue_server, objMsg);
                            break;
                        case 5: //dodavanje novca
                            korisnik = em.createNamedQuery("Korisnik.findByKorisnickoIme", Korisnik.class)
                                    .setParameter("korisnickoIme", request.getKorisnicko_ime()).getSingleResult();
                            
                            em.getTransaction().begin();
                            korisnik.setNovac(korisnik.getNovac().add(new BigDecimal(request.getNovac())));
                            em.getTransaction().commit();
                            
                            //update podsistem2
                            request.setRequest_no(11);
                            objMsg = context.createObjectMessage(request);
                            producer.send(queue_sub2, objMsg);
                            
                            //update podsistem3
                            request.setRequest_no(6);
                            objMsg = context.createObjectMessage(request);
                            producer.send(queue_sub3, objMsg);
                            
                            txtMsg = context.createTextMessage("novac je uspesno dodat.");
                            producer.send(queue_server, txtMsg);
                            break;
                        case 6: //menjanje adrese i grada
                            korisnik = em.createNamedQuery("Korisnik.findByKorisnickoIme", Korisnik.class)
                                    .setParameter("korisnickoIme", request.getKorisnicko_ime()).getSingleResult();
                            grad = em.createNamedQuery("Grad.findBySifG", Grad.class)
                                    .setParameter("sifG", request.getSif_grada()).getSingleResult();
                            
                            em.getTransaction().begin();
                            korisnik.setAdresa(request.getAdresa());
                            korisnik.setSifG(grad);
                            em.getTransaction().commit();
                            
                            //update podsistem2
                            request.setRequest_no(12);
                            objMsg = context.createObjectMessage(request);
                            producer.send(queue_sub2, objMsg); 
                            
                            //update podsistem3
                            request.setRequest_no(7);
                            objMsg = context.createObjectMessage(request);
                            producer.send(queue_sub3, objMsg);
                            
                            txtMsg = context.createTextMessage("adresa i grad su uspesno promenjeni.");
                            producer.send(queue_server, txtMsg);
                            break;
                        case 7:
                            korisnik = em.createNamedQuery("Korisnik.findByKorisnickoIme", Korisnik.class)
                                    .setParameter("korisnickoIme", request.getKorisnicko_ime()).getSingleResult();
                            em.getTransaction().begin();
                            korisnik.setNovac(new BigDecimal(request.getNovac()));
                            em.getTransaction().commit();
                            break;
                    }
                    
                } catch (JMSException ex) {
                    Logger.getLogger(Podsistem1.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }    
}
