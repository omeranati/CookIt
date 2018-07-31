package com.example.cookit.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.cookit.IngredientFragment;
import com.example.cookit.PreparationFragment;
import com.example.cookit.Recipe;

import java.util.ArrayList;

public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private final ArrayList<Fragment> mFragmentList = new ArrayList<>();
    public SimpleFragmentPagerAdapter(Context context, FragmentManager fm, Recipe recipe) {
        super(fm);
        mContext = context;
        Bundle b = new Bundle();
        b.putParcelable("recipe",recipe);
        IngredientFragment ingredientFragment = new IngredientFragment();
        ingredientFragment.setArguments(b);
        PreparationFragment preparationFragment = new PreparationFragment();
        preparationFragment.setArguments(b);
        mFragmentList.add(ingredientFragment);
        mFragmentList.add(preparationFragment);
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return 2;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return "Ingredients";
            case 1:
                return "Preparation";
            default:
                return null;
        }
    }

}
