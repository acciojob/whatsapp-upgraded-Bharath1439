package com.driver;

import java.util.*;

public class WhatsappService {

    WhatsappRepository whatsappRepository=new WhatsappRepository();
    public String createUser(String name, String mobile) throws Exception {
        try {
            if (whatsappRepository.getUserRepository().containsKey(mobile)) {
                throw new Exception("User already exists");
            } else {
                User user = new User();
                user.setName(name);
                user.setMobile(mobile);
                whatsappRepository.getUserRepository().put(name, user);
            }
        }
        catch(Exception e){
            System.out.print(e);
        }
        return "SUCCESS";

    }

    public Group createGroup(List<User> users) throws Exception {
        int length = users.size();
        Group group=new Group();
        if (length<=2){
            throw new Exception("Not enough users to create a group");
        }
        else if(length==2){

            group.setName(users.get(1).getName());
            group.setNumberOfParticipants(length);
            whatsappRepository.getGroupRepository().put(group,users);
        }
        else{
            int gCount=0;
            for(Group temp : whatsappRepository.getGroupRepository().keySet()){
                if(temp.getNumberOfParticipants()>2){
                    gCount++;
                }
            }
            gCount++;
            String groupName = "Group "+gCount;
            group.setName(groupName);
            group.setNumberOfParticipants(length);
            whatsappRepository.getGroupRepository().put(group,users);
        }
        return group;
    }

    public int createMessage(String content) {
        Message message=new Message();
        int count=whatsappRepository.getMessageRepository().size();
        message.setId(count+1);
        message.setContent(content);
        message.setTimestamp();
        whatsappRepository.getMessageRepository().put(message.getId(),message);
        return message.getId();

    }

    public int sendMessage(Message message, User sender, Group group) {
        try{
            if(!whatsappRepository.getGroupRepository().containsKey(group)){
                throw  new Exception("Group does not exist");
            }
            //check if sender is part of the group
            boolean flag = false;
            for(User user  : whatsappRepository.getGroupRepository().get(group)){
                if(user.equals(sender)){
                    flag =true;
                    break;
                }
            }
            if(flag ==false){
                throw  new Exception("You are not allowed to send message");
            }

            message.setGroup(group);
            message.setUser(sender);
            int size = whatsappRepository.getMessageRepository().size();
            message.setId(size+1);
            whatsappRepository.getMessageRepository().put(size+1,message);

            sender.getMessageList().add(message);

            whatsappRepository.getUserRepository().put(sender.getName(),sender);

            group.getMessageList().add(message);
            List<User> users = whatsappRepository.getGroupRepository().get(group);
            whatsappRepository.getGroupRepository().put(group,users);


        }catch (Exception e){
            System.out.println(e);
        }
        return group.getMessageList().size();

    }

    public String changeAdmin(User approver, User user, Group group) throws Exception {
        if(!whatsappRepository.getGroupRepository().containsKey(group)){
            throw  new Exception("Group does not exist");
        }


        if(!whatsappRepository.getGroupRepository().get(group).get(0).equals(approver)){
            throw  new Exception("Approver does not have rights");
        }
        boolean flag = false;
        List<User> userList= whatsappRepository.getGroupRepository().get(group);
        int index =-1;
        for(User user1: userList){
            index++;
            if(user1.equals(user)){
                flag = true;
                break;
            }
        }
        if(flag==false){
            throw new Exception("User is not a participant");
        }

        User temp = userList.get(index);
        userList.set(index,userList.get(0));
        userList.set(0,temp);

        whatsappRepository.getGroupRepository().put(group,userList);

        return "SUCCESS";

    }

    public int removeUser(User user) throws Exception {

        boolean flag = false;
        int index=-1;
        Group userGroup = null;
        for(Group group: whatsappRepository.getGroupRepository().keySet()){
            for(User user1: whatsappRepository.getGroupRepository().get(group)){
                index++;
                if(user.equals(user1)){
                    userGroup = group;
                    flag = true;
                    break;
                }
            }
            if(flag==true){
                break;
            }
            index = -1;
        }

        if(flag==false){
            throw  new Exception("User not found");
        }
        if(index==0){
            throw new Exception("Cannot remove admin");
        }else if(index>0 && userGroup!=null){
            //remove user from group list
            List<User> updatedUserList = new ArrayList<>();
            List<User> userLIst = whatsappRepository.getGroupRepository().get(userGroup);
            for(User updatedUsers: userLIst){
                if(updatedUsers.equals(user))
                    continue;
                updatedUserList.add(updatedUsers);
            }
            whatsappRepository.getGroupRepository().put(userGroup,updatedUserList);

            //removing user from user repo
            whatsappRepository.getUserRepository().remove(user.getMobile());

            //remove user messages from group message list
            List<Message> messageList = userGroup.getMessageList();
            List<Message> updatedMessageList = new ArrayList<>();
            for(Message updatedMessage : messageList){
                if(updatedMessage.getUser().equals(user) && updatedMessage.getGroup().equals(userGroup))
                    continue;
                updatedMessageList.add(updatedMessage);
            }
            userGroup.setMessageList(updatedMessageList);
            whatsappRepository.getGroupRepository().put(userGroup,updatedUserList);

            for(int i: whatsappRepository.getMessageRepository().keySet()){
                if(whatsappRepository.getMessageRepository().get(i).getUser().equals(user) && whatsappRepository.getMessageRepository().get(i).getGroup().equals(userGroup)){
                    whatsappRepository.getMessageRepository().remove(i);
                }
            }

        }

        int noOfUsers = whatsappRepository.getGroupRepository().get(userGroup).size();
        int noOfMessagesInGroup = userGroup.getMessageList().size();
        int overallMessages = whatsappRepository.getMessageRepository().size();

        return noOfUsers+noOfMessagesInGroup+ overallMessages;
    }

    public String findMessage(Date start, Date end, int k) throws Exception {
        List<Message> messageList = new ArrayList<>();
        for(Message message : whatsappRepository.getMessageRepository().values()){
            if(message.getTimestamp().compareTo(start)>0 && message.getTimestamp().compareTo(end)<0){
                messageList.add(message);
            }
        }
        Comparator<Message> compareByDate = (Message m1, Message m2) -> m1.getTimestamp().compareTo(m2.getTimestamp());

        Collections.sort(messageList,compareByDate);

        if(messageList.size()<k){
            throw new Exception("K is greater than the number of messages");
        }else{
            return messageList.get(k-1).getContent();
        }

    }

}
