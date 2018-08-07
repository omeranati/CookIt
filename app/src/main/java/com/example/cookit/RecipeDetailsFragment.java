package com.example.cookit;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cookit.Adapters.SimpleFragmentPagerAdapter;
import com.example.cookit.Model.GenericListener;
import com.example.cookit.Model.Model;

import com.example.cookit.Adapters.DetailsIngredientsAdapter;
import com.example.cookit.Adapters.DetailsPreparatoinAdapter;


public class RecipeDetailsFragment extends DialogFragment {

    private Recipe recipe;
    private SimpleFragmentPagerAdapter ingredientsAndPrepAd;

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

        ((Toolbar)view.findViewById(R.id.recipe_details_toolbar)).setTitle("");
        ((Toolbar)view.findViewById(R.id.recipe_details_toolbar)).setOverflowIcon(
                ContextCompat.getDrawable(CookIt.getContext(), R.drawable.three_dots_icon));
        Bundle b = getArguments();
        recipe = b.getParcelable("recipe");

        if (Model.getInstance().getCurrentUserID().equals(recipe.getUploaderUID())) {
            setHasOptionsMenu(true);
            Toolbar toolbar = view.findViewById(R.id.recipe_details_toolbar);
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        }

        ((TextView)view.findViewById(R.id.recipeNameText)).setText(recipe.getName());

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Ingredients"));
        tabLayout.addTab(tabLayout.newTab().setText("Preparation"));
        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        viewPager.setNestedScrollingEnabled(true);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        ingredientsAndPrepAd =
                new SimpleFragmentPagerAdapter(getContext(), getActivity().getSupportFragmentManager(),recipe);
        viewPager.setAdapter(ingredientsAndPrepAd);

        tabLayout.setupWithViewPager(viewPager);

        // Displaying food picture.
        final ImageView recipePicture = view.findViewById(R.id.recipePicture);

        Utils.putPicture(recipe.getId(), getContext(), null, new GenericListener<Bitmap>() {
            @Override
            public void onComplete(Bitmap data) {
                Utils.displayPicture(recipePicture, data, 1,null);
            }
        });

        recipePicture.setClickable(false);
        recipePicture.setOnLongClickListener(new View.OnLongClickListener() {
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

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.recipe_details_menu, menu);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null && data.hasExtra("recipe")) {
            TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.tabLayout);
            recipe = (Recipe)((Bundle)data.getExtras().getParcelable("recipe")).get("recipe");
            ((TextView) getActivity().findViewById(R.id.recipeNameText)).setText(recipe.getName());
            ingredientsAndPrepAd.updateRecipe(recipe);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_item_edit_recipe: {
                Intent intent = new Intent(getContext(), UploadRecipeActivity.class);
                Bundle b = new Bundle();
                b.putParcelable("recipeToEdit", recipe);
                intent.putExtra("recipeToEdit", b);
                startActivityForResult(intent,1);

                return true;
            }
            case R.id.menu_item_delete_recipe: {
                Model.getInstance().deleteRecipe(recipe);
                getActivity().finish();

                return true;
            }
            default:
                break;
        }

        return false;
    }
}