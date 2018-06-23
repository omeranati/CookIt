package com.example.cookit;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.cookit.Adapters.IngredientAdapter;
import com.example.cookit.Adapters.PreparationAdapter;

public class UploadRecipeActivity extends AppCompatActivity {
    private IngredientAdapter ingredientsAdapter;
    private PreparationAdapter prepareStagesAdapter;
    private Recipe inputRecipe;

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

    public void uploadRecipe(View view) {
        //DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        //mDatabase.child(getRecipeName()).setValue("First recipe send!!");
        getAndValidateInputRecipe();

    }

    private boolean getAndValidateInputRecipe() {
        inputRecipe = new Recipe();

        inputRecipe.setName(((TextInputLayout)findViewById(R.id.nameTextView)).getEditText().getText().toString());
        if (inputRecipe.getName().length() == 0) {
            ((TextInputLayout)findViewById(R.id.nameTextView)).setError("Enter recipe name");
            return false;
        }

        getInputIngredients();
        getInputPreparation();

        return true;
    }

    private boolean getInputPreparation() {
        for (String currStage : prepareStagesAdapter.prepareStages) {
            if (!currStage.isEmpty())
                inputRecipe.getPreparation().add(currStage);
        }
        return true;
    }

    private boolean getInputIngredients() {
        String q, d;
        for (int i=0; i < ingredientsAdapter.quantities.size(); i++) {
            q = ingredientsAdapter.quantities.get(i);
            d = ingredientsAdapter.descriptions.get(i);

            // Make sure that this is not an empty line
            if (!q.isEmpty() || !d.isEmpty())
                inputRecipe.getIngredients().add(new Ingredient(q, d));

        }

        return !inputRecipe.getIngredients().isEmpty();
    }
}
