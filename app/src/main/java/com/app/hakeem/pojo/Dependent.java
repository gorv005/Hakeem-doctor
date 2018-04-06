package com.app.hakeem.pojo;

/**
 * Created by aditya.singh on 4/6/2018.
 */


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Dependent implements Serializable{

    @SerializedName("dependent_id")
    @Expose
    private Integer dependentId;
    @SerializedName("name")
    @Expose
    private String name;

    public Integer getDependentId() {
        return dependentId;
    }

    public void setDependentId(Integer dependentId) {
        this.dependentId = dependentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
