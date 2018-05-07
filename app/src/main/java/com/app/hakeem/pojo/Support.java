package com.app.hakeem.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by gaurav.garg on 07-05-2018.
 */

public class Support implements Serializable{
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("problem_type")
    @Expose
    private String problemType;
    @SerializedName("problem_title")
    @Expose
    private String problemTitle;
    @SerializedName("problem_description")
    @Expose
    private String problemDescription;

    /**
     * No args constructor for use in serialization
     *
     */
    public Support() {
    }

    /**
     *
     * @param problemDescription
     * @param phone
     * @param email
     * @param userId
     * @param problemType
     * @param problemTitle
     */
    public Support(String userId, String email, String phone, String problemType, String problemTitle, String problemDescription) {
        super();
        this.userId = userId;
        this.email = email;
        this.phone = phone;
        this.problemType = problemType;
        this.problemTitle = problemTitle;
        this.problemDescription = problemDescription;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProblemType() {
        return problemType;
    }

    public void setProblemType(String problemType) {
        this.problemType = problemType;
    }

    public String getProblemTitle() {
        return problemTitle;
    }

    public void setProblemTitle(String problemTitle) {
        this.problemTitle = problemTitle;
    }

    public String getProblemDescription() {
        return problemDescription;
    }

    public void setProblemDescription(String problemDescription) {
        this.problemDescription = problemDescription;
    }
}
