package com.example.cookit;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.cookit.Adapters.RecipeCardAdapter;
import com.example.cookit.Model.AppLocalDb;
import com.example.cookit.Model.Model;
import com.example.cookit.Model.RecipeAsyncDaoListener;
import com.google.firebase.database.DatabaseReference;
import java.util.List;
import java.util.concurrent.Semaphore;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

public class FeedActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private RecipeCardAdapter recipeCardAdapter;
    private static boolean viewingRecipeDetails = false;
    private static boolean uploadingRecipe = false;
    public static Bitmap   blurredImage;
    public static Bitmap   drawingCache;
    public static View     appView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        initRecipesRecyclerView();

        Toolbar toolbar = findViewById(R.id.feed_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        Model m = Model.getInstance();
        appView = findViewById(R.id.main_container);
        appView.setDrawingCacheEnabled(true);

        m.getAllRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> students) {
                updateFeedWithChangedData(students);
            }});
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewingRecipeDetails = false;
        uploadingRecipe = false;
        appView.findViewById(R.id.uploadRecipeButton).setClickable(true);
    }

    private void updateFeedWithChangedData(@Nullable List<Recipe> recipes) {
        if (recipes.size() > 0) {
           for(Recipe r: recipes)
            {
                if (!recipeCardAdapter.recipes.containsKey(r.getId())) {
                    recipeCardAdapter.recipeIds.add(0,r.getId());
                    recipeCardAdapter.recipes.put(r.getId(),r);
                    recipeCardAdapter.notifyDataSetChanged();
                }
                if (r.hashCode() != ((Recipe)recipeCardAdapter.recipes.get(r.getId())).hashCode()) {
                    recipeCardAdapter.recipeIds.set(recipeCardAdapter.recipeIds.indexOf((Object)r.getId()),r.getId());
                    recipeCardAdapter.recipes.put(r.getId(),r);
                    recipeCardAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void initRecipesRecyclerView() {
        recipeCardAdapter = new RecipeCardAdapter();
        RecyclerView recipeRV = ((RecyclerView) findViewById(R.id.recipesRecyclerView));
        recipeRV.setLayoutManager(new LinearLayoutManager(this));
        recipeRV.setAdapter(recipeCardAdapter);
    }

    public void uploadRecipe(View view) {

        // If not already uploading a recipe
        if(!uploadingRecipe) {
            view.setClickable(false);
            uploadingRecipe = true;

            final Intent intent = new Intent(this, UploadRecipeActivity.class);

            appView.destroyDrawingCache();
            appView.buildDrawingCache();
            drawingCache = appView.getDrawingCache();


            blurBitmap(new RecipeAsyncDaoListener<Bitmap>() {
                @Override
                public void onComplete(Bitmap data) {
                    blurredImage = data;
                    startActivity(intent);
                }
            });
        }
    }

    public void viewRecipeDetails(final View view) {
        if (!viewingRecipeDetails)
        {
            viewingRecipeDetails = true;
            View parentView = view;

            while (parentView.getTag() == null)
            {
                parentView = (View)parentView.getParent();

            }

            Intent intent = new Intent(this, RecipeDetailsActivity.class);
            Bundle b = new Bundle();
            b.putParcelable("recipe", (Recipe)parentView.getTag());
            intent.putExtra("recipe",b);
            view.setClickable(false);
            parentView.findViewById(R.id.recipeName).setClickable(false);
            startActivity(intent);
        }
    }

    static public void blurBitmap(final RecipeAsyncDaoListener<Bitmap> listener) {
        class drawBlur extends AsyncTask<String, String, Bitmap> {

            @Override
            protected Bitmap doInBackground(String... strings) {
                if (drawingCache != null) {

                    // Blurring the image
                    blurredImage = ImageHelper.fastblur(drawingCache,0.1f,60);

                    // Lightening the image
                    blurredImage = ImageHelper.lightenBitmap(blurredImage);
                }

                return blurredImage;
            }

            @Override
            protected void onPostExecute(Bitmap bitmapReturn) {
                super.onPostExecute(bitmapReturn);
                listener.onComplete(bitmapReturn);
            }
        }

        drawBlur task = new drawBlur();
        task.execute();
    }
}