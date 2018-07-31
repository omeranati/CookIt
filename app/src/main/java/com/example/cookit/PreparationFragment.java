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
import com.example.cookit.Adapters.DetailsPreparatoinAdapter;

public class PreparationFragment extends Fragment {

    private DetailsPreparatoinAdapter preparationDetailsAdapter;
    private Recipe recipe;

    public PreparationFragment() {
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
        final View view = inflater.inflate(R.layout.fragment_preparation, container, false);
        fillIngredientsAndPreparation(view);
        return view;
    }

    private void initPreparationRecyclerView(View view) {
        preparationDetailsAdapter = new DetailsPreparatoinAdapter();
        RecyclerView ingredientsRV = view.findViewById(R.id.preparationDetailsRecyclerView);
        ingredientsRV.setLayoutManager(new LinearLayoutManager(getContext()));
        ingredientsRV.setAdapter(preparationDetailsAdapter);
    }

    private void fillIngredientsAndPreparation(View view) {
        initPreparationRecyclerView(view);
        fillPreparationRecyclerView();
    }

    private void fillPreparationRecyclerView() {
        for(String currPrep : recipe.getPreparation()) {
            preparationDetailsAdapter.prepareStages.add(currPrep);
        }
    }
}
