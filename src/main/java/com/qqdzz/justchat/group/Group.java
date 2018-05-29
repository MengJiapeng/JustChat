package com.qqdzz.justchat.group;

import java.util.Date;
import java.util.List;

/**
 * @author: BadCode
 * @date: 2018-05-27 18:53
 **/
public class Group {

    private String name;
    private List<String> members;
    private Date createTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Group{" +
                "name='" + name + '\'' +
                ", members=" + members +
                ", createTime=" + createTime +
                '}';
    }
}
