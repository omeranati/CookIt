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
    private String picture;
    private ArrayList<Ingredient> ingredients;
    private ArrayList<String> preparation;

    public Recipe() {
        ingredients = new ArrayList<>();
        preparation = new ArrayList<>();
    }

    public Recipe(String name,
                  String uploaderEmail,
                  String picture,
                  ArrayList<Ingredient> ingredients,
                  ArrayList<String> preparation) {
        this.name = name;
        this.uploaderEmail = uploaderEmail;
        this.picture = picture;
        this.ingredients = ingredients;
        this.preparation = preparation;
    }

    public String getPreperationString() {
        String recipePrint = "";
        recipePrint +=  /* System.lineSeparator() + uploader.getFullName() + ": " + name + System.lineSeparator() +
        picture + System.lineSeparator() +*/
                "Ingredients:" + System.lineSeparator();

        for (int i = 0; i < ingredients.size(); i++) {
            recipePrint += "• " + ingredients.get(i).getQuantity() + " " + ingredients.get(i).getDescription() + System.lineSeparator();
        }

        recipePrint += System.lineSeparator()+ "Preparation:" + System.lineSeparator();

        for (int i = 0; i < preparation.size(); i++) {
            recipePrint += i+1 + ". " + preparation.get(i) + System.lineSeparator();
        }

        return (recipePrint);
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUploader() { return uploaderEmail; }

    public void setUploader(String uploaderEmail) {
        this.uploaderEmail = uploaderEmail;
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

