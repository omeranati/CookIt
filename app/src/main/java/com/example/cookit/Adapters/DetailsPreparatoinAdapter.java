package com.example.cookit.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cookit.R;

import java.util.ArrayList;
import java.util.List;

public class DetailsPreparatoinAdapter extends RecyclerView.Adapter<DetailsPreparatoinAdapter.ViewHolder> {

    public List<String> prepareStages = new ArrayList<>();

    @Override
    public DetailsPreparatoinAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.details_preparation_list_item, parent, false);
        DetailsPreparatoinAdapter.ViewHolder vh = new DetailsPreparatoinAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.preparationStageNum.setText(String.valueOf(position + 1) + ".");
        holder.preparationDescription.setText(prepareStages.get(position));

    }

    @Override
    public int getItemCount() {
        return prepareStages.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final TextView preparationStageNum;
        final TextView preparationDescription;

        public ViewHolder(View itemView) {
            super(itemView);
            preparationStageNum = itemView.findViewById(R.id.detailsPreparationNum);
            preparationDescription = itemView.findViewById(R.id.detailsPreparationDesc);
        }
    }
}
