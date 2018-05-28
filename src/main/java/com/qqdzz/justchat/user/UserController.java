package com.qqdzz.justchat.user;

import com.qqdzz.justchat.exception.AuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import javax.jms.*;
import java.util.List;
import java.util.Map;

/**
 * @author badcode
 * @date 2018/05/22
 */
@RestController
public class UserController {

    private Map<String, User> users;
    private Connection jmsConnection;
    private SimpMessagingTemplate messagingTemplate;
    private Map<String, Session> jmsSessions;

    @Autowired
    public UserController(Map<String, User> users, Connection jmsConnection,
                          SimpMessagingTemplate messagingTemplate, Map<String, Session> jmsSessions) {
        this.users = users;
        this.jmsConnection = jmsConnection;
        this.messagingTemplate = messagingTemplate;
        this.jmsSessions = jmsSessions;
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public List<String> login(@RequestBody User user) throws Exception {
        System.out.println("I am login");
        String username = user.getUsername();
        String password = user.getPassword();
        System.out.println(password);
        if (users.containsKey(username) && users.get(username).getPassword().equals(password)) {
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
            return users.get(username).getJoinedGroup();
        } else {
            throw new AuthException();
        }
    }

    @GetMapping("/logout/{username}")
    public void logout(@PathVariable String username) {
        Session session = jmsSessions.get(username);
        if (session != null) {
            try {
                session.close();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
        jmsSessions.remove(username);
        System.out.println(jmsSessions.size());
    }
}
