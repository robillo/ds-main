package com.example.sasuke.dailysuvichar.models;

/**
 * Created by rishabhshukla on 24/04/17.
 */

public class Comment {
    String commenterName;
    String commentDescription;

    public Comment(String commenterName, String commentDescription) {
        this.commenterName = commenterName;
        this.commentDescription = commentDescription;
    }

    public void setCommenterName(String commenterName) {
        this.commenterName = commenterName;
    }

    public void setCommentDescription(String commentDescription) {
        this.commentDescription = commentDescription;
    }

    public String getCommenterName() {
        return commenterName;
    }

    public String getCommentDescription() {
        return commentDescription;
    }
}
