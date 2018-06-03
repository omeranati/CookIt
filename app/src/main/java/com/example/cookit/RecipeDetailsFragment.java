package com.example.cookit;

import android.net.Uri;
import android.support.v4.app.DialogFragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class RecipeDetailsFragment extends DialogFragment {

    private Recipe recipe;
    /*private static final String NAME = "NAME";
    private static final String RECIPE_NAME = "RECIPE_NAME";
    private static final String OWNER_PROFILE_PIC = "OWNER_PROFILE_PIC";
    private static final String FOOD_PIC = "OWNER_PROFILE_PIC";
    private static final String INSTRUCTIONS = "INSTRUCTIONS";*/

    public RecipeDetailsFragment() {
    }

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
        View view = inflater.inflate(R.layout.fragment_recipe_details, container, false);

        // Creating User and Recipe for testing the fragment.
        User omerUser = new User("Omer Anati", "omer4554@gmail.com", "lala123");
        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>() {{
            add(new Ingredient("1 kg", "Chicken"));
            add(new Ingredient("2 cups", "Canola oil"));
            add(new Ingredient("1 tbsp", "Salt"));
        }};
        ArrayList<String> preparation = new ArrayList<String>() {{
            add("Defrost chicken");
            add("Sprinkle salt");
            add("Heat oil to 180°C");
            add("Deep fry chicken until golden brown");
        }};
        Recipe friedChickenRecipe = new Recipe("Fried Chicken", omerUser, "picture", ingredients, preparation);

        this.recipe = friedChickenRecipe;

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


        // Owner name
        TextView ownerName = view.findViewById(R.id.ownerName);
        ownerName.setText(omerUser.getFullName());

        // Recipe name
        TextView recipeName = view.findViewById(R.id.recipeName);
        recipeName.setText(friedChickenRecipe.getName());

        // Recipe directions
        TextView recipeDirections = view.findViewById(R.id.recipeDirections);
        recipeDirections.setText(friedChickenRecipe.getPreperationString());

        /*
        // Used to bring back saved instance
        if (savedInstanceState != null) {
        }*/

        return view;
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