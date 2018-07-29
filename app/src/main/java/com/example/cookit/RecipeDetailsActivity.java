package com.example.cookit;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.cookit.Model.Model;

public class RecipeDetailsActivity extends AppCompatActivity {

    Recipe recipe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        RecipeDetailsFragment newRecipeDetailsFragment = new RecipeDetailsFragment();
        Bundle recipeBundle = this.getIntent().getExtras().getBundle("recipe");
        recipe = recipeBundle.getParcelable("recipe");
        newRecipeDetailsFragment.setArguments(recipeBundle);
        fragmentTransaction.replace(R.id.activity_recipe_details,newRecipeDetailsFragment);
        fragmentTransaction.commit();
    }

    public void onDelete(View view){
        /*if (Model.getInstance().getCurrentUserID().equals(recipe.getUploaderEmail())){

        }*/
        Model.getInstance().deleteRecipe(recipe);
        this.finish();
    }
}