package com.app.hakeem.pojo;

/**
 * Created by Ady on 2/20/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OnlineDoctor implements Serializable{

    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("current_grade")
    @Expose
    private String currentGrade;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("sub_specialist")
    @Expose
    private String subSpecialist;
    @SerializedName("classification")
    @Expose
    private String classification;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("doctor_id")
    @Expose
    private Integer doctorId;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("workplace")
    @Expose
    private String workplace;
    @SerializedName("photo")
    @Expose
    private String photo;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getCurrentGrade() {
        return currentGrade;
    }

    public void setCurrentGrade(String currentGrade) {
        this.currentGrade = currentGrade;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubSpecialist() {
        return subSpecialist;
    }

    public void setSubSpecialist(String subSpecialist) {
        this.subSpecialist = subSpecialist;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getWorkplace() {
        return workplace;
    }

    public void setWorkplace(String workplace) {
        this.workplace = workplace;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

}