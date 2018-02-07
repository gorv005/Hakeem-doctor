package com.app.hakeem.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by gaurav.garg on 07-02-2018.
 */

public class UploadFileRes implements Serializable {
    @SerializedName("status_code")
    @Expose
    private String statusCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("urls")
    @Expose
    private Urls urls;

    /**
     * No args constructor for use in serialization
     *
     */
    public UploadFileRes() {
    }

    /**
     *
     * @param message
     * @param statusCode
     * @param urls
     */
    public UploadFileRes(String statusCode, String message, Urls urls) {
        super();
        this.statusCode = statusCode;
        this.message = message;
        this.urls = urls;
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

    public Urls getUrls() {
        return urls;
    }

    public void setUrls(Urls urls) {
        this.urls = urls;
    }


}
