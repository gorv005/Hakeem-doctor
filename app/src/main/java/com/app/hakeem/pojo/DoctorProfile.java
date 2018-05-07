package com.app.hakeem.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by gaurav.garg on 07-05-2018.
 */

public class DoctorProfile implements Serializable {
    @SerializedName("status_code")
    @Expose
    private String statusCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private DoctorProfileData data;

    /**
     * No args constructor for use in serialization
     *
     */
    public DoctorProfile() {
    }

    /**
     *
     * @param message
     * @param statusCode
     * @param data
     */
    public DoctorProfile(String statusCode, String message, DoctorProfileData data) {
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

    public DoctorProfileData getData() {
        return data;
    }

    public void setData(DoctorProfileData data) {
        this.data = data;
    }
}
