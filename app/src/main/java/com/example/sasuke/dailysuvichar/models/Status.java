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
    private String name;

    @NonNull
    private String timestamp;

    @NonNull
    private Integer likes;

    @NonNull
    private Integer shares;

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
