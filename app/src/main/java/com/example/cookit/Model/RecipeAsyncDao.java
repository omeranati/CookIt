package com.example.cookit.Model;

import com.example.cookit.Recipe;
import com.example.cookit.User;

import android.os.AsyncTask;

import java.util.List;

public class RecipeAsyncDao {

    static public void getAll(final GenericListener<List<Recipe>> listener) {

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

    static public void getAllUsers(final GenericListener<List<User>> listener) {

        class GetAllAsyncTask extends AsyncTask<String, String, List<User>> {
            @Override
            protected List<User> doInBackground(String... strings) {
                List<User> list = AppLocalDb.db.recipeDao().getAllUsers();
                return list;
            }

            @Override
            protected void onPostExecute(List<User> users) {
                super.onPostExecute(users);
                listener.onComplete(users);
            }
        }

        GetAllAsyncTask task = new GetAllAsyncTask();
        task.execute();
    }


    static public void insert(final Recipe recipe, final GenericListener<Boolean> listener) {
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

    static public void insertUser(final User user, final GenericListener<Boolean> listener) {
        class InsertAsyncTask extends AsyncTask<User, String, Boolean> {
            @Override
            protected Boolean doInBackground(User... users) {
                AppLocalDb.db.recipeDao().insertAll(users);
                return true;
            }

            @Override
            protected void onPostExecute(Boolean success) {
                super.onPostExecute(success);
                listener.onComplete(success);
            }
        }

        InsertAsyncTask task = new InsertAsyncTask();
        task.execute(user);
    }

    static void insertAll(final List<Recipe> recipes, final GenericListener<Boolean> listener) {
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

    static public void getUserByUID(final String UID, final GenericListener<User> listener) {

        class getUserByUIDAsyncTask extends AsyncTask<String, String, User> {
            @Override
            protected User doInBackground(String... strings) {
                User user = AppLocalDb.db.recipeDao().getUserByUID(UID);
                return user;
            }

            @Override
            protected void onPostExecute(User user) {
                super.onPostExecute(user);
                listener.onComplete(user);
            }
        }

        getUserByUIDAsyncTask task = new getUserByUIDAsyncTask();
        task.execute();
    }
}