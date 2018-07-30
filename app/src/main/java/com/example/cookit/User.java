package com.example.cookit;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class User {
    @PrimaryKey
    @NonNull
    private String emailAddress;
    private String fullName;

    public User(String fullName, String emailAddress)
    {
        this.fullName = fullName;
        this.emailAddress    = emailAddress;
    }

    public String getFullName()
    {
        return (this.fullName);
    }

    public String getEmailAddress()
    {
        return (this.emailAddress);
    }

    public void setFullName(String fullName)
    {
        this.fullName = fullName;
    }

    public void setEmailAddress(String emailAddress)
    {
        this.emailAddress = emailAddress;
    }
}