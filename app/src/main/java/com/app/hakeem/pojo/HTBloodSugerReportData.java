package com.app.hakeem.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by GAURAV on 2/18/2018.
 */

public class HTBloodSugerReportData implements Serializable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("timing")
    @Expose
    private String timing;
    @SerializedName("reading")
    @Expose
    private String reading;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("date")
    @Expose
    private String date;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

    public String getReading() {
        return reading;
    }

    public void setReading(String reading) {
        this.reading = reading;
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
}
