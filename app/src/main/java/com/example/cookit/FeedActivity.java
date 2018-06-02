package com.example.cookit;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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
        RecipeDetailsFragment frag = new RecipeDetailsFragment();
        FragmentTransaction tran = getFragmentManager().beginTransaction();
        tran.add(R.id.fragmentt,frag);
        tran.commit();

    }

    public void uploadRecipe(View view) {
        Intent intent = new Intent(this, UploadRecipeActivity.class);
        startActivity(intent);
    }
}
