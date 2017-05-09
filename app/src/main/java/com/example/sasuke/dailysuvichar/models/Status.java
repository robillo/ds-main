package com.example.sasuke.dailysuvichar.models;

import android.support.annotation.NonNull;

/**
 * Created by Sasuke on 5/7/2017.
 */

public class Status {

    public Status() {
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
