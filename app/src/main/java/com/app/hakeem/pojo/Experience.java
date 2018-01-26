package com.app.hakeem.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by GAURAV on 1/26/2018.
 */

public class Experience implements Serializable{

    @SerializedName("hospital_name")
    @Expose
    private String hospitalName;
    @SerializedName("resigned_since")
    @Expose
    private String resignedSince;
    @SerializedName("worked_since")
    @Expose
    private String workedSince;

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getResignedSince() {
        return resignedSince;
    }

    public void setResignedSince(String resignedSince) {
        this.resignedSince = resignedSince;
    }

    public String getWorkedSince() {
        return workedSince;
    }

    public void setWorkedSince(String workedSince) {
        this.workedSince = workedSince;
    }

}
