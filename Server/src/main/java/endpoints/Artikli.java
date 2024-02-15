/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package endpoints;

import entities.Artikal;
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
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import requests.Request2;

/**
 *
 * @author Andrea
 */
@Path("artikli")
public class Artikli {
    
    @Resource(lookup="jms/__defaultConnectionFactory")
    private ConnectionFactory connectionFactory;
    
    @Resource(lookup="queue_sub2")
    private Queue queue_sub2;
    
    @Resource(lookup="queue_server")
    private Queue queue_server;
    
    @POST
    @Path("{naziv}/{opis}/{cena}/{kategorija}/{korisnicko_ime}")
    @Produces(MediaType.TEXT_PLAIN)
    public String kreirajArtikal(@PathParam("naziv") String naziv, @PathParam("opis") String opis,
            @PathParam("cena") double cena, @PathParam("kategorija") int kategorija, @PathParam("korisnicko_ime") String korisnicko_ime){
    
        JMSContext context = connectionFactory.createContext();
        JMSConsumer consumer = context.createConsumer(queue_server);
        JMSProducer producer = context.createProducer();
        
        Request2 request = new Request2();
        request.setRequest_no(2);
        request.setNaziv(naziv);
        request.setOpis(opis);
        request.setCena(cena);
        request.setPopust(0);
        request.setNatkategorija_sif(kategorija);
        request.setKorisnicko_ime(korisnicko_ime);
        
        ObjectMessage objMsg = context.createObjectMessage(request);
        producer.send(queue_sub2, objMsg);
        
        Message msg = consumer.receive();
        consumer.close();
        context.close();
        
        if(msg instanceof TextMessage){
            try {
                TextMessage txtMsg = (TextMessage)msg;
                String response = txtMsg.getText();
                return response;
            } catch (JMSException ex) {
                Logger.getLogger(Artikli.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return "*** GRESKA ***";
    }
    
    @POST
    @Path("cena/{sifA}/{cena}")
    @Produces(MediaType.TEXT_PLAIN)
    public String promeniCenuArtikla(@PathParam("sifA") int sifA, @PathParam("cena") double cena){
        JMSContext context = connectionFactory.createContext();
        JMSConsumer consumer = context.createConsumer(queue_server);
        JMSProducer producer = context.createProducer();
        
        Request2 request = new Request2();
        request.setRequest_no(3);
        request.setSif_artikal(sifA);
        request.setCena(cena);
        
        ObjectMessage objMsg = context.createObjectMessage(request);
        producer.send(queue_sub2, objMsg);
        
        Message msg = consumer.receive();
        consumer.close();
        context.close();
        
        if(msg instanceof TextMessage){
            try {
                TextMessage txtMsg = (TextMessage)msg;
                String response = txtMsg.getText();
                return response;
            } catch (JMSException ex) {
                Logger.getLogger(Artikli.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return "*** GRESKA ***";
    }
    
    @POST
    @Path("popust/{sifA}/{popust}")
    @Produces(MediaType.TEXT_PLAIN)
    public String postaviPopustNaArtikal(@PathParam("sifA") int sifA, @PathParam("popust") int popust){
        JMSContext context = connectionFactory.createContext();
        JMSConsumer consumer = context.createConsumer(queue_server);
        JMSProducer producer = context.createProducer();
        
        Request2 request = new Request2();
        request.setRequest_no(4);
        request.setSif_artikal(sifA);
        request.setPopust(popust);
        
        ObjectMessage objMsg = context.createObjectMessage(request);
        producer.send(queue_sub2, objMsg);
        
        Message msg = consumer.receive();
        consumer.close();
        context.close();
        
        if(msg instanceof TextMessage){
            try {
                TextMessage txtMsg = (TextMessage)msg;
                String response = txtMsg.getText();
                return response;
            } catch (JMSException ex) {
                Logger.getLogger(Artikli.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return "*** GRESKA ***";
    }
    
    @POST
    @Path("dodaj/{sifA}/{kolicina}/{korisnicko_ime}")
    @Produces(MediaType.TEXT_PLAIN)
    public String dodajArtikalUKorpu(@PathParam("sifA") int sifA, @PathParam("kolicina") int kolicina, @PathParam("korisnicko_ime") String korisnicko_ime){
        JMSContext context = connectionFactory.createContext();
        JMSConsumer consumer = context.createConsumer(queue_server);
        JMSProducer producer = context.createProducer();

        Request2 request = new Request2();
        request.setRequest_no(5);
        request.setSif_artikal(sifA);
        request.setKolicina(kolicina);
        request.setKorisnicko_ime(korisnicko_ime);
        
        ObjectMessage objMsg = context.createObjectMessage(request);
        producer.send(queue_sub2, objMsg);
        
        Message msg = consumer.receive();
        consumer.close();
        context.close();
        
        if(msg instanceof TextMessage){
            try {
                TextMessage txtMsg = (TextMessage)msg;
                String response = txtMsg.getText();
                return response;
            } catch (JMSException ex) {
                Logger.getLogger(Artikli.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return "*** GRESKA ***";
    }
    
    @POST
    @Path("izbaci/{sifA}/{kolicina}/{korisnicko_ime}")
    @Produces(MediaType.TEXT_PLAIN)
    public String izbaciArtikalIzKorpe(@PathParam("sifA") int sifA, @PathParam("kolicina") int kolicina, @PathParam("korisnicko_ime") String korisnicko_ime){
        JMSContext context = connectionFactory.createContext();
        JMSConsumer consumer = context.createConsumer(queue_server);
        JMSProducer producer = context.createProducer();
        
        Request2 request = new Request2();
        request.setRequest_no(6);
        request.setSif_artikal(sifA);
        request.setKolicina(kolicina);
        request.setKorisnicko_ime(korisnicko_ime);
        
        ObjectMessage objMsg = context.createObjectMessage(request);
        producer.send(queue_sub2, objMsg);
        
        Message msg = consumer.receive();
        consumer.close();
        context.close();
        
        if(msg instanceof TextMessage){
            try {
                TextMessage txtMsg = (TextMessage)msg;
                String response = txtMsg.getText();
                return response;
            } catch (JMSException ex) {
                Logger.getLogger(Artikli.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return "*** GRESKA ***";
    }
    
    @GET
    @Path("mojiartikli")
    @Produces(MediaType.TEXT_PLAIN)
    public String dohvatiMojeArtikle(){
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(queue_server);
        
            
        Request2 request = new Request2();
        request.setRequest_no(8);

        ObjectMessage objMsg = context.createObjectMessage(request);
        producer.send(queue_sub2, objMsg);

        Message msg = consumer.receive();
        consumer.close();
        context.close();

        if(msg instanceof ObjectMessage){
            try {
                objMsg = (ObjectMessage)msg;
                ArrayList<Artikal> artikli = (ArrayList<Artikal>)objMsg.getObject();
                String response = "";

                for (Artikal artikal : artikli) {
                    response += artikal.toString();
                    response += "\n";
                }

                return response;
            } catch (JMSException ex) {
                Logger.getLogger(Artikli.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return "*** GRESKA ***";
    }
    
    @GET
    @Path("mojakorpa")
    @Produces(MediaType.TEXT_PLAIN)
    public String dohvatiMojuKorpu(){
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(queue_server);
        
            
        Request2 request = new Request2();
        request.setRequest_no(9);

        ObjectMessage objMsg = context.createObjectMessage(request);
        producer.send(queue_sub2, objMsg);

        Message msg = consumer.receive();
        consumer.close();
        context.close();

        if(msg instanceof ObjectMessage){
            try {
                objMsg = (ObjectMessage)msg;
                ArrayList<Artikal> artikli = (ArrayList<Artikal>)objMsg.getObject();
                String response = "";

                for (Artikal artikal : artikli) {
                    response += artikal.toString();
                    response += "\n";
                }

                return response;
            } catch (JMSException ex) {
                Logger.getLogger(Artikli.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return "*** GRESKA ***";
    }
}
