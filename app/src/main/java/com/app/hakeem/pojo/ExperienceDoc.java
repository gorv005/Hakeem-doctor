package com.app.hakeem.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by gaurav.garg on 07-05-2018.
 */

public class ExperienceDoc implements Serializable {
    @SerializedName("exp_id")
    @Expose
    private String expId;
    @SerializedName("hospital_name")
    @Expose
    private String hospitalName;
    @SerializedName("worked_since")
    @Expose
    private String workedSince;
    @SerializedName("resigned_since")
    @Expose
    private String resignedSince;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("joining_date")
    @Expose
    private String joining_date;
    @SerializedName("resigned_date")
    @Expose
    private String resigned_date;

    /**
     * No args constructor for use in serialization
     *
     */
    public ExperienceDoc() {
    }

    /**
     *
     * @param workedSince
     * @param description
     * @param hospitalName
     * @param resignedSince
     */
    public ExperienceDoc(String hospitalName, String workedSince, String resignedSince, String description) {
        super();
        this.hospitalName = hospitalName;
        this.workedSince = workedSince;
        this.resignedSince = resignedSince;
        this.description = description;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getWorkedSince() {
        return workedSince;
    }

    public void setWorkedSince(String workedSince) {
        this.workedSince = workedSince;
    }

    public String getResignedSince() {
        return resignedSince;
    }

    public void setResignedSince(String resignedSince) {
        this.resignedSince = resignedSince;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getJoining_date() {
        return joining_date;
    }

    public void setJoining_date(String joining_date) {
        this.joining_date = joining_date;
    }

    public String getResigned_date() {
        return resigned_date;
    }

    public void setResigned_date(String resigned_date) {
        this.resigned_date = resigned_date;
    }

    public String getExpId() {
        return expId;
    }

    public void setExpId(String expId) {
        this.expId = expId;
    }
}
