package com.example.cookit;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.cookit.Adapters.RecipeCardAdapter;
import com.example.cookit.Model.GetUserListener;
import com.example.cookit.Model.Model;
import com.example.cookit.Model.RecipeAsyncDaoListener;
import com.google.firebase.database.DatabaseReference;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.PopupMenu;

public class FeedActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private RecipeCardAdapter recipeCardAdapter;
    private static boolean  viewingRecipeDetails = false;
    private static boolean  uploadingRecipe = false;
    public static Bitmap    blurredImage;
    public static Bitmap    drawingCache;
    public static int       mainColor;
    public static View      appView;
    public static View      feedView;
    public static User      appUser;
    public static Model     modelInstance;
    public static String    UID;
    private String   feedUID = null;
    @Override
    public void onBackPressed() {
        if (feedUID == null) {
            moveTaskToBack(true);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        modelInstance = Model.getInstance();
        UID = getIntent().getExtras().get("UID").toString();

        // Observing the recipes - adding new ones and removing deleted ones.
        modelInstance.getAllUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(@Nullable List<User> recipes) {

            }});

        getUserFromServer();

        setContentView(R.layout.activity_feed);
        initRecipesRecyclerView();
        setToolbar();

        if (getIntent().getExtras().get("feedUID") != null) {
            feedUID = getIntent().getExtras().get("feedUID").toString();
            ((Toolbar)findViewById(R.id.feed_toolbar)).setTitle("");
            Model.getInstance().getUserByUID(feedUID, new GetUserListener(){

                @Override
                public void onSuccess(User user) {
                    ((Toolbar)findViewById(R.id.feed_toolbar)).setTitle(user.getFullName());
                }
            });
        }

        appView = findViewById(R.id.main_container);
        feedView = appView.findViewById(R.id.recipesRecyclerView);
        appView.setDrawingCacheEnabled(true);
        feedView.setDrawingCacheEnabled(true);


        // Observing the recipes - adding new ones and removing deleted ones.
        modelInstance.getAllRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                updateFeedWithChangedData(recipes);
            }});
    }

    private void getUserFromServer() {
        modelInstance.getUserByUID(Model.getInstance().getCurrentUserID(),new GetUserListener() {
            @Override
            public void onSuccess(User myUser) {
                    appUser = myUser;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewingRecipeDetails = false;
        uploadingRecipe = false;
        appView.findViewById(R.id.uploadRecipeButton).setClickable(true);
        getUserFromServer();
    }

    private void updateFeedWithChangedData(@Nullable List<Recipe> recipes) {
        boolean isChanged = false;
        boolean isRValid = true;
        if (recipes.size() >= 0) {
           for(Recipe r: recipes)
            {
                if (feedUID != null){
                    if (!r.getUploaderUID().equals(feedUID)){
                        isRValid = false;
                    }
                }
                if (!recipeCardAdapter.recipes.containsKey(r.getId()) && isRValid) {
                    recipeCardAdapter.recipeIds.add(0,r.getId());
                    recipeCardAdapter.recipes.put(r.getId(),r);
                    isChanged = true;
                }
                if ((r.hashCode() != ((Recipe)recipeCardAdapter.recipes.get(r.getId())).hashCode()) && isRValid) {
                    recipeCardAdapter.recipeIds.set(recipeCardAdapter.recipeIds.indexOf((Object)r.getId()),r.getId());
                    recipeCardAdapter.recipes.put(r.getId(),r);
                    isChanged = true;
                }

                isRValid = true;
            }

            Collection<Recipe> recipeCollection = recipeCardAdapter.recipes.values();
            Iterator<Recipe> recipeIterator = recipeCollection.iterator();

            while (recipeIterator.hasNext())
            {
                Recipe nextRecipe = recipeIterator.next();
                if (!recipes.contains(nextRecipe)){
                    recipeIterator.remove();
                    recipeCardAdapter.recipeIds.remove(nextRecipe.getId());
                    recipeCardAdapter.notifyDataSetChanged();
                    isChanged = true;
                }
            }

            if (isChanged) {
                recipeCardAdapter.notifyDataSetChanged();
            }
        }
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.feed_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(CookIt.getContext(), v);
                popup.inflate(R.menu.feed_activity_menu);
                popup.show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.menu_item_log_off:
                                modelInstance.signOut();
                                Intent loginIntent = new Intent(getBaseContext(), LoginActivity.class);
                                loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                finish();
                                startActivity(loginIntent);
                                break;
                            case R.id.menu_item_edit_profile:
                                final Intent editProfileIntent = new Intent(getBaseContext(), EditProfileActivity.class);
                                startActivity(editProfileIntent);
                            case R.id.menu_item_my_recipes:
                                final Intent myRecipesIntent = new Intent(getBaseContext(), FeedActivity.class);
                                myRecipesIntent.putExtra("feedUID", Model.getInstance().getCurrentUserID());
                                myRecipesIntent.putExtra("UID", UID);
                                startActivity(myRecipesIntent);
                        }
                        return false;
                    }
                });
            }
        });
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

            // Used for blurring background in upload recipe activity. removed it for now
            appView.destroyDrawingCache();
            appView.buildDrawingCache();
            drawingCache = appView.getDrawingCache();

            feedView.destroyDrawingCache();
            feedView.buildDrawingCache();
            //Palette pal = Palette.from(feedView.getDrawingCache()).generate();
            //mainColor = pal.getVibrantColor(0xffffffff);

           /* blurBitmap(new RecipeAsyncDaoListener<Bitmap>() {
                @Override
                public void onComplete(Bitmap data) {
                    blurredImage = data;
                    startActivity(intent);
                }
            });*/
            startActivity(intent);
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

                    // Lightening the image
                    blurredImage = ImageHelper.filterBitmap(drawingCache, 0xffffffff, 0x00999999);

                    // Blurring the image
                    //blurredImage = ImageHelper.fastblur(blurredImage,0.1f,22);
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