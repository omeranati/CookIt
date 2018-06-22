package com.example.cookit;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.*;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
public class FeedActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private RecipeCardAdapter recipeCardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
       // mDatabase = FirebaseDatabase.getInstance().getReference();
       // mDatabase.child("test").setValue("Hi omer");

       // Log.d("TAG",b.toString());
        //FragmentManager fragmentManager = getSupportFragmentManager();
        //FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        RecipeDetailsFragment newRecipeDetailsFragment = new RecipeDetailsFragment();
        RecipeCardFragment newRecipeCardFragment = new RecipeCardFragment();
      //  fragmentTransaction.replace(R.id.feed_layout,newRecipeDetailsFragment);
       // fragmentTransaction.replace(R.id.feed_layout,newRecipeCardFragment);
        //fragmentTransaction.addToBackStack("");
         //fragmentTransaction.commit();
        initRecipesRecyclerView();

        User omerUser = new User("Omer Anati", "omer4554@gmail.com", "lala123");
        String[] ingredients = {"1kg Chicken", "2cups Canola oil", "1tbsp Salt"};
        String[] preparation = {"Defrost chicken", "Sprinkle salt", "Heat oil to 180°C", "Deep fry chicken until golden brown"};
        Recipe friedChickenRecipe = new Recipe("Fried Chicken", omerUser, "picture", ingredients, preparation);

        recipeCardAdapter.recipes.add(friedChickenRecipe);
        recipeCardAdapter.notifyDataSetChanged();
        recipeCardAdapter.recipes.add(friedChickenRecipe);
        recipeCardAdapter.notifyDataSetChanged();
        recipeCardAdapter.recipes.add(friedChickenRecipe);
        recipeCardAdapter.notifyDataSetChanged();
    }


    private void initRecipesRecyclerView() {
        recipeCardAdapter = new RecipeCardAdapter();
        RecyclerView recipeRV = ((RecyclerView)findViewById(R.id.recipesRecyclerView));
        recipeRV.setLayoutManager(new LinearLayoutManager(this));
        recipeRV.setAdapter(recipeCardAdapter);
        //recipeCardAdapter.recipes.add(new Recipe());
    }
}
