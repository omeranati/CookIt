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
}
