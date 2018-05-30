package com.qqdzz.justchat.message;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author BadCode
 * @date 2018-05-22 21:29
 **/
public class Message implements Serializable {

    public static final int MESSAGE = 0;
    public static final int ADD_TEMP_GROUP = 1;
    public static final int ADD_GROUP = 2;

    private String receiverId;
    private String senderName;
    private String content;
    private Date time;
    private int type;

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Message{" +
                "receiverId='" + receiverId + '\'' +
                ", senderName='" + senderName + '\'' +
                ", content='" + content + '\'' +
                ", time=" + time +
                ", type=" + type +
                '}';
    }
}
