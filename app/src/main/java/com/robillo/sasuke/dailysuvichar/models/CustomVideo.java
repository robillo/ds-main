package com.robillo.sasuke.dailysuvichar.models;

import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * Created by rishabhshukla on 15/05/17.
 */

public class CustomVideo {
    @NonNull
    private String name;
    @NonNull
    @Exclude
    private StorageReference storageReference;
    @NonNull
    private String user;

    public CustomVideo(){

    }

    @NonNull
    String type;

    @NonNull
    public String getType() {
        return type;
    }

    public void setType(@NonNull String type) {
        this.type = type;
    }

//    public CustomVideo(@NonNull String type, @NonNull String name, @NonNull StorageReference storageReference, @NonNull String user, @NonNull String videoURI, @NonNull Long timestamp, @NonNull Integer likes, @NonNull Integer shares, @NonNull ArrayList<Comment> comments, @NonNull String caption, @NonNull String uid, @NonNull ArrayList<String> tags) {
//
//        this.type = type;
//        this.name = name;
//        this.storageReference = storageReference;
//        this.user = user;
//        this.videoURI = videoURI;
//        this.timestamp = timestamp;
//        this.likes = likes;
//        this.shares = shares;
//        this.comments = comments;
//        this.caption = caption;
//        this.uid = uid;
//        this.tags = tags;
//    }
//
//    public CustomVideo(@NonNull String name, @NonNull String videoURI, @NonNull Integer likes, @NonNull String caption) {
//        this.name = name;
//        this.videoURI = videoURI;
//        this.likes = likes;
//        this.caption = caption;
//    }

    public CustomVideo(@NonNull String type, @NonNull String name, @NonNull String user, @NonNull Long timestamp,
                       @NonNull Integer likes, @NonNull Integer shares, @NonNull ArrayList<Comment> comments,
                       @NonNull String caption, @NonNull String uid, @NonNull ArrayList<String> tags, @NonNull String language) {

        this.type = type;
        this.name = name;
        this.user = user;
        this.timestamp = timestamp;
        this.likes = likes;
        this.shares = shares;
        this.comments = comments;
        this.caption = caption;
        this.uid = uid;
        this.tags = tags;
        this.language = language;
    }


    @NonNull
    ArrayList<String> likedUsers;


    @Exclude
    private String postUid;

    @NonNull
    public ArrayList<String> getLikedUsers() {
        return likedUsers;
    }

    public void setLikedUsers(@NonNull ArrayList<String> likedUsers) {
        this.likedUsers = likedUsers;
    }

    public String getPostUid() {
        return postUid;
    }

    public void setPostUid(String postUid) {
        this.postUid = postUid;
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

    @NonNull
    public String getVideoURI() {
        return videoURI;
    }

    public void setVideoURI(@NonNull String videoURI) {
        this.videoURI = videoURI;
    }

    @NonNull
    private String videoURI;

    @NonNull
    private Long timestamp;


    @NonNull
    private Integer likes;

    @NonNull
    private Integer shares;

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    @Exclude
    public StorageReference getStorageReference() {
        return storageReference;
    }

    @Exclude
    public void setStorageReference(@NonNull StorageReference storageReference) {
        this.storageReference = storageReference;
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

    private ArrayList<Comment> comments;
    @NonNull
    private String caption;


    @NonNull
    private String uid;

    @NonNull
    private ArrayList<String> tags;

}
