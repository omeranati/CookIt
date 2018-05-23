package com.example.cookit;

public class User {
    private String fullName;
    private String email;
    private String password;

    public User(String fullName, String email, String password)
    {
        this.fullName = fullName;
        this.email    = email;
        this.password = password;
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
    public void setPassword(String password)
    {
        this.password = password;
    }
}