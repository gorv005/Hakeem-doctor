package com.app.hakeem.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gaurav.garg on 22-02-2018.
 */

public class CommentList implements Serializable {
    @SerializedName("status_code")
    @Expose
    private String statusCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("comments")
    @Expose
    private List<Comment> comments = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public CommentList() {
    }

    /**
     *
     * @param message
     * @param statusCode
     * @param comments
     */
    public CommentList(String statusCode, String message, List<Comment> comments) {
        super();
        this.statusCode = statusCode;
        this.message = message;
        this.comments = comments;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
