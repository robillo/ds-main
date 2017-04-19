package com.example.sasuke.dailysuvichar.models;

/**
 * Created by rishabhshukla on 19/04/17.
 */

public class UserAuth {
    String email;
    String name;

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public UserAuth(String email, String name) {
        this.email = email;
        this.name = name;
    }
}
