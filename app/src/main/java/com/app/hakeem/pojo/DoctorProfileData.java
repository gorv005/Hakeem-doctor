package com.app.hakeem.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gaurav.garg on 07-05-2018.
 */

public class DoctorProfileData implements Serializable {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("fname")
    @Expose
    private String fname;
    @SerializedName("lname")
    @Expose
    private String lname;
    @SerializedName("doctor_id")
    @Expose
    private String doctorIid;
    @SerializedName("about_me")
    @Expose
    private String aboutMe;
    @SerializedName("education")
    @Expose
    private List<Education> education = null;
    @SerializedName("experience")
    @Expose
    private List<ExperienceDoc> experience = null;
    @SerializedName("home_location")
    @Expose
    private String homeLocation;
    @SerializedName("upload")
    @Expose
    private String upload;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("location")
    @Expose
    private String location;

    /**
     * No args constructor for use in serialization
     *
     */
    public DoctorProfileData() {
    }

    /**
     *
     * @param homeLocation
     * @param name
     * @param experience
     * @param education
     * @param upload
     * @param aboutMe
     */
    public DoctorProfileData(String name, String aboutMe, List<Education> education, List<ExperienceDoc> experience, String homeLocation, String upload) {
        super();
        this.name = name;
        this.aboutMe = aboutMe;
        this.education = education;
        this.experience = experience;
        this.homeLocation = homeLocation;
        this.upload = upload;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public List<Education> getEducation() {
        return education;
    }

    public void setEducation(List<Education> education) {
        this.education = education;
    }

    public List<ExperienceDoc> getExperience() {
        return experience;
    }

    public void setExperience(List<ExperienceDoc> experience) {
        this.experience = experience;
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

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getDoctorIid() {
        return doctorIid;
    }

    public void setDoctorIid(String doctorIid) {
        this.doctorIid = doctorIid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
