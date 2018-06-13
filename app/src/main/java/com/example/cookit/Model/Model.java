package com.example.cookit.Model;
import com.example.cookit.Recipe;

import java.util.List;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

public class Model {
    private static Model instance = new Model();
    ModelFirebase modelFirebase;

    private Model() {
        modelFirebase = new ModelFirebase();
    }

    public static Model getInstance() {
        return instance;
    }

    public void cancelGetAllRecipes() {
        modelFirebase.cancelGetAllRecipes();
    }

    class RecipesLiveData extends MutableLiveData<List<Recipe>> {

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
