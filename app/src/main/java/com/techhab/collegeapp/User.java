package com.techhab.collegeapp;

import android.location.Location;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;

/**
 * Created by jhchoe on 10/16/14.
 *
 * This class provides user information
 */
public class User {

    private boolean validity = false;

    private String id;
    private String password;
    private boolean active;
    private String name;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private Location location;

    public User() {
        // Empty constructor.
        // no reason to use this.
    }

    public User(String id, String password) {
        setUserId(id);
        setPassword(password);
        validity = false;
//        if (isValid()) {
//            return;
//        } else {
//            // TODO throw an error
//            setUserId("guest");
//            setPassword(null);
//        }
    }

    public void setUserAsGuest() {
        setUserId("guest");
        setPassword(null);
    }

    public String getUserId() {
        return id;
    }

    public void setUserId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getUserName() {
        return name;
    }

    public void setUserName() {
        if (getUserId().equals("guest")) {
            name = "guest";
        } else {
            name = firstName + " " + lastName;
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setValid(boolean valid) {
        this.validity = valid;
    }

    public boolean isValid() {
        // TODO authenticate if the user is valid user.
        // TODO if valid, load user information before return true
        loadInfo();
        return validity;
    }

    private void loadInfo() {
        // TODO load user information using userID and password
        // TODO and set those information using setter methods
    }
}
