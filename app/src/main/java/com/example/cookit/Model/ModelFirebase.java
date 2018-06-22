package com.example.cookit.Model;

import com.example.cookit.Ingredient;
import com.example.cookit.Recipe;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ModelFirebase {

    ValueEventListener eventListener;

    public void cancelGetAllRecipes() {
        DatabaseReference stRef = FirebaseDatabase.getInstance().getReference().child("Recipes");
        stRef.removeEventListener(eventListener);
    }

    public Recipe getRecipeFromDataSnapshot(DataSnapshot recipeSnapshot) {
        Recipe recipe = new Recipe();
        recipe.setId(recipeSnapshot.getKey());
        recipe.setName(recipeSnapshot.child("name").getValue().toString());
        recipe.setPicture(recipeSnapshot.child("picture").getValue().toString());
        recipe.setUploaderEmail(recipeSnapshot.child("uploaderEmail").getValue().toString());
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        ArrayList<String> preparation = new ArrayList<>();

        // Getting ingredients from database
        for (DataSnapshot ingredient: recipeSnapshot.child("ingredients").getChildren()) {
            ingredients.add(new Ingredient(ingredient.child("quantity").getValue().toString(),
                                           ingredient.child("description").getValue().toString()));
        }

        // Getting preparation stages database
        for (DataSnapshot stage: recipeSnapshot.child("preparation").getChildren()) {
            preparation.add(stage.getValue().toString());
        }

        recipe.setIngredients(ingredients);
        recipe.setPreparation(preparation);

        return (recipe);
    }

    public void getAllRecipes(final GetAllRecipesListener listener) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("recipes");


        eventListener = dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Recipe> recipeList = new ArrayList<>();

                for (DataSnapshot recipeSnapshot: dataSnapshot.getChildren()) {
                    recipeList.add(getRecipeFromDataSnapshot(recipeSnapshot));
                }

                listener.onSuccess(recipeList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });

    }

}
