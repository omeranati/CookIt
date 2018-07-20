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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cookit.FeedActivity;
import com.example.cookit.ImageHelper;
import com.example.cookit.Model.RecipeAsyncDaoListener;
import com.example.cookit.R;
import com.example.cookit.Recipe;

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
        Recipe recipe = recipes.get(recipeIds.get(position));
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
        ImageView imageView = holder.itemView.findViewById(R.id.recipePicture);
        if (position == 0) {
            food = BitmapFactory.decodeResource(holder.itemView.getContext().getResources(), R.drawable.ham);
        }
        else {
            food = BitmapFactory.decodeResource(holder.itemView.getContext().getResources(), R.drawable.salad);

        }
        imageView.setImageBitmap(food);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((FeedActivity)holder.itemView.getContext()).viewRecipeDetails(view);
            }
        });

        ImageView imageView2 = holder.itemView.findViewById(R.id.ownerProfilePicture);
        Bitmap omerProfilePicture = BitmapFactory.decodeResource(holder.itemView.getContext().getResources(),R.drawable.omer);

        // Extracting dark vibrant color from food picture and coloring the food name.
        Palette pal = Palette.from(food).generate();
        holder.foodName.setTextColor(pal.getDarkVibrantColor(0x00000000));

        omerProfilePicture = ImageHelper.getRoundedCornerBitmap(omerProfilePicture, omerProfilePicture.getHeight()/2);
        imageView2.setImageBitmap(omerProfilePicture);
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