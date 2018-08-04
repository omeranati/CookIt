package com.example.cookit;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class User {
    @PrimaryKey
    @NonNull
    private String userID;
    private String fullName;

    public User(String fullName, String emailAddress) {
        this.fullName = fullName;
        this.userID = emailAddress;
    }

    public String getFullName()
    {
        return (this.fullName);
    }

    public String getUserID()
    {
        return (this.userID);
    }

    public void setFullName(String fullName)
    {
        this.fullName = fullName;
    }

    public void setUserID(String userID)
    {
        this.userID = userID;
    }
}