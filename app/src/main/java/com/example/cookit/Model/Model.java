package com.example.cookit.Model;
import com.example.cookit.CookIt;
import com.example.cookit.Recipe;
import com.example.cookit.User;
import com.example.cookit.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.webkit.URLUtil;

public class Model {
    private static Model instance = new Model();
    private ModelFirebase modelFirebase;
    private RecipesLiveData recipesLiveData = new RecipesLiveData();
    private UsersLiveData usersLiveData = new UsersLiveData();

    private Model() {
        modelFirebase = new ModelFirebase();
    }

    public String getCurrentUserID() {
        return modelFirebase.getCurrentUserID();
    }

    public static Model getInstance() {
        return instance;
    }

    public RecipesLiveData getAllRecipes(){
        return recipesLiveData;
    }

    public UsersLiveData getAllUsers(){
        return usersLiveData;
    }

    public void getUserByUID(final String UID, final GetUserListener listener) {
        RecipeAsyncDao.getUserByUID(UID, new RecipeAsyncDaoListener<User>() {
            @Override
            public void onComplete(User data) {
                if (data != null){
                    listener.onSuccess(data);
                }
                else {
                    modelFirebase.getUserByUID(UID,listener);
                }
            }
        });
    }

    public void deleteRecipe(final Recipe recipe) {
        RecipeAsyncDao.deleteRecipeById(recipe.getId(), new Listener() {
            @Override
            public void onSuccess() {
                modelFirebase.deleteRecipe(recipe, new Listener(){

                    @Override
                    public void onSuccess() {
                        recipesLiveData.removeRecipe(recipe);
                    }

                    @Override
                    public void onFail() {

                    }
                });
            }

            @Override
            public void onFail() {

            }
        });
    }
  //  public void cancelGetAllRecipes() {
  //      modelFirebase.cancelGetAllRecipes();
   // }

    public void addRecipe(Recipe r, byte[] imageByteData, WithFailMessageListener listener) {modelFirebase.addRecipe(r, imageByteData, listener);}

    public void updateUser(boolean wasImageUpdated, String fullName, byte[] imageByteData, Listener listener) {modelFirebase.updateUser(wasImageUpdated, fullName, imageByteData, listener);}

    public void signOut() { modelFirebase.signOut(); }

    public void signUp(String email, String password, String fullName, byte[] imageData, final WithFailMessageListener listener){modelFirebase.signUp(email,password,fullName,imageData, listener);}

    public void login(String email, String password, final Listener listener){modelFirebase.login(email,password,listener);}

    public class RecipesLiveData extends MutableLiveData<List<Recipe>> {

        private RecipesLiveData(){
            this.onActive();
        }
        @Override
        protected void onActive() {
            super.onActive();

            RecipeAsyncDao.getAll(new RecipeAsyncDaoListener<List<Recipe>>() {

                @Override
                public void onComplete(List<Recipe> data) {
                    setValue(data);

                    modelFirebase.getAllRecipes(new FirebaseChildEventListener() {
                        @Override
                        public void onChildAdded(Recipe r) {
                            List<Recipe> data = getValue();
                            data.add(r);
                            setValue(data);

                            RecipeAsyncDao.insert(r, new RecipeAsyncDaoListener<Boolean>() {
                                @Override
                                public void onComplete(Boolean data) {

                                }
                            });
                        }

                        @Override
                        public void onChildRemoved(final Recipe r) {
                            RecipeAsyncDao.deleteRecipeById(r.getId(), new Listener() {
                                @Override
                                public void onSuccess() {
                                    removeRecipe(r);
                                }

                                @Override
                                public void onFail() {
                                    Utils.showDynamicErrorAlert("Failed removing the recipe from DB", CookIt.getContext());
                                }
                            });
                        }
                    });
                }
            });
        }

        public void removeRecipe(Recipe recipe) {
            List<Recipe> allRecipes = getValue();
            for (Recipe r:allRecipes){
                if (r.getId().equals(recipe.getId())){
                    allRecipes.remove(r);
                    break;
                }
            }
            postValue(allRecipes);
        }
    }

    public class UsersLiveData extends MutableLiveData<List<User>> {

        private UsersLiveData(){
            this.onActive();
        }
        @Override
        protected void onActive() {
            super.onActive();

            RecipeAsyncDao.getAllUsers(new RecipeAsyncDaoListener<List<User>>() {

                @Override
                public void onComplete(final List<User> data) {
                    setValue(data);

                    modelFirebase.getAllUsers(new RecipeAsyncDaoListener<User>() {
                        @Override
                        public void onComplete(User user) {
                            data.add(user);
                            setValue(data);

                            RecipeAsyncDao.insertUser(user, new RecipeAsyncDaoListener<Boolean>() {
                                @Override
                                public void onComplete(Boolean data) {

                                }
                            });
                        }
                    });
                }
            });
        }
    }

    private Bitmap loadImageFromFile(String imageFileName, Context context){
        Bitmap bitmap = null;

        try {
            File dir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File imageFile = new File(dir,imageFileName);
            InputStream inputStream = new FileInputStream(imageFile);
            bitmap = BitmapFactory.decodeStream(inputStream);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    public void getImage(final String recipeID, final GetImageListener listener , final Context context){
        final Bitmap image = loadImageFromFile(recipeID, context);

        // The image is not yet saved on the device
        if (image == null) {
            modelFirebase.getImage(recipeID, new GetImageListener() {
                @Override
                public void onDone(Bitmap imageBitmap) {
                    if (imageBitmap == null) {
                        listener.onDone(null);
                    }
                    else {
                        //2.  save the image localy
                        saveImageToFile(imageBitmap, recipeID, context);
                        //3. return the image using the listener
                        listener.onDone(imageBitmap);
                    }
                }
            });
        }
        else {
            listener.onDone(image);
        }
    }

    private void saveImageToFile(Bitmap imageBitmap, String imageFileName, Context context){
        if (imageBitmap == null) return;
        try {
            File directory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

            if (!directory.exists()) {
                directory.mkdir();
            }

            File imageFile = new File(directory,imageFileName);
            imageFile.createNewFile();

            OutputStream out = new FileOutputStream(imageFile);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
