package com.example.listassist;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.listassist.db.AppDatabase;

import java.util.Date;
/**
 *Author: Conner Jordan
 *Date: 4/15/23
 *Description: A POJO class for the User objects stored in our DB.
 */
@Entity(tableName = AppDatabase.USER_TABLE)
public class User {
    @PrimaryKey(autoGenerate = true)
    private int mUserID;
    private String mUsername;
    private String mPassword;
    private Boolean isAdmin;
    private Date mDate;

    public User(String mUsername, String mPassword) {
        this.mUsername = mUsername;
        this.mPassword = mPassword;
        isAdmin = false;
        mDate = new Date();
    }

    public int getUserID() {
        return mUserID;
    }

    public void setUserID(int mUserID) {
        this.mUserID = mUserID;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        this.mUsername = username;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        this.mPassword = password;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        this.mDate = date;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    @Override
    public String toString() {
        return "UserID:" + mUserID + " Username:" + mUsername + " Password:" + mPassword + " Date:" + mDate;
    }
}
