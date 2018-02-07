package com.app.hakeem.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by gaurav.garg on 07-02-2018.
 */

public class Urls implements Serializable{
    @SerializedName("document")
    @Expose
    private String document;
    @SerializedName("photo")
    @Expose
    private String photo;

    /**
     * No args constructor for use in serialization
     *
     */
    public Urls() {
    }

    /**
     *
     * @param document
     * @param photo
     */
    public Urls(String document, String photo) {
        super();
        this.document = document;
        this.photo = photo;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}

