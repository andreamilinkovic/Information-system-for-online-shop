/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package endpoints;

import java.util.ArrayList;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import requests.Request1;

import entities.Grad;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.TextMessage;

/**
 *
 * @author Andrea
 */
@Path("gradovi")
public class Gradovi {
    
    @Resource(lookup="jms/__defaultConnectionFactory")
    private ConnectionFactory connectionFactory;
    
    @Resource(lookup="queue_sub1")
    private Queue queue_sub1;
    
    @Resource(lookup="queue_server")
    private Queue queue_server;
    
    
    @POST
    @Path("{naziv}")
    @Produces(MediaType.TEXT_PLAIN)
    public String kreirajGrad(@PathParam("naziv") String naziv){
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(queue_server);
        
        Request1 request = new Request1();
        request.setRequest_no(1);
        request.setNaziv_grada(naziv);
        
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
                Logger.getLogger(Gradovi.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return "*** GRESKA ***";
    }
    
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String dohvatiGradove(){
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(queue_server);
        
        Request1 request = new Request1();
        request.setRequest_no(2);
        
        ObjectMessage objMsg = context.createObjectMessage(request);
        producer.send(queue_sub1, objMsg);
        
        Message msg = consumer.receive();
        consumer.close();
        context.close();
        
        if(msg instanceof ObjectMessage){
            try {
                objMsg = (ObjectMessage)msg;
                ArrayList<Grad> gradovi = (ArrayList<Grad>)objMsg.getObject();
                String response = "";
                
                for (Grad grad : gradovi) {
                    response += grad.toString();
                    response += "\n";
                }
                
                return response;
            } catch (JMSException ex) {
                Logger.getLogger(Gradovi.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return "*** GRESKA ***";
    }
}
