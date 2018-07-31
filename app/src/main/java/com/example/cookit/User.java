package com.example.cookit;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class User {
    @PrimaryKey
    @NonNull
    private String UID;
    private String fullName;

    public User(String fullName, String emailAddress)
    {
        this.fullName = fullName;
        this.UID = emailAddress;
    }

    public String getFullName()
    {
        return (this.fullName);
    }

    public String getUID()
    {
        return (this.UID);
    }

    public void setFullName(String fullName)
    {
        this.fullName = fullName;
    }

    public void setUID(String UID)
    {
        this.UID = UID;
    }
}