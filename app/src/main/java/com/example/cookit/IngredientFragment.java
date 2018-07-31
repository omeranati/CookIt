package com.example.cookit;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cookit.Adapters.DetailsIngredientsAdapter;

public class IngredientFragment extends Fragment {

    private DetailsIngredientsAdapter ingredientsDetailsAdapter;
    private Recipe recipe;

    public IngredientFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getArguments();
        recipe = b.getParcelable("recipe");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_ingredient, container, false);
        fillIngredientsAndPreparation(view);
        return view;
    }

    private void initIngredientsRecyclerView(View view) {
        ingredientsDetailsAdapter = new DetailsIngredientsAdapter();
        RecyclerView ingredientsRV = view.findViewById(R.id.ingredientsDetailsRecyclerVieww);
        ingredientsRV.setLayoutManager(new LinearLayoutManager(getContext()));
        ingredientsRV.setAdapter(ingredientsDetailsAdapter);
    }

    private void fillIngredientsAndPreparation(View view) {
        initIngredientsRecyclerView(view);
        fillIngredientsRecyclerView();
    }

    private void fillIngredientsRecyclerView() {
        for(Ingredient currIngredient : recipe.getIngredients()) {
            ingredientsDetailsAdapter.quantities.add(currIngredient.getQuantity());
            ingredientsDetailsAdapter.descriptions.add(currIngredient.getDescription());
        }
    }
}
