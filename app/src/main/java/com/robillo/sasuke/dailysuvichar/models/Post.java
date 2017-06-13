package com.robillo.sasuke.dailysuvichar.models;

import java.util.ArrayList;

/**
 * Created by rishabhshukla on 24/04/17.
 */

public class Post {
    String title;
    String description;
    String language;
    String imageUrl;
    String videoUrl;
    Integer likes;
    ArrayList<Comment> comments;

    public Post(String title, String description, String language, String imageUrl, String videoUrl, Integer likes, ArrayList<Comment> comments) {
        this.title = title;
        this.description = description;
        this.language = language;
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
        this.likes = likes;
        this.comments = comments;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLanguage() {
        return language;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public Integer getLikes() {
        return likes;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }
}
