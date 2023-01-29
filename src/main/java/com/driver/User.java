package com.driver;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String name;

    private List<Message> messageList= new ArrayList<>();

    public List<Message> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }

    public User(String name, String mobile) {
        this.name = name;
        this.mobile = mobile;
    }

    public User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    private String mobile;
}
