package com.robillo.sasuke.dailysuvichar.models;

/**
 * Created by rishabhshukla on 24/04/17.
 */

public class Blog {
    String blogUrl;
    String title;

    public String getBlogUrl() {
        return blogUrl;
    }

    public void setBlogUrl(String blogUrl) {
        this.blogUrl = blogUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Blog(String blogUrl, String title) {

        this.blogUrl = blogUrl;
        this.title = title;
    }
}
