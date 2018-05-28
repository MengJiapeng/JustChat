package com.qqdzz.justchat.user;


import java.util.List;

/**
 * @author BadCode
 * @date 2018-05-27 15:31
 **/
public class User {

    private String username;
    private String password;
    private List<String> joinedGroup;

    public User() {}

    public User(String username, String password, List<String> joinedGroup) {
        this.username = username;
        this.password = password;
        this.joinedGroup = joinedGroup;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getJoinedGroup() {
        return joinedGroup;
    }

    public void setJoinedGroup(List<String> joinedGroup) {
        this.joinedGroup = joinedGroup;
    }
}
