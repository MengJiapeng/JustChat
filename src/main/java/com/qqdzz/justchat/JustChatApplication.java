package com.qqdzz.justchat;

import com.qqdzz.justchat.user.User;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Session;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author badcode
 * @date 2018/05/22
 */
@SpringBootApplication
@EnableJms
public class JustChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(JustChatApplication.class, args);
	}

	@Bean
	public Map<String, User> users() {
		String password = "123456";
		HashMap<String, User> accounts = new HashMap<>(4);
		List<String> groupList = new ArrayList<>();
		groupList.add("hello");
		groupList.add("world");
		accounts.put("user1", new User("user1", password, groupList));
		accounts.put("user2", new User("user2", password, groupList));
		accounts.put("user3", new User("user3", password, null));
		accounts.put("user4", new User("user4", password, null));
		return accounts;
	}

	@Bean
	public Connection jmsConnection(ConnectionFactory connectionFactory) {
		Connection connection = null;
		try {
			connection = connectionFactory.createConnection();
			connection.start();
		} catch (JMSException e) {
			e.printStackTrace();
		}
		return connection;
	}

	@Bean
	public Map<String, Session> jmsSessions() {
		return new HashMap<>(4);
	}
}
