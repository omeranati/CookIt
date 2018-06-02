package com.example.cookit.Model;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.cookit.R;

import java.util.ArrayList;
import java.util.List;

public class PreparationAdapter extends RecyclerView.Adapter<PreparationAdapter.ViewHolder> {

    public List<String> prepareStages = new ArrayList<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.preparation_list_item, parent, false);
        PreparationAdapter.ViewHolder vh = new PreparationAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(PreparationAdapter.ViewHolder holder, int position) {
        if (position < prepareStages.size()) {
            holder.serialNum.setText(String.valueOf(position + 1));
            holder.prep.setText(prepareStages.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return prepareStages.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final TextView serialNum;
        final EditText prep;

        public ViewHolder(View itemView) {
            super(itemView);
            serialNum = itemView.findViewById(R.id.serialNunTextView);
            prep = itemView.findViewById(R.id.preparationEditText);
        }
    }
}
