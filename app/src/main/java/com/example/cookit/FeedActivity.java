package com.example.cookit;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.cookit.Model.Model;
import com.example.cookit.Model.RecipeAsyncDao;
import com.google.firebase.*;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class FeedActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
       // mDatabase = FirebaseDatabase.getInstance().getReference();
       // mDatabase.child("test").setValue("Hi omer");
        Model m = Model.getInstance();
        m.getAllRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> students) {
                /*myAdapter.notifyDataSetChanged();
                Log.d("TAG","notifyDataSetChanged" + students.size());*/
            }});
        List<Recipe> list = m.getAllRecipes().getValue();

        //       // Log.d("TAG",b.toString());
        FragmentManager fragmentManager = getSupportFragmentManager();

        List<Recipe> list2 = m.getAllRecipes().getValue();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        RecipeDetailsFragment newRecipeDetailsFragment = new RecipeDetailsFragment();
        fragmentTransaction.replace(R.id.main_container,newRecipeDetailsFragment);
        fragmentTransaction.addToBackStack("");
        fragmentTransaction.commit();

    }

    public void uploadRecipe(View view) {
        Intent intent = new Intent(this, UploadRecipeActivity.class);
        startActivity(intent);
    }
}
