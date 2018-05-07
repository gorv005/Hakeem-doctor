package com.app.hakeem.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by gaurav.garg on 07-05-2018.
 */

public class ExperienceDoc implements Serializable {
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
    private Object description;

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
    public ExperienceDoc(String hospitalName, String workedSince, String resignedSince, Object description) {
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

    public Object getDescription() {
        return description;
    }

    public void setDescription(Object description) {
        this.description = description;
    }
}
