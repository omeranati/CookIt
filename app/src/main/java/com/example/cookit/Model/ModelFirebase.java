package com.example.cookit.Model;

import com.example.cookit.Recipe;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.List;

public class ModelFirebase {

    ValueEventListener eventListener;

    public void cancelGetAllRecipes() {
        DatabaseReference stRef = FirebaseDatabase.getInstance().getReference().child("Recipes");
        stRef.removeEventListener(eventListener);
    }

    public void getAllRecipes(final GetAllRecipesListener listener) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("Recipes");

        eventListener = dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Recipe> list = new LinkedList<>();

                for (DataSnapshot stSnapshot: dataSnapshot.getChildren()) {
                    Recipe r = stSnapshot.getValue(Recipe.class);
                    list.add(r);
                }

                listener.onSuccess(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });

    }

}
