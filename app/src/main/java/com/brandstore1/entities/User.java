package com.brandstore1.entities;

/**
 * Created by Ravi on 29-Mar-15.
 */
public class User {

    private String userId;
    private String password;
    private String emailId;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
}
