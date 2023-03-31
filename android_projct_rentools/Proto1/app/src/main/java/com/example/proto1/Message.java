package com.example.proto1;


import android.icu.text.SimpleDateFormat;

import java.util.Date;

public class Message{
    private final String message;
    private String tag;
    private final String message_delivery_time;
    private final String message_delivery_date;
    private int senderID;



    public Message(String message) {
        this.message = message;

        message_delivery_time = getTimeWithoutSecond();
        message_delivery_date = getDay() + " " + getTimeWithSecond();
    }

    private String getTimeWithSecond(){
        return new SimpleDateFormat("HH:mm:ss").format(new Date());
    }

    private String getTimeWithoutSecond(){
        return new SimpleDateFormat("HH:mm").format(new Date());
    }

    private String getDay(){
        return new SimpleDateFormat("dd-MM-yyyy").format(new Date());
    }

    public Message(String message, String message_delivery_time,int senderID,String message_delivery_date) {
        this.message = message;
        this.message_delivery_time = message_delivery_time;
        this.senderID = senderID;
        this.message_delivery_date = message_delivery_date;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getMessage() {
        return message;
    }

    public void setSenderID(int senderID) {
        this.senderID = senderID;
    }

    public int getSenderID() {
        return senderID;
    }

    public String getCurrentTime() {
        return message_delivery_time;
    }

    public String getMessage_delivery_date() {
        return message_delivery_date;
    }
}
