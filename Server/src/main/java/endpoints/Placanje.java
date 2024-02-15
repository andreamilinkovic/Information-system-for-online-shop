/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package endpoints;

import entities.Narudzbina;
import entities.Transakcija;
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
import requests.Request3;

/**
 *
 * @author Andrea
 */
@Path("placanje")
public class Placanje {
    @Resource(lookup="jms/__defaultConnectionFactory")
    private ConnectionFactory connectionFactory;
    
    @Resource(lookup="queue_sub3")
    private Queue queue_sub3;
    
    @Resource(lookup="queue_server")
    private Queue queue_server;
    
    @POST
    @Path("{korisnicko_ime}")
    @Produces(MediaType.TEXT_PLAIN)
    public String plati(@PathParam("korisnicko_ime") String korisnicko_ime){
        JMSContext context = connectionFactory.createContext();
        JMSConsumer consumer = context.createConsumer(queue_server);
        JMSProducer producer = context.createProducer();
        
        Request3 request = new Request3();
        request.setRequest_no(1);
        request.setKorisnicko_ime(korisnicko_ime);
        
        ObjectMessage objMsg = context.createObjectMessage(request);
        producer.send(queue_sub3, objMsg);
        
        Message msg = consumer.receive();
        consumer.close();
        context.close();
        
        if(msg instanceof TextMessage){
            try {
                TextMessage txtMsg = (TextMessage)msg;
                String response = txtMsg.getText();
                return response;
            } catch (JMSException ex) {
                Logger.getLogger(Placanje.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    
        return "*** GRESKA ***";
    }
    
    @GET
    @Path("mojenarudzbine")
    @Produces(MediaType.TEXT_PLAIN)
    public String dohvatiMojeNarudzbine(){
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(queue_server);
        
            
        Request3 request = new Request3();
        request.setRequest_no(2);

        ObjectMessage objMsg = context.createObjectMessage(request);
        producer.send(queue_sub3, objMsg);

        Message msg = consumer.receive();
        consumer.close();
        context.close();

        if(msg instanceof ObjectMessage){
            try {
                objMsg = (ObjectMessage)msg;
                ArrayList<Narudzbina> narudzbine = (ArrayList<Narudzbina>)objMsg.getObject();
                String response = "";

                for (Narudzbina narudzbina : narudzbine) {
                    response += narudzbina.toString();
                    response += "\n";
                }

                return response;
            } catch (JMSException ex) {
                Logger.getLogger(Placanje.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return "*** GRESKA ***";
    }
    
    @GET
    @Path("narudzbine")
    @Produces(MediaType.TEXT_PLAIN)
    public String dohvatiNarudzbine(){
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(queue_server);
        
        Request3 request = new Request3();
        request.setRequest_no(3);
        
        ObjectMessage objMsg = context.createObjectMessage(request);
        producer.send(queue_sub3, objMsg);
        
        Message msg = consumer.receive();
        consumer.close();
        context.close();
        
        if(msg instanceof ObjectMessage){
            try {
                objMsg = (ObjectMessage)msg;
                ArrayList<Narudzbina> narudzbine = (ArrayList<Narudzbina>)objMsg.getObject();
                String response = "";
                
                for (Narudzbina narudzbina : narudzbine) {
                    response += narudzbina.toString();
                    response += "\n";
                }

                return response;
            } catch (JMSException ex) {
                Logger.getLogger(Placanje.class.getName()).log(Level.SEVERE, null, ex);
                                return "*** !WTF! ***";
            }
        }
        
        return "*** GRESKA ***";
    }
    
    @GET
    @Path("transakcije")
    @Produces(MediaType.TEXT_PLAIN)
    public String dohvatiTransakcije(){
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(queue_server);
        
        Request3 request = new Request3();
        request.setRequest_no(4);
        
        ObjectMessage objMsg = context.createObjectMessage(request);
        producer.send(queue_sub3, objMsg);
        
        Message msg = consumer.receive();
        consumer.close();
        context.close();
        
        if(msg instanceof ObjectMessage){
            try {
                objMsg = (ObjectMessage)msg;
                ArrayList<Transakcija> transakcije = (ArrayList<Transakcija>)objMsg.getObject();
                String response = "";
                
                for (Transakcija transakcija : transakcije) {
                    response += transakcija.toString();
                    response += "\n";
                }
                
                return response;
            } catch (JMSException ex) {
                Logger.getLogger(Placanje.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return "*** GRESKA ***";
    }
}
