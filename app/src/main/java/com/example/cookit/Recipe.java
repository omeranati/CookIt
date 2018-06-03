package com.example.cookit;

import java.util.ArrayList;

public class Recipe {
    private String name;
    private User   uploader;
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
        this.name = name;
        this.uploader = uploader;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUploader() { return uploader; }

    public void setUploader(User uploader) {
        this.uploader = uploader;
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

