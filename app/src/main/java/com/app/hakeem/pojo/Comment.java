package com.app.hakeem.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by gaurav.garg on 22-02-2018.
 */

public class Comment implements Serializable {
    @SerializedName("comment_id")
    @Expose
    private Integer commentId;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("comment_by")
    @Expose
    private String commentBy;
    @SerializedName("posted_at")
    @Expose
    private String postedAt;
    @SerializedName("profile_pic")
    @Expose
    private String profile_pic;

    @SerializedName("Usertype")
    @Expose
    private String Usertype;
    @SerializedName("specialization")
    @Expose
    private String specialization;
    /**
     * No args constructor for use in serialization
     *
     */
    public Comment() {
    }

    /**
     *
     * @param comment
     * @param commentId
     * @param postedAt
     * @param commentBy
     */
    public Comment(Integer commentId, String comment, String commentBy, String postedAt) {
        super();
        this.commentId = commentId;
        this.comment = comment;
        this.commentBy = commentBy;
        this.postedAt = postedAt;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCommentBy() {
        return commentBy;
    }

    public void setCommentBy(String commentBy) {
        this.commentBy = commentBy;
    }

    public String getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(String postedAt) {
        this.postedAt = postedAt;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }



    public String getUsertype() {
        return Usertype;
    }

    public void setUsertype(String usertype) {
        Usertype = usertype;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
}
