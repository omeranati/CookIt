package com.example.cookit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.cookit.Model.IngredientAdapter;

public class UploadRecipeActivity extends AppCompatActivity {
    private IngredientAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_recipe);

        adapter = new IngredientAdapter();

        RecyclerView rv = ((RecyclerView) findViewById(R.id.my_recycler_view));
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
        addNewIngredient(null);
    }

    public void addNewIngredient(View view) {
        adapter.quantities.add("");
        adapter.descriptions.add("");
        adapter.notifyDataSetChanged();
    }
}
