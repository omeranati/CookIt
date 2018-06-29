package com.example.cookit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

@Entity
public class Recipe implements Parcelable{
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

    public Recipe(Parcel p){
        this.ingredients = new ArrayList<>();
        this.preparation = new ArrayList<>();
        ArrayList<String> data = new ArrayList<>();
        p.readStringList(data);
        this.id = data.get(0);
        this.name = data.get(1);
        this.uploaderEmail = data.get(2);
        this.uploaderName = data.get(3);
        this.picture = data.get(4);

        int ingredientsLength = p.readInt();

        for(int i = 0; i < ingredientsLength; i++){
            this.ingredients.add(new Ingredient(p.readString(),p.readString()));
        }

        int preparationLength = p.readInt();

        for(int i = 0; i < preparationLength; i++){
            this.preparation.add(p.readString());
        }
    }

    public String getPreperationString() {
        String recipePrint = "";
        recipePrint += "Ingredients:" + System.lineSeparator();

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String []{
            this.id,
            this.name,
            this.uploaderEmail,
            this.uploaderName,
            this.picture});

        parcel.writeInt(this.ingredients.size());

        for(Ingredient nextIngredient : this.ingredients) {
            parcel.writeString(nextIngredient.getQuantity());
            parcel.writeString(nextIngredient.getDescription());
        }

        parcel.writeInt(this.preparation.size());

        for (String step : this.preparation) {
            parcel.writeString(step);
        }
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
}

