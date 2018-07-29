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

public class UploadIngredientAdapter extends RecyclerView.Adapter<UploadIngredientAdapter.ViewHolder> {

    public List<String> quantities = new ArrayList<>();
    public List<String> descriptions = new ArrayList<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.upload_ingredient_list_item, parent, false);
        UploadIngredientAdapter.ViewHolder vh = new UploadIngredientAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(UploadIngredientAdapter.ViewHolder holder, int position) {
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
            quantityEditText.addTextChangedListener(new TextWatcher() {

                @Override
                public void afterTextChanged(Editable s) {}

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                @Override
                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {
                    quantities.set(getLayoutPosition(),quantityEditText.getText().toString());
                }
            });


            descriptionEditText = itemView.findViewById(R.id.descriptionEditText);
            descriptionEditText.addTextChangedListener(new TextWatcher() {

                @Override
                public void afterTextChanged(Editable s) {}

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                @Override
                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {
                    descriptions.set(getLayoutPosition(),descriptionEditText.getText().toString());
                }
            });
        }
    }
}
