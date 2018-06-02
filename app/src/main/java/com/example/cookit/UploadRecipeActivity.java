package com.example.cookit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.cookit.Model.IngredientAdapter;
import com.example.cookit.Model.PreparationAdapter;

public class UploadRecipeActivity extends AppCompatActivity {
    private IngredientAdapter ingredientsAdapter;
    private PreparationAdapter prepareStagesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_recipe);

        initIngredientsRecyclerView();
        initPreparationRecyclerView();
    }

    public void addNewIngredient(View view) {
        ingredientsAdapter.quantities.add("");
        ingredientsAdapter.descriptions.add("");
        ingredientsAdapter.notifyDataSetChanged();
    }

    public void addNewPrepareStage(View view) {
        prepareStagesAdapter.prepareStages.add("");
        prepareStagesAdapter.notifyDataSetChanged();
    }

    private void initPreparationRecyclerView() {
        prepareStagesAdapter = new PreparationAdapter();
        RecyclerView preparationRV = ((RecyclerView) findViewById(R.id.preparationRecyclerView));
        preparationRV.setLayoutManager(new LinearLayoutManager(this));
        preparationRV.setAdapter(prepareStagesAdapter);
        prepareStagesAdapter.prepareStages.add("");
    }

    private void initIngredientsRecyclerView() {
        ingredientsAdapter = new IngredientAdapter();
        RecyclerView ingredientsRV = ((RecyclerView) findViewById(R.id.ingredientsRecyclerView));
        ingredientsRV.setLayoutManager(new LinearLayoutManager(this));
        ingredientsRV.setAdapter(ingredientsAdapter);
        addNewIngredient(null);
    }
}
