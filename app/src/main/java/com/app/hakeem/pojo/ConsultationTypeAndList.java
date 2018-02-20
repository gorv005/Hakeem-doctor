package com.app.hakeem.pojo;

/**
 * Created by Ady on 2/18/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ConsultationTypeAndList {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status_code")
    @Expose
    private String statusCode;
    @SerializedName("awareness")
    @Expose
    private ArrayList<Awareness> awareness = null;

    @SerializedName("specialization")
    @Expose
    private ArrayList<OnlineDoctor> onlineDoctors = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public ConsultationTypeAndList() {
    }

    /**
     *
     * @param statusCode
     * @param awareness
     */
    public ConsultationTypeAndList(String statusCode, ArrayList<Awareness> awareness) {
        super();
        this.statusCode = statusCode;
        this.awareness = awareness;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public ArrayList<Awareness> getAwareness() {
        return awareness;
    }

    public void setAwareness(ArrayList<Awareness> awareness) {
        this.awareness = awareness;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<OnlineDoctor> getOnlineDoctors() {
        return onlineDoctors;
    }

    public void setOnlineDoctors(ArrayList<OnlineDoctor> onlineDoctors) {
        this.onlineDoctors = onlineDoctors;
    }
}
