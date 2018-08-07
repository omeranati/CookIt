package com.example.cookit.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Vibrator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.cookit.FeedActivity;
import com.example.cookit.Model.Model;
import com.example.cookit.Model.GenericListener;
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


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recipe_card, parent, false);
        RecipeCardAdapter.ViewHolder vh = new RecipeCardAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecipeCardAdapter.ViewHolder holder, int position) {
        final CustomImageView recipeImageView = holder.itemView.findViewById(R.id.recipePicture);
        final Recipe recipe = recipes.get(recipeIds.get(position));

        if (!recipe.getId().equals(((String)holder.foodName.getTag()))){
            recipeImageView.setImageBitmap(null);
        }

        holder.foodName.setTag(recipe.getId());

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


        holder.ownerName.setText(findOwnerName(recipe));
        holder.ownerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(),FeedActivity.class);
                intent.putExtra("UID", Model.getInstance().getCurrentUserID());
                intent.putExtra("feedUID", recipe.getUploaderUID());
                holder.itemView.getContext().startActivity(intent);
            }
        });
        holder.foodName.setText(recipe.getName());

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

        holder.itemView.findViewById(R.id.cardLayourProgressBar).setVisibility(View.VISIBLE);
        Utils.putPicture(recipe.getId(), holder.itemView.getContext(), new GenericListener<Bitmap>() {
            @Override
            public void onComplete(Bitmap data) {
                Utils.displayPicture(recipeImageView, data, 1, (ProgressBar)holder.itemView.findViewById(R.id.cardLayourProgressBar));
            }
        });


        recipeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((FeedActivity)holder.itemView.getContext()).viewRecipeDetails(view);
            }
        });

        final ImageView ownerProfilePicture = holder.itemView.findViewById(R.id.ownerProfilePicture);

       Utils.putPicture(recipe.getUploaderUID(), holder.itemView.getContext(), new GenericListener<Bitmap>() {
            @Override
            public void onComplete(Bitmap data) {
                Utils.displayPicture(ownerProfilePicture, data,0.1,null);
            }
        });

       ownerProfilePicture.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(holder.itemView.getContext(),FeedActivity.class);
               intent.putExtra("UID", Model.getInstance().getCurrentUserID());
               intent.putExtra("feedUID", recipe.getUploaderUID());
               holder.itemView.getContext().startActivity(intent);
           }
       });
    }

    private String findOwnerName(Recipe recipe) {
        String ownerName="";
        for (User currUser: FeedActivity.usersLiveData.getValue()) {
            if (currUser.getUserID().equals(recipe.getUploaderUID())) {
                ownerName = currUser.getFullName();
                break;
            }
        }
        return ownerName;
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