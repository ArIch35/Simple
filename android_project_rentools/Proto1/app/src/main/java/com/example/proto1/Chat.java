package com.example.proto1;

public class Chat {
    private final User chatPartner;
    private final int chatID;
    private final Message lastMessage;

    public Chat(User chatPartner,int chatID, Message lastMessage) {
        this.chatPartner = chatPartner;
        this.chatID = chatID;
        this.lastMessage = lastMessage;
    }

    public User getChatPartner() {
        return chatPartner;
    }

    public int getChatID() {
        return chatID;
    }

    public String getLastMessage(){
        return lastMessage.getMessage();
    }
}
