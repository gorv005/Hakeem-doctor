package com.app.hakeem.pojo;

/**
 * Created by Ady on 2/18/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Awareness {

    @SerializedName("category_id")
    @Expose
    private Integer categoryId;
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("icon_url")
    @Expose
    private String iconUrl;

    /**
     * No args constructor for use in serialization
     *
     */
    public Awareness() {
    }

    /**
     *
     * @param categoryName
     * @param iconUrl
     * @param categoryId
     */
    public Awareness(Integer categoryId, String categoryName, String iconUrl) {
        super();
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.iconUrl = iconUrl;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

}