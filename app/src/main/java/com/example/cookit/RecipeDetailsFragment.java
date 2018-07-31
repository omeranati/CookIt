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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.cookit.Model.AppLocalDb;
import com.example.cookit.Model.GetAllRecipesListener;
import com.example.cookit.Model.Model;
import com.example.cookit.Model.RecipeAsyncDao;
import com.example.cookit.Model.RecipeAsyncDaoListener;

import java.util.ArrayList;
import java.util.List;
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

    public void onDelete(View view){
        Model.getInstance().deleteRecipe(recipe);
        getActivity().finish();
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

        // Extracting main colors from food picture and coloring the background and the food's name
        /*Palette p = Palette.from(chickenBitmap).generate();
        view.findViewById(R.id.recipeDetailsLayout).setBackgroundColor(p.getLightVibrantColor(0xffffffff));
        ((TextView)view.findViewById(R.id.recipeName)).setTextColor(p.getDarkVibrantColor(0x00000000));
        */

        Button deleteButton = ((Button)view.findViewById(R.id.delete));

        if (recipe.getUploaderUID().equals(Model.getInstance().getCurrentUserID()))
        {
            deleteButton.setVisibility(View.VISIBLE);
        }
        else
        {
            deleteButton.setVisibility(View.INVISIBLE);
        }

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Model.getInstance().deleteRecipe(recipe);
                getActivity().finish();
            }
        });

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