package com.example.cookit.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cookit.FeedActivity;
import com.example.cookit.ImageHelper;
import com.example.cookit.Model.AppLocalDb;
import com.example.cookit.Model.GetImageListener;
import com.example.cookit.Model.Model;
import com.example.cookit.Model.RecipeAsyncDaoListener;
import com.example.cookit.R;
import com.example.cookit.Recipe;
import com.example.cookit.CustomImageView;
import com.example.cookit.User;
import com.example.cookit.Utils;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class RecipeCardAdapter extends RecyclerView.Adapter<RecipeCardAdapter.ViewHolder> {

    public Hashtable<String,Recipe> recipes = new Hashtable<>();
    public List<String> recipeIds = new ArrayList<>();
    private LruCache<String, Bitmap> mMemoryCache;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory * 3 / 4;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recipe_card, parent, false);
        RecipeCardAdapter.ViewHolder vh = new RecipeCardAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecipeCardAdapter.ViewHolder holder, int position) {
        final CustomImageView recipeImageView = holder.itemView.findViewById(R.id.recipePicture);
        recipeImageView.setImageBitmap(null);
        final Recipe recipe = recipes.get(recipeIds.get(position));

        Button deleteButton = ((Button)holder.itemView.findViewById(R.id.delete));

        // Showing the delete button if the post is mine.
        if (recipe.getUploaderUID().equals(Model.getInstance().getCurrentUserID()))
        {
            deleteButton.setVisibility(View.VISIBLE);
        }
        // Hiding it if it is not mine.
        else
        {
            deleteButton.setVisibility(View.INVISIBLE);
        }

        // Waiting dor the click.
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Model.getInstance().deleteRecipe(recipe);
            }
        });

        holder.ownerName.setText(recipe.getUploaderName());

        holder.foodName.setText(recipe.getName());
        holder.foodName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((FeedActivity)holder.itemView.getContext()).viewRecipeDetails(view);
            }
        });

        holder.itemView.findViewById(R.id.recipeCardLayout).setTag(recipe);
        holder.itemView.setTag(recipe);

        holder.itemView.findViewById(R.id.recipePicture).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Vibrator t = (Vibrator)(holder.itemView.getContext().getSystemService(Context.VIBRATOR_SERVICE));
                // Vibrate for 500 milliseconds
                if (t.hasVibrator())
                {
                    t.vibrate(500);
                }

                return true;
            }
        });
        final Bitmap bitmap = getBitmapFromMemCache(recipe.getId());
        if(bitmap == null) {
            putPicture(recipe.getId(), holder.itemView.getContext(), new RecipeAsyncDaoListener<Bitmap>() {
                @Override
                public void onComplete(Bitmap data) {
                    if (data != null) {
                        addBitmapToMemoryCache(recipe.getId(), data);
                    }
                    displayPicture(recipeImageView, data, 1);
                }
            });
        }
        else {
            displayPicture(recipeImageView, bitmap, 1);
        }

        recipeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((FeedActivity)holder.itemView.getContext()).viewRecipeDetails(view);
            }
        });

        final ImageView ownerProfilePicture = holder.itemView.findViewById(R.id.ownerProfilePicture);

       /* putPicture(recipe.getUploaderUID(), holder.itemView.getContext(), new RecipeAsyncDaoListener<Bitmap>() {
            @Override
            public void onComplete(Bitmap data) {
                displayPicture(ownerProfilePicture, data,0.1);
            }
        });*/
       // Bitmap omerProfilePicture = BitmapFactory.decodeResource(holder.itemView.getContext().getResources(),R.drawable.omer);

        /*Model.getInstance().getImage(recipe.getUploaderUID(), new GetImageListener() {
            @Override
            public void onDone(Bitmap imageBitmap) {
                if (imageBitmap != null) {
                /*    imageBitmap = Bitmap.createScaledBitmap(imageBitmap,(int)(imageBitmap.getWidth()*0.1),(int)(imageBitmap.getHeight()*0.1),false);
                    imageBitmap = ImageHelper.getRoundedCornerBitmap(imageBitmap, imageBitmap.getWidth()/2);
                    ownerProfilePicture.setImageBitmap(imageBitmap);
                }
                else{

                }
            }
        }, holder.itemView.getContext());*/

        // Extracting dark vibrant color from food picture and coloring the food name.
       // Palette pal = Palette.from(food).generate();
        //holder.foodName.setTextColor(pal.getDarkVibrantColor(0x00000000));

       // omerProfilePicture = ImageHelper.getRoundedCornerBitmap(omerProfilePicture, omerProfilePicture.getHeight()/2);
       // ownerProfilePicture.setImageBitmap(omerProfilePicture);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView owner, food;
        public TextView ownerName, foodName;

        public ViewHolder(View itemView) {
            super(itemView);
            owner = (ImageView) itemView.findViewById(R.id.ownerProfilePicture);
            food = (ImageView) itemView.findViewById(R.id.recipePicture);
            ownerName = (TextView) itemView.findViewById(R.id.ownerName);
            foodName = (TextView) itemView.findViewById(R.id.recipeName);
        }
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    static public void displayPicture(final ImageView recipeImageView, final Bitmap imageBitmap, final double scale) {

        class DisplayPictureAsyncTask extends AsyncTask<String, String, Bitmap> {
            @Override
            protected Bitmap doInBackground(String... strings) {
                Bitmap newImageBitmap = imageBitmap;
                if (scale < 1) {
                    newImageBitmap = Bitmap.createScaledBitmap(imageBitmap, (int) (imageBitmap.getWidth() * scale), (int) (imageBitmap.getHeight() * scale), false);
                }
                return newImageBitmap;
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                recipeImageView.setImageBitmap(result);
            }
        }

        DisplayPictureAsyncTask task = new DisplayPictureAsyncTask();
        task.execute();
    }
    static public void putPicture(final String imageName, final Context context, final RecipeAsyncDaoListener<Bitmap> listener) {
        class PutPictureAsyncTask extends AsyncTask<String, String, Recipe> {
            @Override
            protected Recipe doInBackground(String... strings) {
                Model.getInstance().getImage(imageName, new GetImageListener() {
                    @Override
                    public void onDone(Bitmap imageBitmap) {
                        if (imageBitmap != null) {

                            listener.onComplete(imageBitmap);
                        }
                    }
                }, context);

                return null;
            }
        }

        PutPictureAsyncTask task = new PutPictureAsyncTask();
        task.execute();
    }
}