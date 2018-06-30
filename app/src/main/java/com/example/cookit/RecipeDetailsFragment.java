package com.example.cookit;

import android.content.Context;
import android.net.Uri;
import android.support.v4.app.DialogFragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import com.example.cookit.Model.AppLocalDb;
import com.example.cookit.Model.GetAllRecipesListener;
import com.example.cookit.Model.RecipeAsyncDao;
import com.example.cookit.Model.RecipeAsyncDaoListener;

import java.util.ArrayList;
import java.util.List;


public class RecipeDetailsFragment extends DialogFragment {

    private Recipe recipe;

    /*private static final String NAME = "NAME";
    private static final String RECIPE_NAME = "RECIPE_NAME";
    private static final String OWNER_PROFILE_PIC = "OWNER_PROFILE_PIC";
    private static final String FOOD_PIC = "OWNER_PROFILE_PIC";
    private static final String INSTRUCTIONS = "INSTRUCTIONS";*/

    public static RecipeDetailsFragment newInstance() {
        RecipeDetailsFragment fragment = new RecipeDetailsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_recipe_details, container, false);

        RecipeAsyncDao.getRecipeById(new RecipeAsyncDaoListener<Recipe>() {

            @Override
            public void onComplete(Recipe data) {
                recipe = data;


                TextView ownerName = view.findViewById(R.id.ownerName);
                ownerName.setText(recipe.getUploaderName());

                // Recipe name
                TextView recipeName = view.findViewById(R.id.recipeName);
                recipeName.setText(recipe.getName());

                // Recipe directions
                TextView recipeDirections = view.findViewById(R.id.recipeDirections);
                recipeDirections.setText(createRecipeString(recipe));

            }
        }, (String)getArguments().get("recipeID"));

        // Displaying profile picture.
        ImageView ownerProfilePicture = view.findViewById(R.id.ownerProfilePicture);
        Bitmap omerProfilePicture = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.omer);
        omerProfilePicture = ImageHelper.getRoundedCornerBitmap(omerProfilePicture,omerProfilePicture.getHeight()/2);
        ownerProfilePicture.setImageBitmap(omerProfilePicture);

        // Displaying food picture.
        ImageView RecipePicture = view.findViewById(R.id.recipePicture);
        Bitmap chickenBitmap = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.chicken);

        if (chickenBitmap.getHeight() > chickenBitmap.getWidth()) {
            chickenBitmap = ImageHelper.getRoundedCornerBitmap(chickenBitmap, chickenBitmap.getHeight() / 35);
        }
        else {
            chickenBitmap = ImageHelper.getRoundedCornerBitmap(chickenBitmap, chickenBitmap.getWidth() / 35);
        }
        
        RecipePicture.setImageBitmap(chickenBitmap);

        /*
        // Used to bring back saved instance
        if (savedInstanceState != null) {
        }*/

        return view;
    }

    private String createRecipeString(Recipe recipe) {
        String recipePrint = "";
        recipePrint += "Ingredients:" + System.lineSeparator();

        for (int i = 0; i < recipe.getIngredients().size(); i++) {
            recipePrint += "• " + recipe.getIngredients().get(i).getQuantity() + " " + recipe.getIngredients().get(i).getDescription() + System.lineSeparator();
        }

        recipePrint += System.lineSeparator()+ "Preparation:" + System.lineSeparator();

        for (int i = 0; i < recipe.getPreparation().size(); i++) {
            recipePrint += i+1 + ". " + recipe.getPreparation().get(i) + System.lineSeparator();
        }

        return (recipePrint);
    }

    //@Override
   /* public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

   // @Override
    /*public void onDetach() {
        super.onDetach();
        mListener = null;
    }*/

   @Override
   public void onDestroyView(){
       super.onDestroyView();
   }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        /*outState.putString(NAME, this.recipe.getUploader().getFullName());
        outState.putString(RECIPE_NAME, this.recipe.getName());
        outState.putString(INSTRUCTIONS, this.recipe.toString());
        outState.putString(OWNER_PROFILE_PIC, "omer");
        outState.putString(FOOD_PIC,"chicken");*/
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}