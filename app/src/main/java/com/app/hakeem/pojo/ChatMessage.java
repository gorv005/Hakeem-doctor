package com.app.hakeem.pojo;

/**
 * Created by Ady on 2/18/2018.
 */

public class ChatMessage {

    public static int STATUS_MESSAGE_SENT =1;
    public static int STATUS_MESSAGE_RECIVED =2;
    String message;
    boolean isReciver;
    int messageStatus =-1;

    public ChatMessage() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isReciver() {
        return isReciver;
    }

    public void setReciver(boolean reciver) {
        isReciver = reciver;
    }

    public int getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(int messageStatus) {
        this.messageStatus = messageStatus;
    }
}
