package com.example.cookit.Model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.cookit.Ingredient;
import com.example.cookit.Recipe;
import com.example.cookit.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ModelFirebase {
    private ValueEventListener recipeEventListener;
    private DatabaseReference recipesReference;
    private DatabaseReference usersReference;
    private StorageReference storageRef;
    private FirebaseAuth authInstance;

    public ModelFirebase() {
        recipesReference = FirebaseDatabase.getInstance().getReference().child("recipes");
        usersReference = FirebaseDatabase.getInstance().getReference().child("users");
        storageRef = FirebaseStorage.getInstance().getReference();
        authInstance = FirebaseAuth.getInstance();
    }

    public void getUserByUID(String UID, final GetUserListener listener) {
        Query query = usersReference.child(UID);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                listener.onSuccess(new User((String)snapshot.child("fullName").getValue(),
                        snapshot.getKey()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getAllRecipes(final GetAllRecipesListener listener) {
        recipeEventListener = recipesReference.addValueEventListener(new ValueEventListener() {
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
        recipesReference.removeEventListener(recipeEventListener);
    }

    public void addRecipe(Recipe r, byte[] imageByteData) {
        String recipeGeneratedKey;

        if (r.getId() == null) {
            recipeGeneratedKey = recipesReference.push().getKey();
        }
        else {
            recipeGeneratedKey = r.getId();
        }

        r.setId(null);

        recipesReference.child(recipeGeneratedKey).setValue(r);
        //storageRef.child(recipeGeneratedKey).putBytes(imageByteData);
    }

    public void addUser(User newUser) {
        String userKey = this.getCurrentUserID();
        usersReference.child(userKey).child("fullName").setValue(newUser.getFullName());
    }

    private Recipe getRecipeFromDataSnapshot(DataSnapshot recipeSnapshot) {
        Recipe recipe = new Recipe();
        recipe.setId(recipeSnapshot.getKey());
        recipe.setName(recipeSnapshot.child("name").getValue().toString());
        recipe.setUploaderUID(recipeSnapshot.child("uploaderUID").getValue().toString());
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

    public void signUp(final String emailAddress, final String password, final String fullName, final Listener listener) {
        authInstance.createUserWithEmailAndPassword(emailAddress, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    addUser(new User(fullName, getCurrentUserID()));
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

    // Returns the current UID. If no user is logged on - returns null.
    public String getCurrentUserID() {
        if (authInstance.getCurrentUser() != null){
            return authInstance.getCurrentUser().getUid();
        }

        return null;
    }

    public void signOut() {
        authInstance.signOut();
    }
}