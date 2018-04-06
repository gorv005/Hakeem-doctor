package com.app.hakeem.pojo;

/**
 * Created by aditya.singh on 4/6/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("is_follow")
    @Expose
    private String isFollow;
    @SerializedName("description")
    @Expose
    private String description;

    public String getIsFollow() {
        return isFollow;
    }

    public void setIsFollow(String isFollow) {
        this.isFollow = isFollow;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}