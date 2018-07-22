package com.example.cookit.Model;
import com.example.cookit.Recipe;

import java.util.List;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

public class Model {
    private static Model instance = new Model();
    private ModelFirebase modelFirebase;
    private RecipesLiveData recipesLiveData = new RecipesLiveData();

    private Model() {
        modelFirebase = new ModelFirebase();
    }

    public static Model getInstance() {
        return instance;
    }

    public RecipesLiveData getAllRecipes(){
        return recipesLiveData;
    }

    public void cancelGetAllRecipes() {
        modelFirebase.cancelGetAllRecipes();
    }

    public void addRecipe(Recipe r, byte[] imageByteData) {modelFirebase.addRecipe(r, imageByteData);}

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
