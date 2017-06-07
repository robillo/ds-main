package com.example.sasuke.dailysuvichar.models;

import android.support.annotation.NonNull;

import java.util.ArrayList;

/**
 * Created by Sasuke on 5/7/2017.
 */

public class Status {

    @NonNull
    private String uid;

    @NonNull
    public String getUid() {
        return uid;
    }

    public void setUid(@NonNull String uid) {
        this.uid = uid;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(@NonNull Long timestamp) {
        this.timestamp = timestamp;
    }

    @NonNull
    public Integer getLikes() {
        return likes;
    }

    public void setLikes(@NonNull Integer likes) {
        this.likes = likes;
    }

    @NonNull
    public Integer getShares() {
        return shares;
    }

    public void setShares(@NonNull Integer shares) {
        this.shares = shares;
    }

    @NonNull
    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(@NonNull ArrayList<Comment> comments) {
        this.comments = comments;
    }

    @NonNull
    public ArrayList<String> getTags() {
        return tags;

    }

    @NonNull
    private String language;

    @NonNull
    public String getLanguage() {
        return language;
    }

    public void setLanguage(@NonNull String language) {
        this.language = language;
    }

    public void setTags(@NonNull ArrayList<String> tags) {
        this.tags = tags;
    }

    @NonNull
    private ArrayList<String> tags;

    public Status(@NonNull String type, @NonNull String uid, @NonNull ArrayList<String> tags,
                  @NonNull String name, @NonNull Long timestamp, @NonNull Integer likes, @NonNull Integer shares,
                  @NonNull ArrayList<Comment> comments, @NonNull String status, @NonNull String user, @NonNull String language) {

        this.type = type;
        this.uid = uid;
        this.tags = tags;
        this.name = name;
        this.timestamp = timestamp;
        this.likes = likes;
        this.shares = shares;
        this.comments = comments;
        this.status = status;
        this.user = user;
        this.language = language;
    }

    @NonNull
    private String name;

    @NonNull
    private Long timestamp;

    @NonNull
    private Integer likes;

    @NonNull
    private Integer shares;

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @NonNull
    private ArrayList<Comment> comments;

    public Status() {
    }

    public Status( @NonNull String user,@NonNull String status) {
        this.status = status;
        this.user = user;
    }

    public Status(@NonNull String status) {
        this.status = status;
    }

    @NonNull
    private String status;

    @NonNull
    private String user;

    @NonNull
    public String getUser() {
        return user;
    }

    public void setUser(@NonNull String user) {
        this.user = user;
    }

    @NonNull
    public String getStatus() {
        return status;
    }

    public void setStatus(@NonNull String status) {
        this.status = status;
    }
}
