package com.example.cookit.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Vibrator;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cookit.FeedActivity;
import com.example.cookit.ImageHelper;
import com.example.cookit.Model.Model;
import com.example.cookit.Model.RecipeAsyncDaoListener;
import com.example.cookit.R;
import com.example.cookit.Recipe;
import com.example.cookit.SquareImageView;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class RecipeCardAdapter extends RecyclerView.Adapter<RecipeCardAdapter.ViewHolder> {

    public Hashtable<String,Recipe> recipes = new Hashtable<>();
    public List<String> recipeIds = new ArrayList<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recipe_card, parent, false);
        RecipeCardAdapter.ViewHolder vh = new RecipeCardAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecipeCardAdapter.ViewHolder holder, int position) {
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

        Bitmap food;

        SquareImageView imageView = holder.itemView.findViewById(R.id.recipePicture);

        if (position == 0) {
            food = BitmapFactory.decodeResource(holder.itemView.getContext().getResources(), R.drawable.ham);
        }
        else {
            food = BitmapFactory.decodeResource(holder.itemView.getContext().getResources(), R.drawable.a4590);
        }

        // Shrinking the photo so the scroll will be more fluent.
        float scale = food.getWidth() / 500;
        if (scale > 1) {
            int width = (int) Math.round(food.getWidth() * (float)(1 / scale));
            int height = (int) Math.round(food.getHeight() * (float)(1 / scale));
            food = Bitmap.createScaledBitmap(food, width, height, false);
        }

        imageView.setImageBitmap(food);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((FeedActivity)holder.itemView.getContext()).viewRecipeDetails(view);
            }
        });

        ImageView ownerProfilePicture = holder.itemView.findViewById(R.id.ownerProfilePicture);
        Bitmap omerProfilePicture = BitmapFactory.decodeResource(holder.itemView.getContext().getResources(),R.drawable.omer);

        // Extracting dark vibrant color from food picture and coloring the food name.
        Palette pal = Palette.from(food).generate();
        holder.foodName.setTextColor(pal.getDarkVibrantColor(0x00000000));

        omerProfilePicture = ImageHelper.getRoundedCornerBitmap(omerProfilePicture, omerProfilePicture.getHeight()/2);
        ownerProfilePicture.setImageBitmap(omerProfilePicture);
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
}