package com.example.cookit;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class UploadRecipeActivity extends AppCompatActivity {
    private final int DESCRIPTION_ID_ADDITION = 300;

    private int ingredientsNum = 1;
    private int preparationNum = 1;

    private View.OnFocusChangeListener focusListener;
    private ConstraintLayout cl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_recipe);

        cl = (ConstraintLayout)findViewById(R.id.consLayout);
        focusListener = new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && ((EditText) findViewById(R.id.description1)).getText().length() != 0) {
                    addLine(v);
                }
            }
        };

        ((EditText)findViewById(R.id.description1)).setOnFocusChangeListener(focusListener);
    }

    public void addLine(View v) {
        ingredientsNum++;

        EditText quantityText = getQuantityEditText(v);

        cl.addView(quantityText);
        cl.addView(getDescriptionEditText(v, quantityText));
    }

    @NonNull
    private EditText getQuantityEditText(View v) {
        ConstraintLayout.LayoutParams p = new ConstraintLayout.LayoutParams(270, ViewGroup.LayoutParams.WRAP_CONTENT);
        p.topMargin = 6;
        p.topToBottom = v.getId();
        p.setMarginStart(48);
        p.startToStart = cl.getId();
        p.setMarginEnd(0);

        EditText quantityText = new EditText(this);
        quantityText.setLayoutParams(p);
        quantityText.setEms(10);
        quantityText.setHint(R.string.quantity);
        quantityText.setId(ingredientsNum);
        return quantityText;
    }

    @NonNull
    private EditText getDescriptionEditText(View v, EditText quantity) {
        ConstraintLayout.LayoutParams p = new ConstraintLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        p.setMarginStart(0);
        p.startToEnd = quantity.getId();
        p.setMarginEnd(0);
        p.endToEnd = cl.getId();
        p.baselineToBaseline = quantity.getId();

        EditText descriptionText = new EditText(this);
        descriptionText.setLayoutParams(p);
        descriptionText.setEms(10);
        descriptionText.setHint(R.string.description);
        descriptionText.setId(ingredientsNum + DESCRIPTION_ID_ADDITION);
        return descriptionText;
    }
}
