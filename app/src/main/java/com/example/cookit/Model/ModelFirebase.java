package com.example.cookit.Model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.cookit.Ingredient;
import com.example.cookit.Recipe;
import com.google.firebase.database.ChildEventListener;
import com.example.cookit.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class ModelFirebase {
    DatabaseReference recipesReference;
    private final String USER_FULL_NAME_FIELD_NAME = "fullName";
    private ValueEventListener recipeEventListener;
    private DatabaseReference usersReference;
    private StorageReference storageReference;
    private FirebaseAuth authInstance;

    public ModelFirebase() {
        recipesReference = FirebaseDatabase.getInstance().getReference().child("recipes");
        usersReference = FirebaseDatabase.getInstance().getReference().child("users");
        storageReference = FirebaseStorage.getInstance().getReference();
        authInstance = FirebaseAuth.getInstance();
    }

    public void getUserByUID(String UID, final GetUserListener listener) {
        Query query = usersReference.child(UID);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                listener.onSuccess(new User((String)snapshot.child(USER_FULL_NAME_FIELD_NAME).getValue(),
                        snapshot.getKey()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void getAllRecipes(final FirebaseChildEventListener listener) {

        recipesReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                listener.onChildAdded(getRecipeFromDataSnapshot(dataSnapshot));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                listener.onChildRemoved(getRecipeFromDataSnapshot(dataSnapshot));
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getAllUsers(final RecipeAsyncDaoListener<User> listener){
        usersReference.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User user = getUserFromDataSnapshot(dataSnapshot);
                listener.onComplete(user);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User user = getUserFromDataSnapshot(dataSnapshot);
                listener.onComplete(user);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void addRecipe(Recipe r, byte[] imageByteData, final WithFailMessageListener listener) {
        String recipeGeneratedKey;

        if (r.getId().equals(Recipe.NO_UID)) {
            recipeGeneratedKey = recipesReference.push().getKey();
            r.setId(recipeGeneratedKey);

        }
        else {
            recipeGeneratedKey = r.getId();
        }

        recipesReference.child(recipeGeneratedKey).setValue(r);
        storageReference.child(recipeGeneratedKey).putBytes(imageByteData).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful())
                    listener.onSuccess();
                else
                    listener.onFail(task.getException().getMessage().toString());
            }
        });
    }

    public void addUser(User newUser) {
        usersReference.child(newUser.getUserID()).child(USER_FULL_NAME_FIELD_NAME).setValue(newUser.getFullName());
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

    private User getUserFromDataSnapshot(DataSnapshot recipeSnapshot) {
       User user = new User(recipeSnapshot.child("fullName").getValue().toString(),
               recipeSnapshot.getKey());


        return (user);
    }

    public void deleteRecipe(Recipe recipe, final Listener listener){
        recipesReference.child(recipe.getId()).removeValue();
        storageReference.child(recipe.getId()).delete();
        listener.onSuccess();
    }

    public void signUp(final String emailAddress, final String password, final String fullName, final byte[] imageByteData, final WithFailMessageListener listener) {
        authInstance.createUserWithEmailAndPassword(emailAddress, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String userID = getCurrentUserID();
                    addUser(new User(fullName, userID));
                    storageReference.child(userID).putBytes(imageByteData);
                    listener.onSuccess();
                } else {
                    listener.onFail(task.getException().getMessage());
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

    public void updateUser(boolean wasImageUpdated, String fullName, byte[] imageByteData, final Listener listener) {
        if (wasImageUpdated)
            storageReference.child(getCurrentUserID()).putBytes(imageByteData);

        usersReference.child(getCurrentUserID()).child(USER_FULL_NAME_FIELD_NAME).setValue(fullName).
                addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        listener.onSuccess();
                    }
                });
    }



   /* public void saveImage(Bitmap imageBitmap, final SaveImageListener listener) {
        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference imagesRef = storage.getReference().child("images").child(name);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                listener.onDone(null);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                listener.onDone(downloadUrl.toString());
            }
        });

    }*/


    public void getImage(String recipeID, final GetImageListener listener){
        /*FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference httpsReference = storage.getReferenceFromUrl(url);*/

        final long ONE_MEGABYTE = 1024 * 1024;
        storageReference.child(recipeID).getBytes(3 * ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap image = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                listener.onDone(image);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                listener.onDone(null);
            }
        });
    }
}