package com.example.cookit;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class UploadRecipeActivity extends AppCompatActivity {
    private static final int CAMERA_DIALOG_INDEX = 0;
    private static final int GALLERY_DIALOG_INDEX = 1;
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

    public void addPhoto(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Photo Source")
                .setItems(R.array.photo_inputs, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case CAMERA_DIALOG_INDEX:
                                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(takePicture, CAMERA_DIALOG_INDEX);
                                break;
                            case GALLERY_DIALOG_INDEX:
                                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(pickPhoto , GALLERY_DIALOG_INDEX);
                                break;
                        }
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        ImageButton imageButton = findViewById(R.id.uploadImageButton);
        switch(requestCode) {
            case CAMERA_DIALOG_INDEX:
                if(resultCode == RESULT_OK){
                    imageButton.setImageBitmap((Bitmap) imageReturnedIntent.getExtras().get("data"));
                }

                break;
            case GALLERY_DIALOG_INDEX:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    imageButton.setImageURI(selectedImage);
                }
                break;
        }
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

    public void uploadRecipe(View view) {
        //DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        //mDatabase.child(getRecipeName()).setValue("First recipe send!!");
        getAndValidateInputRecipe();

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
