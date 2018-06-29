package com.example.cookit.Model;

import android.util.Log;

import com.example.cookit.Ingredient;
import com.example.cookit.Recipe;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ModelFirebase {
    ValueEventListener eventListener;
    DatabaseReference recipesReference;

    public ModelFirebase() {
        recipesReference = FirebaseDatabase.getInstance().getReference().child("recipes");
    }

    public void getAllRecipes(final GetAllRecipesListener listener) {
        eventListener = recipesReference.addValueEventListener(new ValueEventListener() {
            @Override public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("TAG","onDataChange" );
                ArrayList<Recipe> recipeList = new ArrayList<>();

                for (DataSnapshot recipeSnapshot: dataSnapshot.getChildren()) {
                    recipeList.add(getRecipeFromDataSnapshot(recipeSnapshot));
                }

                listener.onSuccess(recipeList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }

    public void cancelGetAllRecipes() {
        recipesReference.removeEventListener(eventListener);
    }

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