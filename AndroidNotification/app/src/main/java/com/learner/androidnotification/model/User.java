package com.learner.androidnotification.model;

import java.io.Serializable;

public class User implements Serializable {

    private String email, token;

    public User(String email, String token) {
        this.email = email;
        this.token = token;
    }

    public User() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
