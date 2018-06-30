package com.example.cookit;

import java.util.ArrayList;
import java.util.Arrays;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.support.annotation.NonNull;

@Entity
public class Recipe {
    @PrimaryKey
    @NonNull
    private String id;
    private String name;
    private String uploaderEmail;
    private String uploaderName;
    private String picture;
    private ArrayList<Ingredient> ingredients;
    private ArrayList<String> preparation;

    public Recipe() {
        ingredients = new ArrayList<>();
        preparation = new ArrayList<>();
    }

    public Recipe(String name,
                  User uploader,
                  String picture,
                  ArrayList<Ingredient> ingredients,
                  ArrayList<String> preparation) {
        this.id = "1";
        this.name = name;
        this.uploaderEmail = uploader.getEmail();
        this.uploaderName = uploader.getFullName();
        this.picture = picture;
        this.ingredients = ingredients;
        this.preparation = preparation;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getUploaderEmail() {
        return uploaderEmail;
    }

    public void setUploaderEmail(String uploaderEmail) {
        this.uploaderEmail = uploaderEmail;
    }

    public String getUploaderName() {
        return uploaderName;
    }

    public void setUploaderName(String uploaderName) {
        this.uploaderName = uploaderName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<String> getPreparation() {
        return preparation;
    }

    public void setPreparation(ArrayList<String> preparation) {
        this.preparation = preparation;
    }
}

