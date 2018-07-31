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

    static void insertAll(final List<Recipe> recipes, final RecipeAsyncDaoListener<Boolean> listener) {
        class InsertAllAsyncTask extends AsyncTask<List<Recipe>, String, Boolean> {
            @Override
            protected Boolean doInBackground(List<Recipe>... recipes) {
                for (Recipe r : recipes[0]) {
                    AppLocalDb.db.recipeDao().insertAll(r);
                }

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

    static public void deleteRecipeById(final String id, final Listener listener) {

        class deleteRecipeByIdAsyncTask extends AsyncTask<String, String, Recipe> {

            @Override
            protected Recipe doInBackground(String... strings) {
                AppLocalDb.db.recipeDao().deleteRecipeById(id);
                return null;
            }

            @Override
            protected void onPostExecute(Recipe result) {
                super.onPostExecute(result);
                listener.onSuccess();
            }
        }

        deleteRecipeByIdAsyncTask task = new deleteRecipeByIdAsyncTask();
        task.execute();
    }
}