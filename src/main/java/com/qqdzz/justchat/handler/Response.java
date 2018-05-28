package com.qqdzz.justchat.handler;

/**
 * @author: BadCode
 * @date: 2018-05-22 21:36
 **/
public class Response {

    private String message;

    public Response(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
