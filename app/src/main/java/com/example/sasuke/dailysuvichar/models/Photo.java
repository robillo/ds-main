package com.example.sasuke.dailysuvichar.models;

import android.support.annotation.NonNull;

import java.util.ArrayList;

/**
 * Created by Sasuke on 5/7/2017.
 */

public class Photo {

    @NonNull
    private String name;
    @NonNull
    private Long size;
    @NonNull
    private String language;
    @NonNull
    private String encoding;

    public Photo(@NonNull String name, @NonNull Long size, @NonNull String language, @NonNull String encoding, @NonNull String bucket, @NonNull String user, @NonNull Long timestamp, @NonNull Integer likes, @NonNull Integer shares, @NonNull ArrayList<Comment> comments, @NonNull String caption, @NonNull String uid, @NonNull ArrayList<String> tags, @NonNull int photoURL) {
        this.name = name;
        this.size = size;
        this.language = language;
        this.encoding = encoding;
        this.bucket = bucket;
        this.user = user;
        this.timestamp = timestamp;
        this.likes = likes;
        this.shares = shares;
        this.comments = comments;
        this.caption = caption;
        this.uid = uid;
        this.tags = tags;
        this.photoURL = photoURL;
    }

    @NonNull
    private String bucket;

    @NonNull
    private String user;

    @NonNull
    private Long timestamp;

    @NonNull
    private Integer likes;

    @NonNull
    private Integer shares;

    @NonNull
    private ArrayList<Comment> comments;
    @NonNull
    private String caption;


    @NonNull
    private String uid;

    @NonNull
    private ArrayList<String> tags;

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getUser() {
        return user;
    }

    public void setUser(@NonNull String user) {
        this.user = user;
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
    public String getCaption() {
        return caption;
    }

    public void setCaption(@NonNull String caption) {
        this.caption = caption;
    }

    @NonNull
    public String getUid() {
        return uid;
    }

    public void setUid(@NonNull String uid) {
        this.uid = uid;
    }

    @NonNull
    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(@NonNull ArrayList<String> tags) {
        this.tags = tags;
    }

    @NonNull
    public Long getSize() {
        return size;
    }

    public void setSize(@NonNull Long size) {
        this.size = size;
    }

    @NonNull
    public String getLanguage() {
        return language;
    }

    public void setLanguage(@NonNull String language) {
        this.language = language;
    }

    @NonNull
    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(@NonNull String encoding) {
        this.encoding = encoding;
    }

    @NonNull
    public String getBucket() {
        return bucket;
    }

    public void setBucket(@NonNull String bucket) {
        this.bucket = bucket;
    }

    public Photo() {
    }

    public Photo(@NonNull int photoURL) {
        this.photoURL = photoURL;
    }

    @NonNull
    private int photoURL;

    @NonNull
    public int getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(@NonNull int photoURL) {
        this.photoURL = photoURL;
    }
}
