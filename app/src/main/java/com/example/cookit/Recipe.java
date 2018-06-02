package com.example.cookit;

public class Recipe {
    private String name;
    private User   uploader;
    private String picture;
    private String[] ingredients;
    private String[] preparation;

    public Recipe(String name,
                       User uploader,
                       String picture,
                       String[] ingredients,
                       String[] preparation) {
        this.name = name;
        this.uploader = uploader;
        this.picture = picture;
        this.ingredients = ingredients;
        this.preparation = preparation;
    }

    public String getName() {
        return name;
    }

    public User getUploader() { return uploader; }

    public String getPreperation() {
        String recipePrint = "";
        recipePrint +=  /* System.lineSeparator() + uploader.getFullName() + ": " + name + System.lineSeparator() +
                picture + System.lineSeparator() +*/
                "Ingredients:" + System.lineSeparator();

        for (int i = 0; i < ingredients.length; i++) {
            recipePrint += "• " + ingredients[i] + System.lineSeparator();
        }

        recipePrint += System.lineSeparator()+ "Preparation:" + System.lineSeparator();

        for (int i = 0; i < preparation.length; i++) {
            recipePrint += i+1 + ". " + preparation[i] + System.lineSeparator();
        }

        return (recipePrint);
    }

}

