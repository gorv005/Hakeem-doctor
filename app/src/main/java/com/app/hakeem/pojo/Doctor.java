package com.app.hakeem.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by gaurav.garg on 10-04-2018.
 */

public class Doctor implements Serializable{
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("nationality")
    @Expose
    private String nationality;
    @SerializedName("residence")
    @Expose
    private String residence;
    @SerializedName("workplace")
    @Expose
    private String workplace;
    @SerializedName("home_location")
    @Expose
    private String homeLocation;
    @SerializedName("upload")
    @Expose
    private String upload;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    /**
     * No args constructor for use in serialization
     *
     */
    public Doctor() {
    }

    /**
     *
     * @param homeLocation
     * @param nationality
     * @param residence
     * @param email
     * @param createdAt
     * @param name
     * @param gender
     * @param workplace
     * @param upload
     */
    public Doctor(String name, String email, String gender, String nationality, String residence, String workplace, String homeLocation, String upload, String createdAt) {
        super();
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.nationality = nationality;
        this.residence = residence;
        this.workplace = workplace;
        this.homeLocation = homeLocation;
        this.upload = upload;
        this.createdAt = createdAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getResidence() {
        return residence;
    }

    public void setResidence(String residence) {
        this.residence = residence;
    }

    public String getWorkplace() {
        return workplace;
    }

    public void setWorkplace(String workplace) {
        this.workplace = workplace;
    }

    public String getHomeLocation() {
        return homeLocation;
    }

    public void setHomeLocation(String homeLocation) {
        this.homeLocation = homeLocation;
    }

    public String getUpload() {
        return upload;
    }

    public void setUpload(String upload) {
        this.upload = upload;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
