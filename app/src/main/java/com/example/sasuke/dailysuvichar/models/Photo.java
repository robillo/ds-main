package com.example.sasuke.dailysuvichar.models;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * Created by Sasuke on 5/7/2017.
 */

public class Photo {

    @NonNull
    private String name;

    @NonNull
    @Exclude
    private StorageReference storageReference;

    @NonNull
    private String type;

    @NonNull
    public String getType() {
        return type;
    }

    public void setType(@NonNull String type) {
        this.type = type;
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
    private String language;

    @NonNull
    public String getLanguage() {
        return language;
    }

    public void setLanguage(@NonNull String language) {
        this.language = language;
    }

    public Photo(@NonNull String type, @NonNull String name, @NonNull String user, @NonNull Long timestamp,
                 @NonNull Integer likes, @NonNull Integer shares, @NonNull ArrayList<Comment> comments,
                 @NonNull String caption, @NonNull String uid, @NonNull ArrayList<String> tags,
                 @NonNull Uri photoURL, @NonNull String language) {

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
        this.photoURL = photoURL;
        this.language = language;
    }

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

//    @NonNull
//    public Long getSize() {
//        return size;
//    }
//
//    public void setSize(@NonNull Long size) {
//        this.size = size;
//    }
//
//    @NonNull
//    public String getLanguage() {
//        return language;
//    }
//
//    public void setLanguage(@NonNull String language) {
//        this.language = language;
//    }
//
//    @NonNull
//    public String getEncoding() {
//        return encoding;
//    }
//
//    public void setEncoding(@NonNull String encoding) {
//        this.encoding = encoding;
//    }
//
//    @NonNull
//    public String getBucket() {
//        return bucket;
//    }
//
//    public void setBucket(@NonNull String bucket) {
//        this.bucket = bucket;
//    }

    public Photo() {
    }

//    public Photo(@NonNull int photo) {
//        this.photo = photo;
//    }

    @NonNull
    private Uri photoURL;

    @NonNull
    public Uri getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(@NonNull Uri photoURL) {
        this.photoURL = photoURL;
    }

}
