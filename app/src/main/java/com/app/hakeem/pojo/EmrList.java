package com.app.hakeem.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gaurav.garg on 15-02-2018.
 */

public class EmrList implements Serializable {

    @SerializedName("status_code")
    @Expose
    private String statusCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<EMRData> data = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public EmrList() {
    }

    /**
     *
     * @param message
     * @param statusCode
     * @param data
     */
    public EmrList(String statusCode, String message, List<EMRData> data) {
        super();
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
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

    public List<EMRData> getData() {
        return data;
    }

    public void setData(List<EMRData> data) {
        this.data = data;
    }
}
