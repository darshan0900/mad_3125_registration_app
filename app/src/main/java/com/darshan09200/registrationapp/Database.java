package com.darshan09200.registrationapp;

import java.util.ArrayList;

enum Status {
    USER_EXISTS,
    //    USER_DOES_NOT_EXIST,
    USER_ADDED,
    INVALID_PASSWORD,
    LOGGED_IN,
}

public class Database {
    private static Database databaseInstance = null;

    private ArrayList<User> users;

    private User loggedInUser;

    private Database() {
        users = new ArrayList<>();
    }

    public static Database getInstance() {
        if (databaseInstance == null) {
            databaseInstance = new Database();
        }
        return databaseInstance;
    }

    private int getUser(String username) {
        int index = -1;
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            if (user.getUsername().equals(username)) {
                index = i;
                break;
            }
        }
        return index;
    }

    public boolean checkIfExists(User user) {
        return getUser(user.getUsername()) > -1;
    }

    public Status addNewUser(User user) {
        if (checkIfExists(user)) return Status.USER_EXISTS;
        users.add(user);
        return Status.USER_ADDED;
    }

    public Status isValidLogin(String username, String password) {
        Status currentStatus = Status.INVALID_PASSWORD;
        int userIndex = getUser(username);
        if (userIndex > -1) {
            User user = users.get(userIndex);
            if (user.isValid(password)) {
                currentStatus = Status.LOGGED_IN;
                loggedInUser = user;
            }
        }
        return currentStatus;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void logoutUser() {
        loggedInUser = null;
    }
}
