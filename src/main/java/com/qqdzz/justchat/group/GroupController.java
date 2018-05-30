package com.qqdzz.justchat.group;

import com.qqdzz.justchat.message.Message;
import com.qqdzz.justchat.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author BadCode
 * @date 2018-05-27 18:52
 **/
@RestController
public class GroupController {

    private Connection jmsConnection;
    private Map<String, Session> jmsSessions;
    private SimpMessagingTemplate messagingTemplate;
    private Map<String, User> users;

    @Autowired
    public GroupController(Connection jmsConnection, Map<String, Session> jmsSessions,
                           SimpMessagingTemplate messagingTemplate, Map<String, User> users) {
        this.jmsConnection = jmsConnection;
        this.jmsSessions = jmsSessions;
        this.messagingTemplate = messagingTemplate;
        this.users = users;
    }

    @PostMapping("/temp-group")
    public void createTempGroup(@RequestBody Group group) {
        System.out.println(group);
        try {
            Session session = jmsConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            jmsSessions.put(group.getName(), session);
            System.out.println(jmsSessions.size());
            Destination destination = session.createQueue(group.getName());
            MessageConsumer messageConsumer = session.createConsumer(destination);
            messageConsumer.setMessageListener(message -> {
                try {
                    messagingTemplate.convertAndSend("/" + group.getName(), ((ObjectMessage) message).getObject());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            });
        } catch (JMSException e) {
            e.printStackTrace();
        }

        for (String username : group.getMembers()) {
            if (users.containsKey(username)) {
                addUserToGroup(username, group.getName());

                Message msg = new Message();
                msg.setType(Message.ADD_GROUP);
                msg.setContent(group.getName());
                messagingTemplate.convertAndSend("/" + username, msg);
            }
        }
    }

    @PostMapping("/group")
    public void createGroup(@RequestBody Group group) throws JMSException {
        System.out.println(group);
        Session session = jmsConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        jmsSessions.put(group.getName(), session);
        System.out.println(jmsSessions.size());
        Destination destination = session.createTopic(group.getName());
        for (String username : group.getMembers()) {
            addUserToGroup(username, group.getName());
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

    private void addUserToGroup(String username, String groupName) {
        if (users.containsKey(username)) {
            User user = users.get(username);
            if (user.getJoinedGroup() != null) {
                user.getJoinedGroup().add(groupName);
            } else {
                List<String> joinedGroup = new ArrayList<>();
                joinedGroup.add(groupName);
                user.setJoinedGroup(joinedGroup);
            }
        }
    }
}
