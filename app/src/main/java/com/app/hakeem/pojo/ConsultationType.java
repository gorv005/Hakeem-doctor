package com.app.hakeem.pojo;

/**
 * Created by Ady on 2/18/2018.
 */

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConsultationType {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status_code")
    @Expose
    private String statusCode;
    @SerializedName("awareness")
    @Expose
    private ArrayList<Awareness> awareness = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public ConsultationType() {
    }

    /**
     *
     * @param statusCode
     * @param awareness
     */
    public ConsultationType(String statusCode, ArrayList<Awareness> awareness) {
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
}
