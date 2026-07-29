package com.banking.model;

public class User {
    private int userId;
    private String username;
    private String password;
    private String fullName;
    private String nationalId;
    private String phoneNumber;

    public User(int userId, String username, String password, String fullName, String nationalId, String phoneNumber) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.nationalId = nationalId;
        this.phoneNumber = phoneNumber;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFullName() {
        return fullName;
    }

    public String getNationalId() {
        return nationalId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}