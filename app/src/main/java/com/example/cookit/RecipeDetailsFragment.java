package com.example.cookit;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Vibrator;
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

        Bundle b = getArguments();
        recipe = b.getParcelable("recipe");

        // Owner name
        TextView ownerName = view.findViewById(R.id.ownerName);
        ownerName.setText(recipe.getUploaderName());

        // Recipe name
        TextView recipeName = view.findViewById(R.id.recipeName);
        recipeName.setText(recipe.getName());
        recipeName.setClickable(false);

        // Recipe directions
        TextView recipeDirections = view.findViewById(R.id.recipeDirections);
        recipeDirections.setText(recipe.getPreperationString());

        // Displaying profile picture.
        ImageView ownerProfilePicture = view.findViewById(R.id.ownerProfilePicture);
        Bitmap omerProfilePicture = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.omer);
        omerProfilePicture = ImageHelper.getRoundedCornerBitmap(omerProfilePicture,omerProfilePicture.getHeight()/2);
        ownerProfilePicture.setImageBitmap(omerProfilePicture);

        // Displaying food picture.
        ImageView RecipePicture = view.findViewById(R.id.recipePicture);
        Bitmap chickenBitmap = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.ham);


        Bitmap blurredFoodPicture = ImageHelper.fastblur(chickenBitmap,0.1f,15);
        blurredFoodPicture = ImageHelper.filterBitmap(blurredFoodPicture, 0xfffffff, 0x00777777);
        Drawable d = new BitmapDrawable(getResources(), blurredFoodPicture);
        view.findViewById(R.id.recipeDetailsLayout).setBackground(d);


        RecipePicture.setClickable(false);
        RecipePicture.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                Vibrator t = (Vibrator)(getContext().getSystemService(Context.VIBRATOR_SERVICE));
                // Vibrate for 500 milliseconds
                if (t.hasVibrator())
                {
                    t.vibrate(500);
                }

                return true;
            }
        });

        
        RecipePicture.setImageBitmap(chickenBitmap);

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