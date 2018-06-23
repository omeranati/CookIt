package com.example.cookit.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.cookit.R;

import java.util.ArrayList;
import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {

    public List<String> quantities = new ArrayList<>();
    public List<String> descriptions = new ArrayList<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_list_item, parent, false);
        IngredientAdapter.ViewHolder vh = new IngredientAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(IngredientAdapter.ViewHolder holder, int position) {
        if (position >= quantities.size())
            return;
        holder.quantityEditText.setText(quantities.get(position), TextView.BufferType.EDITABLE);
        holder.descriptionEditText.setText(descriptions.get(position), TextView.BufferType.EDITABLE);
    }

    @Override
    public int getItemCount() {
        return quantities.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final EditText descriptionEditText;
        final EditText quantityEditText;

        public ViewHolder(View itemView) {
            super(itemView);
            quantityEditText = itemView.findViewById(R.id.quantityEditText);
            quantityEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    if(!hasFocus)
                        quantities.set(getLayoutPosition(),quantityEditText.getText().toString());
                }
            });

            descriptionEditText = itemView.findViewById(R.id.descriptionEditText);
            descriptionEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    if(!hasFocus)
                        descriptions.set(getLayoutPosition(),descriptionEditText.getText().toString());
                }
            });
        }
    }
}
