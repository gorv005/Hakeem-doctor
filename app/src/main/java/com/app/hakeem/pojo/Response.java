package com.app.hakeem.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by aditya.singh on 2/2/2018.
 */

public class Response {

    @SerializedName("status_code")
    @Expose
    private String statusCode;
    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("patient")
    @Expose
    private Patient patient;

    @SerializedName("data")
    @Expose
    private ArrayList<QueuePerson> queuePeople;


    public Response() {
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public ArrayList<QueuePerson> getQueuePeople() {
        return queuePeople;
    }

    public void setQueuePeople(ArrayList<QueuePerson> queuePeople) {
        this.queuePeople = queuePeople;
    }
}
