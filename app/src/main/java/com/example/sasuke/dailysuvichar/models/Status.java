package com.example.sasuke.dailysuvichar.models;

import android.support.annotation.NonNull;

/**
 * Created by Sasuke on 5/7/2017.
 */

public class Status {

    @NonNull
    private String status;

    @NonNull
    public String getStatus() {
        return status;
    }

    public void setStatus(@NonNull String status) {
        this.status = status;
    }
}
