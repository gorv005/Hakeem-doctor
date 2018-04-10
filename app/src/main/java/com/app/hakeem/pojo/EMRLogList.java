package com.app.hakeem.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by gaurav.garg on 10-04-2018.
 */

public class EMRLogList implements Serializable{
    @SerializedName("status_code")
    @Expose
    private String statusCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private EMRLog data;

    /**
     * No args constructor for use in serialization
     *
     */
    public EMRLogList() {
    }

    /**
     *
     * @param statusCode
     * @param data
     */
    public EMRLogList(String statusCode, EMRLog data) {
        super();
        this.statusCode = statusCode;
        this.data = data;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public EMRLog getData() {
        return data;
    }

    public void setData(EMRLog data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
