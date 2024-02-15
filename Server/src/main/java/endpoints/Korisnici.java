/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package endpoints;

import entities.Korisnik;
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
import requests.Request1;

/**
 *
 * @author Andrea
 */
@Path("korisnici")
public class Korisnici {
    
    @Resource(lookup="jms/__defaultConnectionFactory")
    private ConnectionFactory connectionFactory;
    
    @Resource(lookup="queue_sub1")
    private Queue queue_sub1;
    
    @Resource(lookup="queue_server")
    private Queue queue_server;
    
    @POST
    @Path("{korisnicko_ime}/{sifra}/{ime}/{prezime}/{adresa}/{novac}/{grad}")
    @Produces(MediaType.TEXT_PLAIN)
    public String kreirajKorisnika(
            @PathParam("korisnicko_ime") String korisnicko_ime, @PathParam("sifra") String sifra,
            @PathParam("ime") String ime, @PathParam("prezime") String prezime,
            @PathParam("adresa") String adresa, @PathParam("novac") double novac, @PathParam("grad") int grad)
    {
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(queue_server);
        
        Request1 request = new Request1();
        request.setRequest_no(3);
        request.setKorisnicko_ime(korisnicko_ime);
        request.setSifra(sifra);
        request.setIme(ime);
        request.setPrezime(prezime);
        request.setAdresa(adresa);
        request.setNovac(novac);
        request.setSif_grada(grad);
        
        ObjectMessage objMsg = context.createObjectMessage(request);
        producer.send(queue_sub1, objMsg);
        
        Message msg = consumer.receive();
        consumer.close();
        context.close();
        
        if(msg instanceof TextMessage){
            try {
                TextMessage txtMsg = (TextMessage)msg;
                String response = txtMsg.getText();
                return response;
            } catch (JMSException ex) {
                Logger.getLogger(Korisnici.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return "*** GRESKA ***";
    }
    
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String dohvatiKorisnike(){
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(queue_server);

        Request1 request = new Request1();
        request.setRequest_no(4);
        
        ObjectMessage objMsg = context.createObjectMessage(request);
        producer.send(queue_sub1, objMsg);
        
        Message msg = consumer.receive();
        consumer.close();
        context.close();
        if(msg instanceof ObjectMessage){
            try {
                objMsg = (ObjectMessage)msg;
                ArrayList<Korisnik> korisnici = (ArrayList<Korisnik>)objMsg.getObject();
                String response = "";
                
                for (Korisnik korisnik : korisnici) {
                    response += korisnik.toString();
                    response += "\n";
                }
                
                return response;
            } catch (JMSException ex) {
                Logger.getLogger(Korisnici.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return "*** GRESKA ***";
    }
    
    @POST
    @Path("{korisnicko_ime}/{novac}")
    @Produces(MediaType.TEXT_PLAIN)
    public String dodajNovac(@PathParam("korisnicko_ime") String korisnicko_ime, @PathParam("novac") double novac){
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(queue_server);
        
        Request1 request = new Request1();
        request.setRequest_no(5);
        request.setKorisnicko_ime(korisnicko_ime);
        request.setNovac(novac);
        
        ObjectMessage objMsg = context.createObjectMessage(request);
        producer.send(queue_sub1, objMsg);
        
        Message msg = consumer.receive();
        consumer.close();
        context.close();
        
        if(msg instanceof TextMessage){
            try {
                TextMessage txtMsg = (TextMessage)msg;
                String response = txtMsg.getText();
                return response;
            } catch (JMSException ex) {
                Logger.getLogger(Korisnici.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return "*** GRESKA ***";
    }
    
    @POST
    @Path("{korisnicko_ime}/{adresa}/{grad}")
    @Produces(MediaType.TEXT_PLAIN)
    public String promeniAdresuIGrad(@PathParam("korisnicko_ime") String korisnicko_ime, @PathParam("adresa") String adresa, @PathParam("grad") int grad){
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(queue_server);
        
        Request1 request = new Request1();
        request.setRequest_no(6);
        request.setKorisnicko_ime(korisnicko_ime);
        request.setAdresa(adresa);
        request.setSif_grada(grad);
        
        ObjectMessage objMsg = context.createObjectMessage(request);
        producer.send(queue_sub1, objMsg);
        
        Message msg = consumer.receive();
        consumer.close();
        context.close();
        
        if(msg instanceof TextMessage){
            try {
                TextMessage txtMsg = (TextMessage)msg;
                String response = txtMsg.getText();
                return response;
            } catch (JMSException ex) {
                Logger.getLogger(Korisnici.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return "*** GRESKA ***";
    }
}
