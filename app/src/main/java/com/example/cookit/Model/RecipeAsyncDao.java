package com.example.cookit.Model;

import com.example.cookit.Recipe;
import android.os.AsyncTask;

import java.util.List;

public class RecipeAsyncDao {

    static public void getAll(final RecipeAsyncDaoListener<List<Recipe>> listener) {

        class GetAllAsyncTask extends AsyncTask<String, String, List<Recipe>> {

            @Override
            protected List<Recipe> doInBackground(String... strings) {
                List<Recipe> list = AppLocalDb.db.recipeDao().getAll();
                return list;
            }

            @Override
            protected void onPostExecute(List<Recipe> recipes) {
                super.onPostExecute(recipes);
                listener.onComplete(recipes);
            }
        }

        GetAllAsyncTask task = new GetAllAsyncTask();
        task.execute();
    }

    static public void insert(final Recipe recipe, final RecipeAsyncDaoListener<Boolean> listener) {
        class InsertAsyncTask extends AsyncTask<Recipe, String, Boolean> {
            @Override
            protected Boolean doInBackground(Recipe... recipes) {
                AppLocalDb.db.recipeDao().insert(recipes[0]);
                return true;
            }

            @Override
            protected void onPostExecute(Boolean success) {
                super.onPostExecute(success);
                listener.onComplete(success);
            }
        }

        InsertAsyncTask task = new InsertAsyncTask();
        task.execute(recipe);
    }

    static void insertAll(final List<Recipe> recipes, final RecipeAsyncDaoListener<Boolean> listener) {

        class InsertAllAsyncTask extends AsyncTask<List<Recipe>, String, Boolean> {
            @Override
            protected Boolean doInBackground(List<Recipe>... recipes) {
                for (Recipe r : recipes[0]) {
                    AppLocalDb.db.recipeDao().insertAll(r);
                }
                // TODO: return false??

                return true;
            }

            @Override
            protected void onPostExecute(Boolean success) {
                super.onPostExecute(success);
                listener.onComplete(success);
            }
        }

        InsertAllAsyncTask task = new InsertAllAsyncTask();
        task.execute(recipes);
    }

    static public void getRecipeById(final RecipeAsyncDaoListener<Recipe> listener, final String id) {

        class getRecipeByIdAsyncTask extends AsyncTask<String, String, Recipe> {

            @Override
            protected Recipe doInBackground(String... strings) {
                Recipe recipe = AppLocalDb.db.recipeDao().getRecipeById(id);
                return recipe;
            }

            @Override
            protected void onPostExecute(Recipe recipe) {
                super.onPostExecute(recipe);
                listener.onComplete(recipe);
            }
        }

        getRecipeByIdAsyncTask task = new getRecipeByIdAsyncTask();
        task.execute();
    }
}
