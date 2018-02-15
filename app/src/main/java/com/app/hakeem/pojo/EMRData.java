package com.app.hakeem.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by gaurav.garg on 15-02-2018.
 */

public class EMRData implements Serializable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("patient_id")
    @Expose
    private Integer patientId;
    @SerializedName("doctor_id")
    @Expose
    private Integer doctorId;
    @SerializedName("current_grade")
    @Expose
    private String currentGrade;
    @SerializedName("sub_specialist")
    @Expose
    private String subSpecialist;
    @SerializedName("classification")
    @Expose
    private String classification;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("workplace")
    @Expose
    private String workplace;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("email")
    @Expose
    private String email;

    /**
     * No args constructor for use in serialization
     *
     */
    public EMRData() {
    }

    /**
     *
     * @param lastName
     * @param currentGrade
     * @param workplace
     * @param photo
     * @param id
     * @param patientId
     * @param email
     * @param doctorId
     * @param createdAt
     * @param classification
     * @param gender
     * @param firstName
     * @param subSpecialist
     */
    public EMRData(Integer id, Integer patientId, Integer doctorId, String currentGrade, String subSpecialist, String classification, String createdAt, String photo, String gender, String workplace, String firstName, String lastName, String email) {
        super();
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.currentGrade = currentGrade;
        this.subSpecialist = subSpecialist;
        this.classification = classification;
        this.createdAt = createdAt;
        this.photo = photo;
        this.gender = gender;
        this.workplace = workplace;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public String getCurrentGrade() {
        return currentGrade;
    }

    public void setCurrentGrade(String currentGrade) {
        this.currentGrade = currentGrade;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getWorkplace() {
        return workplace;
    }

    public void setWorkplace(String workplace) {
        this.workplace = workplace;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
