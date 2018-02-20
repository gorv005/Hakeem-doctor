package com.app.hakeem.pojo;

/**
 * Created by Ady on 2/20/2018.
 */


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OnlineDoctor {

    @SerializedName("doctor_id")
    @Expose
    private Integer doctorId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("mobile_no")
    @Expose
    private String mobileNo;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("status")
    @Expose
    private String status;

    /**
     * No args constructor for use in serialization
     *
     */
    public OnlineDoctor() {
    }

    /**
     *
     * @param status
     * @param email
     * @param doctorId
     * @param name
     * @param gender
     * @param mobileNo
     */
    public OnlineDoctor(Integer doctorId, String name, String mobileNo, String email, String gender, String status) {
        super();
        this.doctorId = doctorId;
        this.name = name;
        this.mobileNo = mobileNo;
        this.email = email;
        this.gender = gender;
        this.status = status;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}