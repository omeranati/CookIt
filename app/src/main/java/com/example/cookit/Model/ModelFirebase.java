package com.example.cookit.Model;

import android.net.sip.SipSession;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.cookit.Ingredient;
import com.example.cookit.Recipe;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ModelFirebase {
    private ValueEventListener eventListener;
    private DatabaseReference recipesReference;
    private StorageReference storageRef;
    private FirebaseAuth authInstance;

    public ModelFirebase() {
        recipesReference = FirebaseDatabase.getInstance().getReference().child("recipes");
        storageRef = FirebaseStorage.getInstance().getReference();
        authInstance = FirebaseAuth.getInstance();
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

    public void addRecipe(Recipe r, byte[] imageByteData) {
        String recipeGeneratedKey = recipesReference.push().getKey();
        recipesReference.child(recipeGeneratedKey).setValue(r);

        storageRef.child(recipeGeneratedKey).putBytes(imageByteData);
    }

    private Recipe getRecipeFromDataSnapshot(DataSnapshot recipeSnapshot) {
        Recipe recipe = new Recipe();
        recipe.setId(recipeSnapshot.getKey());
        recipe.setName(recipeSnapshot.child("name").getValue().toString());
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

    public void deleteRecipe(Recipe recipe, final Listener listener){
        recipesReference.child(recipe.getId()).removeValue();
        listener.onSuccess();
    }

    public void signUp(String email, String password, final Listener listener) {
        authInstance.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    listener.onSuccess();
                } else {
                    listener.onFail();
                }
            }
        });
    }

    public void login(String email, String password, final Listener listener) {
        authInstance.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    listener.onSuccess();
                } else {
                    listener.onFail();
                }
            }
        });
    }

    public FirebaseUser getCurrentUser() {
        return authInstance.getCurrentUser();
    }
}