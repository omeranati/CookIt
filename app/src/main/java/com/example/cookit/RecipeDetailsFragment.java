package com.example.cookit;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
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
import com.example.cookit.Model.Model;
import com.example.cookit.Model.AppLocalDb;
import com.example.cookit.Model.RecipeAsyncDao;
import com.example.cookit.Model.RecipeAsyncDaoListener;

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

        if (Model.getInstance().getCurrentUserID().equals(recipe.getUploaderUID())) {
            setHasOptionsMenu(true);
            Toolbar toolbar = view.findViewById(R.id.recipe_details_toolbar);
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        }

        // Owner name
        /*TextView ownerName = view.findViewById(R.id.ownerName);
        ownerName.setText(recipe.getUploaderName());*/


        ((TextView)view.findViewById(R.id.recipeNameText)).setText(recipe.getName());

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Ingredients"));
        tabLayout.addTab(tabLayout.newTab().setText("Preparation"));
        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        viewPager.setNestedScrollingEnabled(true);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        SimpleFragmentPagerAdapter adapter =
                new SimpleFragmentPagerAdapter(getContext(), getActivity().getSupportFragmentManager(),recipe);
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);

        // Displaying profile picture.
        /*ImageView ownerProfilePicture = view.findViewById(R.id.ownerProfilePicture);
        Bitmap omerProfilePicture = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.omer);
        omerProfilePicture = ImageHelper.getRoundedCornerBitmap(omerProfilePicture,omerProfilePicture.getHeight()/2);
        ownerProfilePicture.setImageBitmap(omerProfilePicture);*/

        // Displaying food picture.
        final ImageView recipePicture = view.findViewById(R.id.recipePicture);

        Utils.putPicture(recipe.getId(), getContext(), new RecipeAsyncDaoListener<Bitmap>() {
            @Override
            public void onComplete(Bitmap data) {
                Utils.displayPicture(recipePicture,data,1);
            }
        });

        // Extracting main colors from food picture and coloring the background and the food's name
        /*Palette p = Palette.from(chickenBitmap).generate();
        view.findViewById(R.id.recipeDetailsLayout).setBackgroundColor(p.getLightVibrantColor(0xffffffff));
        ((TextView)view.findViewById(R.id.recipeName)).setTextColor(p.getDarkVibrantColor(0x00000000));
        */

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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_item_edit_recipe: {
                Intent intent = new Intent(getContext(), UploadRecipeActivity.class);
                Bundle b = new Bundle();
                b.putParcelable("recipeToEdit", recipe);
                intent.putExtra("recipeToEdit", b);
                startActivity(intent);

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