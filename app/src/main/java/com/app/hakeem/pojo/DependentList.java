package com.app.hakeem.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by gaurav.garg on 12-02-2018.
 */

public class DependentList implements Serializable{
    @SerializedName("status_code")
    @Expose
    private String statusCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("patient")
    @Expose
    private Patient patient;

    /**
     * No args constructor for use in serialization
     *
     */
    public DependentList() {
    }

    /**
     *
     * @param message
     * @param statusCode
     * @param patient
     */
    public DependentList(String statusCode, String message, Patient patient) {
        super();
        this.statusCode = statusCode;
        this.message = message;
        this.patient = patient;
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

}
