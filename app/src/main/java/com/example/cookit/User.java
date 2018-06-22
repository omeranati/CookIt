package com.example.cookit;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class User {
    @PrimaryKey
    @NonNull
    private String email;
    private String fullName;

    public User(String fullName, String email)
    {
        this.fullName = fullName;
        this.email    = email;
    }

    public String getFullName()
    {
        return (this.fullName);
    }

    public String getEmail()
    {
        return (this.email);
    }

    public void setFullName(String fullName)
    {
        this.fullName = fullName;
    }

    public void setEmail(String password)
    {
        this.email = email;
    }
}