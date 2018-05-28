package com.qqdzz.justchat.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

/**
 * @author BadCode
 * @date 2018-05-22 21:35
 **/
@Controller
public class MessageController {

    private JmsTemplate jmsTemplate;

    @Autowired
    public MessageController(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @MessageMapping("/chat")
    public void onReceiveMessage(Message message) {
        jmsTemplate.convertAndSend(message.getReceiverId(), message);
    }
}
