package com.driver;

import java.util.HashMap;
import java.util.List;

public class WhatsappRepository {

    private HashMap<String,User> userRepository;

    private HashMap<Group, List<User>> groupRepository;

    private HashMap<Integer,Message> messageRepository;

    public HashMap<String, User> getUserRepository() {
        return userRepository;
    }

    public void setUserRepository(HashMap<String, User> userRepository) {
        this.userRepository = userRepository;
    }

    public HashMap<Group, List<User>> getGroupRepository() {
        return groupRepository;
    }

    public void setGroupRepository(HashMap<Group, List<User>> groupRepository) {
        this.groupRepository = groupRepository;
    }

    public HashMap<Integer, Message> getMessageRepository() {
        return messageRepository;
    }

    public void setMessageRepository(HashMap<Integer, Message> messageRepository) {
        this.messageRepository = messageRepository;
    }

    public  WhatsappRepository(){
        this.userRepository = new HashMap<>();
        this.groupRepository = new HashMap<>();
        this.messageRepository = new HashMap<>();
    }
}