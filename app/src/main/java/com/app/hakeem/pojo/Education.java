package com.app.hakeem.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by gaurav.garg on 07-05-2018.
 */

public class Education implements Serializable {
    @SerializedName("edu_id")
    @Expose
    private String eduId;
    @SerializedName("university_name")
    @Expose
    private String universityName;
    @SerializedName("description")
    @Expose
    private String description;

    /**
     * No args constructor for use in serialization
     *
     */
    public Education() {
    }

    /**
     *
     * @param eduId
     * @param description
     * @param universityName
     */
    public Education(String eduId, String universityName, String description) {
        super();
        this.eduId = eduId;
        this.universityName = universityName;
        this.description = description;
    }

    public String getEduId() {
        return eduId;
    }

    public void setEduId(String eduId) {
        this.eduId = eduId;
    }

    public String getUniversityName() {
        return universityName;
    }

    public void setUniversityName(String universityName) {
        this.universityName = universityName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
