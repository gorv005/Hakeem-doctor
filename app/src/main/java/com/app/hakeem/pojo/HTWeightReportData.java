package com.app.hakeem.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by GAURAV on 2/18/2018.
 */

public class HTWeightReportData implements Serializable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("weight")
    @Expose
    private String weight;
    @SerializedName("rest_hr")
    @Expose
    private String restHr;
    @SerializedName("height")
    @Expose
    private String height;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("doctor_id")
    @Expose
    private Integer doctorId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getRestHr() {
        return restHr;
    }

    public void setRestHr(String restHr) {
        this.restHr = restHr;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }
}
