package com.techhab.collegeapp;

import android.location.Location;

/**
 * Created by jhchoe on 10/16/14.
 *
 * This class provides user information
 */
public class User {

    private String id;
    private String password;
    private String name;
    private String firstName;
    private String middleName;
    private String lastName;
    private int studentId;
    private Location location;

    public User() {
        // Empty constructor.
        // no reason to use this.
    }

    public User(String id, String password) {
        setUserId(id);
        setPassword(password);
        isValid();
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

    public String getUserName() {
        return name;
    }

    public void setUserName(String name) {
        this.name = name;
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

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public boolean isValid() {
        // TODO authenticate if the user is valid user.
        // TODO if valid, load user information before return true
        loadInfo();
        return true;
    }

    private void loadInfo() {
        // TODO load user information using userID and password
        // TODO and set those information using setter methods
    }
}
