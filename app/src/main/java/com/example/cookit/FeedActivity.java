package com.example.cookit;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.*;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FeedActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
       // mDatabase = FirebaseDatabase.getInstance().getReference();
       // mDatabase.child("test").setValue("Hi omer");


       // Log.d("TAG",b.toString());
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        RecipeDetailsFragment newRecipeDetailsFragment = new RecipeDetailsFragment();
        fragmentTransaction.replace(R.id.main_container,newRecipeDetailsFragment);
        fragmentTransaction.addToBackStack("");
        fragmentTransaction.commit();

    }
}
