package com.app.hakeem.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by gaurav.garg on 13-02-2018.
 */

public class AddPost implements Serializable {
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("awareness_id")
    @Expose
    private String awarenessId;
    @SerializedName("tags")
    @Expose
    private String tags;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("url")
    @Expose
    private String url;

    /**
     * No args constructor for use in serialization
     *
     */
    public AddPost() {
    }

    /**
     *
     * @param content
     * @param tags
     * @param userId
     * @param awarenessId
     * @param type
     * @param url
     */
    public AddPost(String userId, String awarenessId, String tags, String type, String content, String url) {
        super();
        this.userId = userId;
        this.awarenessId = awarenessId;
        this.tags = tags;
        this.type = type;
        this.content = content;
        this.url = url;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAwarenessId() {
        return awarenessId;
    }

    public void setAwarenessId(String awarenessId) {
        this.awarenessId = awarenessId;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
