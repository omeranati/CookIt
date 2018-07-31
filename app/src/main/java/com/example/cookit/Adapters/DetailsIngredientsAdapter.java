package com.example.cookit.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cookit.R;

import java.util.ArrayList;
import java.util.List;

public class DetailsIngredientsAdapter extends RecyclerView.Adapter<DetailsIngredientsAdapter.ViewHolder> {

    public List<String> quantities = new ArrayList<>();
    public List<String> descriptions = new ArrayList<>();

    @Override
    public DetailsIngredientsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.details_ingredients_list_item, parent, false);
        DetailsIngredientsAdapter.ViewHolder vh = new DetailsIngredientsAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(DetailsIngredientsAdapter.ViewHolder holder, int position) {
        holder.quantity.setText(quantities.get(position));
        holder.description.setText(descriptions.get(position));
        if (position == (quantities.size() - 1)){
            holder.itemView.findViewById(R.id.ingredientsDivider).setVisibility(View.INVISIBLE);
        }
        else{
            holder.itemView.findViewById(R.id.ingredientsDivider).setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return quantities.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final TextView description;
        final TextView quantity;

        public ViewHolder(View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.detailsIngredientDescription);
            quantity = itemView.findViewById(R.id.detailsIngredientQuantity);
        }
    }
}
