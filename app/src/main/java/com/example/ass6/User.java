package com.example.ass6;

import java.util.List;

public class User {
    private String username;
    private String password;
    private String email;
    private String phone;
    private List<String> interest;
    private String subscription;

    public User(String username, String password, String email, String phone, List<String> interest, String subscription) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.interest = interest;
        this.subscription = subscription;
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getInterest() {
        return interest;
    }

    public void setInterest(List<String> interest) {
        this.interest = interest;
    }

    public String getSubscription(){
        return subscription;
    }


}
