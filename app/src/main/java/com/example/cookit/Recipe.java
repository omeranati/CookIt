package com.example.cookit;

import java.util.ArrayList;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

@Entity
public class Recipe implements Parcelable {
    @PrimaryKey
    @NonNull
    private String id;
    private String name;
    private String uploaderUID;
    private String uploaderName;
    private ArrayList<Ingredient> ingredients;
    private ArrayList<String> preparation;

    public Recipe() {
        this.id = null;
        ingredients = new ArrayList<>();
        preparation = new ArrayList<>();
    }

    public Recipe(String name,
                  User uploader,
                  ArrayList<Ingredient> ingredients,
                  ArrayList<String> preparation) {
        this.id = null;
        this.name = name;
        this.uploaderUID = uploader.getUserID();
        this.uploaderName = uploader.getFullName();
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
        this.uploaderUID = data.get(2);
        this.uploaderName = data.get(3);

        int ingredientsLength = p.readInt();

        for(int i = 0; i < ingredientsLength; i++){
            this.ingredients.add(new Ingredient(p.readString(),p.readString()));
        }

        int preparationLength = p.readInt();

        for(int i = 0; i < preparationLength; i++){
            this.preparation.add(p.readString());
        }
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getUploaderUID() {
        return uploaderUID;
    }

    public void setUploaderUID(String uploaderUID) {
        this.uploaderUID = uploaderUID;
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
            this.uploaderUID,
            this.uploaderName});

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

