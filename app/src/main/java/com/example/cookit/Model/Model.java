package com.example.cookit.Model;
import com.example.cookit.Recipe;

import java.util.List;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.view.View;

public class Model {
    private static Model instance = new Model();
    private ModelFirebase modelFirebase;
    private RecipesLiveData recipesLiveData = new RecipesLiveData();

    private Model() {
        modelFirebase = new ModelFirebase();
    }

    public String getCurrentUserID() {
        return modelFirebase.getCurrentUser().getUid();
    }


    public static Model getInstance() {
        return instance;
    }

    public RecipesLiveData getAllRecipes(){
        return recipesLiveData;
    }

    public void deleteRecipe(final Recipe recipe) {
        RecipeAsyncDao.deleteRecipeById(recipe.getId(), new Listener() {
            @Override
            public void onSuccess() {
                modelFirebase.deleteRecipe(recipe, new Listener(){

                    @Override
                    public void onSuccess() {
                        List<Recipe> allRecipes = recipesLiveData.getValue();
                        for (Recipe r:allRecipes){
                            if (r.getId().equals(recipe.getId())){
                                allRecipes.remove(r);
                                break;
                            }
                        }

                        recipesLiveData.postValue(allRecipes);
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

    public void cancelGetAllRecipes() {
        modelFirebase.cancelGetAllRecipes();
    }

    public void addRecipe(Recipe r, byte[] imageByteData) {modelFirebase.addRecipe(r, imageByteData);}

    public void signUp(String email, String password, final Listener listener){modelFirebase.signUp(email,password,listener);}
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

                    modelFirebase.getAllRecipes(new GetAllRecipesListener() {
                        @Override
                        public void onSuccess(List<Recipe> recipeslist) {
                            setValue(recipeslist);

                            RecipeAsyncDao.insertAll(recipeslist, new RecipeAsyncDaoListener<Boolean>() {
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
}
