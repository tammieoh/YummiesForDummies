package com.example.yummiesfordummies;

import java.util.ArrayList;
import java.util.List;

public class User {
    public String userID;
    public String username;
//    public String password;
    public String email;
    public List<String> favorites = new ArrayList();

    public User(String userID, String username, String email, List<String> favorites) {
        this.userID = userID;
        this.username = username;
//        this.password = password;
        this.email = email;
        this.favorites = favorites;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public List<String> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<String> favorites) {
        this.favorites = favorites;
    }

}
