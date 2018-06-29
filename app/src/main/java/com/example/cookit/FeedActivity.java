package com.example.cookit;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.cookit.Adapters.RecipeCardAdapter;
import com.example.cookit.Model.Model;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        initRecipesRecyclerView();

        Toolbar toolbar = findViewById(R.id.feed_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        Model m = Model.getInstance();

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
        Intent intent = new Intent(this, UploadRecipeActivity.class);
        startActivity(intent);
    }

    public void viewRecipeDetails(View view) {
    if (!viewingRecipeDetails)
    {
        viewingRecipeDetails = true;

        while (view.getTag() == null)
        {
            view = (View)view.getParent();

        }

        Intent intent = new Intent(this, RecipeDetailsActivity.class);
        Bundle b = new Bundle();
        b.putParcelable("recipe", (Recipe)view.getTag());
        intent.putExtra("recipe",b);
        //view.getRootView().setDrawingCacheEnabled(true);
        ((View)view.getParent().getParent()).setDrawingCacheEnabled(true);
        Bitmap bitmap = ((View)view.getParent().getParent()).getDrawingCache();
        //((View)view.getParent().getParent()).setDrawingCacheEnabled(false);
        //((ImageView)view.findViewById(R.id.recipePicture)).setImageBitmap(ImageHelper.blur(this.getApplicationContext(),bitmap));
        /*view=(View)view.getParent().getParent();
        bitmap = ImageHelper.blur(this.getApplicationContext(),bitmap);
        bitmap = ImageHelper.blur(this.getApplicationContext(),bitmap);
        bitmap = ImageHelper.blur(this.getApplicationContext(),bitmap);
        ((ImageView)view.findViewById(R.id.blurImageView)).setImageBitmap(bitmap);*/
        view.setClickable(false);
        view.findViewById(R.id.recipeName).setClickable(false);

        startActivity(intent);

       /* view.findViewById(R.id.recipePicture).setClickable(true);
        view.findViewById(R.id.recipeName).setClickable(true);*/
    }
    }
}