package com.example.cookit.Model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.cookit.Ingredient;
import com.example.cookit.Recipe;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ModelFirebase {
    DatabaseReference recipesReference;

    public ModelFirebase() {
        recipesReference = FirebaseDatabase.getInstance().getReference().child("recipes");
    }

    public void getAllRecipes(final FirebaseChildEventListener listener) {

        recipesReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                listener.onChildAdded(getRecipeFromDataSnapshot(dataSnapshot));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

   // public void cancelGetAllRecipes() {
    //    recipesReference.removeEventListener(eventListener);
    //}

    public void addRecipe(Recipe r) {
        recipesReference.push().setValue(r);
    }

    private Recipe getRecipeFromDataSnapshot(DataSnapshot recipeSnapshot) {
        Recipe recipe = new Recipe();
        recipe.setId(recipeSnapshot.getKey());
        recipe.setName(recipeSnapshot.child("name").getValue().toString());
        recipe.setPicture(recipeSnapshot.child("picture").getValue().toString());
        recipe.setUploaderEmail(recipeSnapshot.child("uploaderEmail").getValue().toString());
        recipe.setUploaderName(recipeSnapshot.child("uploaderName").getValue().toString());
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
}