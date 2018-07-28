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

public class UploadPreparationAdapter extends RecyclerView.Adapter<UploadPreparationAdapter.ViewHolder> {

    public List<String> prepareStages = new ArrayList<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.upload_preparation_list_item, parent, false);
        UploadPreparationAdapter.ViewHolder vh = new UploadPreparationAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(UploadPreparationAdapter.ViewHolder holder, int position) {
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
