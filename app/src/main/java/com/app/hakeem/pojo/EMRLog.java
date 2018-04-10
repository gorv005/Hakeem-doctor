package com.app.hakeem.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by gaurav.garg on 10-04-2018.
 */

public class EMRLog {
    @SerializedName("prescription")
    @Expose
    private List<Prescription> prescription = null;
    @SerializedName("diagnosis")
    @Expose
    private List<Diagnosis> diagnosis = null;
    @SerializedName("followup")
    @Expose
    private List<Object> followup = null;
    @SerializedName("doctor")
    @Expose
    private Doctor doctor;

    /**
     * No args constructor for use in serialization
     *
     */
    public EMRLog() {
    }

    /**
     *
     * @param followup
     * @param prescription
     * @param diagnosis
     * @param doctor
     */
    public EMRLog(List<Prescription> prescription, List<Diagnosis> diagnosis, List<Object> followup, Doctor doctor) {
        super();
        this.prescription = prescription;
        this.diagnosis = diagnosis;
        this.followup = followup;
        this.doctor = doctor;
    }

    public List<Prescription> getPrescription() {
        return prescription;
    }

    public void setPrescription(List<Prescription> prescription) {
        this.prescription = prescription;
    }

    public List<Diagnosis> getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(List<Diagnosis> diagnosis) {
        this.diagnosis = diagnosis;
    }

    public List<Object> getFollowup() {
        return followup;
    }

    public void setFollowup(List<Object> followup) {
        this.followup = followup;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
}
