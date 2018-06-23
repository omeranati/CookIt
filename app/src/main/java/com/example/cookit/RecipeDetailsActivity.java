package com.example.cookit;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class RecipeDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        this.getIntent().getExtras().get("recipeID");
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Bundle b = new Bundle();
        b.putString("recipeID", (String)this.getIntent().getExtras().get("recipeID"));
        RecipeDetailsFragment newRecipeDetailsFragment = new RecipeDetailsFragment();
        newRecipeDetailsFragment.setArguments(b);
        fragmentTransaction.replace(R.id.activity_recipe_details,newRecipeDetailsFragment);
        fragmentTransaction.commit();
    }
}
