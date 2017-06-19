package com.dmitriytitov.ritgtesttask.fragments;

/**
 * Created by Dmitriy Titov on 19.06.2017.
 */

public class UserData {
    private String name;
    private String email;
    private String password;

    UserData() {

    }

    public UserData(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
