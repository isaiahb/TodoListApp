package com.android.list;

import java.util.ArrayList;

/**
 * Created by isaiah on 2018-05-13.
 */

public class User {
    private String username;
    private String email;
    private ArrayList<Task> tasks;
    private ArrayList<User> friends;

    User() {
        username = "isaiah";
        email = "isaiahballah@gmail.com";
        tasks = new ArrayList<Task>();
        friends = new ArrayList<User>();

    }

}