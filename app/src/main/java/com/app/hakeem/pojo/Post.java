package com.app.hakeem.pojo;

/**
 * Created by aditya.singh on 1/23/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Post implements Serializable{

    @SerializedName("tags")
    @Expose
    private String tags;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("post_by")
    @Expose
    private String postBy;
    @SerializedName("user_pic")
    @Expose
    private String userPic;
    @SerializedName("total_likes")
    @Expose
    private Integer totalLikes;
    @SerializedName("icon_url")
    @Expose
    private String iconUrl;
    @SerializedName("specialist")
    @Expose
    private String specialist;
    @SerializedName("liked_on")
    @Expose
    private String likedOn;
    @SerializedName("post_id")
    @Expose
    private Integer postId;
    @SerializedName("total_comments")
    @Expose
    private Integer total_comments;
    @SerializedName("is_liked")
    @Expose
    private Integer isLiked;

    /**
     * No args constructor for use in serialization
     *
     */
    public Post() {
    }

    /**
     *
     * @param content
     * @param tags
     * @param totalLikes
     * @param userPic
     * @param likedOn
     * @param specialist
     * @param iconUrl
     * @param isLiked
     * @param type
     * @param postId
     * @param url
     * @param postBy
     */
    public Post(String tags, String type, String url, String content, String postBy, String userPic, Integer totalLikes, String iconUrl, String specialist, String likedOn, Integer postId, Integer isLiked) {
        super();
        this.tags = tags;
        this.type = type;
        this.url = url;
        this.content = content;
        this.postBy = postBy;
        this.userPic = userPic;
        this.totalLikes = totalLikes;
        this.iconUrl = iconUrl;
        this.specialist = specialist;
        this.likedOn = likedOn;
        this.postId = postId;
        this.isLiked = isLiked;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPostBy() {
        return postBy;
    }

    public void setPostBy(String postBy) {
        this.postBy = postBy;
    }

    public String getUserPic() {
        return userPic;
    }

    public void setUserPic(String userPic) {
        this.userPic = userPic;
    }

    public Integer getTotalLikes() {
        return totalLikes;
    }

    public void setTotalLikes(Integer totalLikes) {
        this.totalLikes = totalLikes;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getSpecialist() {
        return specialist;
    }

    public void setSpecialist(String specialist) {
        this.specialist = specialist;
    }

    public String getLikedOn() {
        return likedOn;
    }

    public void setLikedOn(String likedOn) {
        this.likedOn = likedOn;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Integer getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(Integer isLiked) {
        this.isLiked = isLiked;
    }

    public Integer getTotal_comments() {
        return total_comments;
    }

    public void setTotal_comments(Integer total_comments) {
        this.total_comments = total_comments;
    }
}