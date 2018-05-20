package com.android.list;

import java.util.ArrayList;

/**
 * Created by isaiah on 2018-05-13.
 */

public class User {
    private String ID, username, firstName, lastName;
    private String email;
    private ArrayList<User> friends;

    User() {
        firstName = "isaiah";
        email = "isaiahballah@gmail.com";
        friends = new ArrayList<User>();
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<User> getFriends() {
        return friends;
    }


}
