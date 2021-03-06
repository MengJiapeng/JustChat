package com.qqdzz.justchat.handler;

import com.qqdzz.justchat.exception.AuthException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.JMSException;

/**
 * @author BadCode
 * @date 2018-05-24 11:32
 **/
@ControllerAdvice
@RestController
public class GlobalHandler {

    @ExceptionHandler(AuthException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response authExceptionHandler() {
        return new Response("username or password wrong");
    }

    @ExceptionHandler(JMSException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public Response jmsExceptionHandler() {
        return new Response("can't create consumer");
    }
}
