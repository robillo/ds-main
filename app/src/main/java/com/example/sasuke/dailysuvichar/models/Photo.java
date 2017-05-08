package com.example.sasuke.dailysuvichar.models;

import android.support.annotation.NonNull;

/**
 * Created by Sasuke on 5/7/2017.
 */

public class Photo {

    public Photo() {
    }

    public Photo(@NonNull int photo) {
        this.photo = photo;
    }

    @NonNull
    private int photo;

    @NonNull
    public int getPhoto() {
        return photo;
    }

    public void setPhoto(@NonNull int photo) {
        this.photo = photo;
    }
}
