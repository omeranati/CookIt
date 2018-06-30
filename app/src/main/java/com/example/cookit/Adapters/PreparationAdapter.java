package com.example.cookit.Adapters;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
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
            holder.serialNumTextView.setText(String.valueOf(position + 1));
            holder.prepEditText.setText(prepareStages.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return prepareStages.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final TextView serialNumTextView;
        final EditText prepEditText;

        public ViewHolder(View itemView) {
            super(itemView);
            serialNumTextView = itemView.findViewById(R.id.serialNunTextView);
            prepEditText = itemView.findViewById(R.id.preparationEditText);

            prepEditText.addTextChangedListener(new TextWatcher() {

                @Override
                public void afterTextChanged(Editable s) {}

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                @Override
                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {
                    prepareStages.set(getLayoutPosition(),prepEditText.getText().toString());
                }
            });
        }
    }
}