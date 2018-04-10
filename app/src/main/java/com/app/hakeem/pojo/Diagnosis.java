package com.app.hakeem.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by gaurav.garg on 10-04-2018.
 */

public class Diagnosis implements Serializable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("details")
    @Expose
    private String details;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    /**
     * No args constructor for use in serialization
     *
     */
    public Diagnosis() {
    }

    /**
     *
     * @param id
     * @param details
     * @param createdAt
     * @param type
     */
    public Diagnosis(Integer id, String type, String details, String createdAt) {
        super();
        this.id = id;
        this.type = type;
        this.details = details;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
