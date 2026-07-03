package com.banking.model;

public class User {
    
    private int userID;
    private String username;
    private String password;
    private String fullName;

    public User(int userID, String username, String password, String fullName ){
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
    }


    //getters and setters for private fields
    public int getUserId(){
        return userID;
    }

    public void setUserId(int userId){
        this.userID = userId;
    }
    
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
}
