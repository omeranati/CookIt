package com.example.cookit;

import android.arch.persistence.room.TypeConverter;

import java.util.ArrayList;
import java.util.Arrays;

public class Converters {
    // Preperation converters
    @TypeConverter
    public ArrayList<String> storedStringToPreparation(String value) {
        ArrayList<String> prep = new ArrayList<>();
        prep.addAll(Arrays.asList(value.split(";")));
        return prep;
    }

    @TypeConverter
    public String preparationToStoredString(ArrayList<String> prep) {
        String value = "";

        for (String stage : prep)
            value += stage + ";";

        return value;
    }

    // Ingredients converters
    @TypeConverter
    public String IngredientsToStoredString(ArrayList<Ingredient> ingredients) {
        String value = "";

        for (Ingredient ingredient : ingredients)
            value += ingredient.getQuantity() + ";" + ingredient.getDescription() + ";";

        return value;
    }

    @TypeConverter
    public ArrayList<Ingredient> storedStringToIngredients(String value) {
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        boolean isFinishedCreatingIngredient = false;
        String quantity="";

        for (String s : Arrays.asList(value.split(";"))) {
            if(!isFinishedCreatingIngredient) {
                quantity = s;
                isFinishedCreatingIngredient = true;
            }
            else {
                ingredients.add(new Ingredient(quantity,s));
                isFinishedCreatingIngredient = false;
            }
        }

        return ingredients;
    }


}
