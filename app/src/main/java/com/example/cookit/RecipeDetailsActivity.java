package com.example.cookit;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class RecipeDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        RecipeDetailsFragment newRecipeDetailsFragment = new RecipeDetailsFragment();
        newRecipeDetailsFragment.setArguments(this.getIntent().getExtras().getBundle("recipe"));
        fragmentTransaction.replace(R.id.activity_recipe_details,newRecipeDetailsFragment);
        fragmentTransaction.commit();
    }
}