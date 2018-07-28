package com.example.cookit;

import android.content.Context;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v4.app.DialogFragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cookit.Adapters.DetailsIngredientsAdapter;
import com.example.cookit.Adapters.DetailsPreparatoinAdapter;


public class RecipeDetailsFragment extends DialogFragment {

    private Recipe recipe;
    private DetailsPreparatoinAdapter preparationDetailsAdapter;
    private DetailsIngredientsAdapter ingredientsDetailsAdapter;

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

        fillIngredientsAndPreparation(view);

        // Displaying profile picture.
        ImageView ownerProfilePicture = view.findViewById(R.id.ownerProfilePicture);
        Bitmap omerProfilePicture = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.omer);
        omerProfilePicture = ImageHelper.getRoundedCornerBitmap(omerProfilePicture,omerProfilePicture.getHeight()/2);
        ownerProfilePicture.setImageBitmap(omerProfilePicture);

        // Displaying food picture.
        ImageView RecipePicture = view.findViewById(R.id.recipePicture);
        Bitmap chickenBitmap = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.ham);

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

        return view;
    }

    @Override
   public void onDestroyView(){
       super.onDestroyView();
   }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void fillIngredientsAndPreparation(View view) {
        initPreparationRecyclerView(view);
        fillPreparationRecyclerView();
        initIngredientsRecyclerView(view);
        fillIngredientsRecyclerView();
    }

    private void initPreparationRecyclerView(View view) {
        preparationDetailsAdapter = new DetailsPreparatoinAdapter();
        RecyclerView preparationRV = view.findViewById(R.id.preparationDetailsRecyclerView);
        preparationRV.setLayoutManager(new LinearLayoutManager(getContext()));
        preparationRV.setAdapter(preparationDetailsAdapter);
    }

    private void fillPreparationRecyclerView() {
        preparationDetailsAdapter.prepareStages.addAll(recipe.getPreparation());
    }

    private void initIngredientsRecyclerView(View view) {
        ingredientsDetailsAdapter = new DetailsIngredientsAdapter();
        RecyclerView ingredientsRV = view.findViewById(R.id.ingredientsDetailsRecyclerView);
        ingredientsRV.setLayoutManager(new LinearLayoutManager(getContext()));
        ingredientsRV.setAdapter(ingredientsDetailsAdapter);
    }

    private void fillIngredientsRecyclerView() {
        for(Ingredient currIngredient : recipe.getIngredients()) {
            ingredientsDetailsAdapter.quantities.add(currIngredient.getQuantity());
            ingredientsDetailsAdapter.descriptions.add(currIngredient.getDescription());
        }
    }
}