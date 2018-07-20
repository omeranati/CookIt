package com.example.cookit;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.cookit.Adapters.IngredientAdapter;
import com.example.cookit.Adapters.PreparationAdapter;
import com.example.cookit.Model.Model;

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
        ((NestedScrollView)findViewById(R.id.scrollLayout)).setNestedScrollingEnabled(false);
        Toolbar toolbar = findViewById(R.id.upload_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);

        /* Used for blurring the backround. removed it for now.
        Bitmap bitmap = FeedActivity.blurredImage;
        Drawable d = new BitmapDrawable(getResources(), bitmap);
        findViewById(R.id.uploadRecipeLayout).setBackground(d);*/

        initIngredientsRecyclerView();
        initPreparationRecyclerView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.upload_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_send:
                if (validateInput()) {
                    Model.getInstance().addRecipe(inputRecipe);
                    finish();
                }
                break;
        }

        return false;
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
        SquareImageView imageView = findViewById(R.id.uploadImageButton);
        switch(requestCode) {
            case CAMERA_DIALOG_INDEX:
                if(resultCode == RESULT_OK){
                    imageView.setImageBitmap((Bitmap) imageReturnedIntent.getExtras().get("data"));
                }
                break;
            case GALLERY_DIALOG_INDEX:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    imageView.setImageURI(selectedImage);
                }
                break;
        }
        imageView.requestLayout();
        imageView.invalidate();
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

    private void initPreparationRecyclerView() {
        prepareStagesAdapter = new PreparationAdapter();
        RecyclerView preparationRV = ((RecyclerView) findViewById(R.id.preparationRecyclerView));
        preparationRV.setLayoutManager(new LinearLayoutManager(this));
        preparationRV.setAdapter(prepareStagesAdapter);
        addNewPrepareStage(null);
    }

    private void initIngredientsRecyclerView() {
        ingredientsAdapter = new IngredientAdapter();
        RecyclerView ingredientsRV = ((RecyclerView) findViewById(R.id.ingredientsRecyclerView));
        ingredientsRV.setLayoutManager(new LinearLayoutManager(this));
        ingredientsRV.setAdapter(ingredientsAdapter);
        addNewIngredient(null);
    }

    private boolean validateInput() {
        setInputRecipe();

        if (inputRecipe.getName().length() == 0) {
            ((TextInputLayout)findViewById(R.id.nameTextView)).setError("Enter recipe name");
            return false;
        }

        if (inputRecipe.getIngredients().size() == 0) {
            showEmptyListAlert(R.string.empty_ingredients_error_massage);
            return false;
        }

        if (inputRecipe.getPreparation().size() == 0) {
            showEmptyListAlert(R.string.empty_preparation_error_message);
            return false;
        }

        return true;
    }

    private void showEmptyListAlert(int messageId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(messageId).setPositiveButton("Ok", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void setInputRecipe() {
        inputRecipe = new Recipe();
        inputRecipe.setName(((TextInputLayout)findViewById(R.id.nameTextView)).getEditText().getText().toString());
        getInputIngredients();
        getInputPreparation();

        inputRecipe.setUploaderName("Shirlilol");
        inputRecipe.setUploaderEmail("Shirlilol@gmail.com");
        inputRecipe.setPicture("NotAnPicture.jpg");
    }

    private void getInputPreparation() {
        for (String currStage : prepareStagesAdapter.prepareStages) {
            if (!currStage.isEmpty())
                inputRecipe.getPreparation().add(currStage);
        }
    }

    private void getInputIngredients() {
        String q, d;
        for (int i=0; i < ingredientsAdapter.quantities.size(); i++) {
            q = ingredientsAdapter.quantities.get(i);
            d = ingredientsAdapter.descriptions.get(i);

            // Make sure that this is not an empty line
            if (!q.isEmpty() || !d.isEmpty())
                inputRecipe.getIngredients().add(new Ingredient(q, d));

        }
    }
}
