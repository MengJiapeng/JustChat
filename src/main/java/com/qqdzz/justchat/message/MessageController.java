package com.qqdzz.justchat.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.jms.*;
import java.util.Map;

/**
 * @author BadCode
 * @date 2018-05-22 21:35
 **/
@Controller
public class MessageController {

    private JmsTemplate jmsTemplate;
    private Connection jmsConnection;
    private SimpMessagingTemplate messagingTemplate;
    private Map<String, Session> jmsSessions;

    @Autowired
    public MessageController(JmsTemplate jmsTemplate, Connection jmsConnection,
                             SimpMessagingTemplate messagingTemplate, Map<String, Session> jmsSessions) {
        this.jmsTemplate = jmsTemplate;
        this.jmsConnection = jmsConnection;
        this.messagingTemplate = messagingTemplate;
        this.jmsSessions = jmsSessions;
    }

    @MessageMapping("/chat")
    public void onReceiveMessage(Message message) {
        jmsTemplate.convertAndSend(message.getReceiverId(), message);
    }

    @GetMapping("/create-consumer")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void createConsumer(String username) throws JMSException {
        Session session = jmsConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        jmsSessions.put(username, session);
        System.out.println(jmsSessions.size());
        Destination destination = session.createQueue(username);
        MessageConsumer messageConsumer = session.createConsumer(destination);
        messageConsumer.setMessageListener(message -> {
            try {
                messagingTemplate.convertAndSend("/" + username, ((ObjectMessage) message).getObject());
            } catch (JMSException e) {
                e.printStackTrace();
            }
        });
    }
}
