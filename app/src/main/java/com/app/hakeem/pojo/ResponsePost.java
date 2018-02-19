package com.app.hakeem.pojo;

/**
 * Created by aditya.singh on 1/23/2018.
 */


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponsePost {

    @SerializedName("status_code")
    @Expose
    private String statusCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("posts")
    @Expose
    private ArrayList<Post> posts = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public ResponsePost() {
    }

    /**
     *
     * @param statusCode
     * @param posts
     */
    public ResponsePost(String statusCode, ArrayList<Post> posts) {
        super();
        this.statusCode = statusCode;
        this.posts = posts;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public ArrayList<Post> getPosts() {
        return posts;
    }

    public void setPosts(ArrayList<Post> posts) {
        this.posts = posts;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}