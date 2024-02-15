/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package endpoints;

import entities.Kategorija;
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
import requests.Request2;

/**
 *
 * @author Andrea
 */
@Path("kategorije")
public class Kategorije {
    
    @Resource(lookup="jms/__defaultConnectionFactory")
    private ConnectionFactory connectionFactory;
    
    @Resource(lookup="queue_sub2")
    private Queue queue_sub2;
    
    @Resource(lookup="queue_server")
    private Queue queue_server;
    
    @POST
    @Path("{kategorija}/{natkategorija}")
    @Produces(MediaType.TEXT_PLAIN)
    public String kreirajKategoriju(@PathParam("kategorija") String kategorija, @PathParam("natkategorija") int natkategorija){
        JMSContext context = connectionFactory.createContext();
        JMSConsumer consumer = context.createConsumer(queue_server);
        JMSProducer producer = context.createProducer();
        
        Request2 request = new Request2();
        request.setRequest_no(1);
        request.setNaziv(kategorija);
        request.setNatkategorija_sif(natkategorija);
        
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
                Logger.getLogger(Kategorije.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return "*** GRESKA ***";
    }
    
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String dohvatiKategorije(){
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(queue_server);
        
        Request2 request = new Request2();
        request.setRequest_no(7);
        
        ObjectMessage objMsg = context.createObjectMessage(request);
        producer.send(queue_sub2, objMsg);
        
        Message msg = consumer.receive();
        consumer.close();
        context.close();
        
        if(msg instanceof ObjectMessage){
            try {
                objMsg = (ObjectMessage)msg;
                ArrayList<Kategorija> kategorije = (ArrayList<Kategorija>)objMsg.getObject();
                String response = "";
                
                for (Kategorija kategorija : kategorije) {
                    response += kategorija.toString();
                    response += "\n";
                }
                
                return response;
            } catch (JMSException ex) {
                Logger.getLogger(Kategorije.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return "*** GRESKA ***";
    }
}
