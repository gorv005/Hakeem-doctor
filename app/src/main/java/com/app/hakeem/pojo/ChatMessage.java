package com.app.hakeem.pojo;

/**
 * Created by Ady on 2/18/2018.
 */

public class ChatMessage {


    public static final String TABLE = "ChatMessage";
    public static String KEY_Id = "key_id";
    public static final String KEY_SENDER = "sender";
    public static final String KEY_RECIEVER = "rECIEVER";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_IS_DELIVER = "key_is";
    public static final String KEY_MESSAGE_STATUS = "status";

    public static int STATUS_MESSAGE_SENT =1;
    public static int STATUS_MESSAGE_RECIVED =2;
    String message;
    String sender;
    String receiver;
    String isDeliverd;
    int messageStatus =-1;
    long id;

    public ChatMessage() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String isDeliver() {
        return isDeliverd;
    }

    public void setDelivered(String reciver) {
        isDeliverd = reciver;
    }

    public int getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(int messageStatus) {
        this.messageStatus = messageStatus;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }



}
