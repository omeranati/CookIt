package com.example.cookit;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.FileProvider;
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
import android.widget.ImageView;

import com.example.cookit.Adapters.UploadIngredientAdapter;
import com.example.cookit.Adapters.UploadPreparationAdapter;
import com.example.cookit.Model.GenericListener;
import com.example.cookit.Model.Listener;
import com.example.cookit.Model.Model;
import com.example.cookit.Model.WithFailMessageListener;
import com.google.android.gms.stats.internal.G;

import java.io.File;
import java.io.IOException;

public class UploadRecipeActivity extends AppCompatActivity {
    private static final int CAMERA_DIALOG_INDEX = 0;
    private static final int GALLERY_DIALOG_INDEX = 1;
    private UploadIngredientAdapter ingredientsAdapter;
    private UploadPreparationAdapter prepareStagesAdapter;
    private boolean isNewRecipe = true;
    private Recipe inputRecipe;
    private boolean wasPhotoUploaded = false;
    private byte[] imageData;
    private String photoPath;
    private String editedRecipeID = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_upload_recipe);
        ((NestedScrollView)findViewById(R.id.scrollLayout)).setNestedScrollingEnabled(false);
        findViewById(R.id.uploadRecipeImageButton).setDrawingCacheEnabled(true);

        Toolbar toolbar = findViewById(R.id.upload_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);

        initIngredientsRecyclerView();
        initPreparationRecyclerView();

        setProgressBarVisibility(View.INVISIBLE);

        Bitmap uploadImageBitmap = BitmapFactory.decodeResource(this.getResources(),R.drawable.add_photo);
        ((ImageView)findViewById(R.id.uploadRecipeImageButton)).setImageBitmap(uploadImageBitmap);

        fillDataInCaseOfRecipeEdit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.upload_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_send:
                if (validateInput()) {
                    setProgressBarVisibility(View.VISIBLE);
                    findViewById(R.id.action_send).setEnabled(false);
                    Model.getInstance().addRecipe(isNewRecipe, inputRecipe, imageData, new WithFailMessageListener() {
                        @Override
                        public void onSuccess() {
                            finish();
                        }

                        @Override
                        public void onFail(String message) {
                            setProgressBarVisibility(View.INVISIBLE);
                            findViewById(R.id.action_send).setEnabled(true);
                            Utils.showDynamicErrorAlert(message, UploadRecipeActivity.this);
                        }
                    });

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
                                startCameraActivity();
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
        CustomImageView imageView = findViewById(R.id.uploadRecipeImageButton);
        switch(requestCode) {
            case CAMERA_DIALOG_INDEX:
                if(resultCode == RESULT_OK){
                    Uri outputFileUri = Uri.fromFile(new File(photoPath));
                    imageView.setImageURI(outputFileUri);
                    wasPhotoUploaded = true;

                    try {
                        Bitmap newBitmap = Utils.rotateImageIfNeeded(((BitmapDrawable)imageView.getDrawable()).getBitmap(), photoPath);
                        imageView.setImageBitmap(newBitmap);
                    } catch (IOException e) {
                        Utils.showErrorAlert(R.string.image_rotate_error_message, this);
                        wasPhotoUploaded = false;
                    }
                }
                break;
            case GALLERY_DIALOG_INDEX:
                if(resultCode == RESULT_OK){
                    try {
                        imageView.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageReturnedIntent.getData()));
                        wasPhotoUploaded = true;
                    } catch (IOException e) {
                        Utils.showDynamicErrorAlert(e.getMessage(), this);
                    }
                }
                break;
        }
        if (wasPhotoUploaded) {
            imageData = Utils.getDataFromImageView(imageView);
            imageView.requestLayout();
            imageView.invalidate();
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

    private void initPreparationRecyclerView() {
        prepareStagesAdapter = new UploadPreparationAdapter();
        RecyclerView preparationRV = ((RecyclerView) findViewById(R.id.preparationRecyclerView));
        preparationRV.setLayoutManager(new LinearLayoutManager(this));
        preparationRV.setAdapter(prepareStagesAdapter);
        addNewPrepareStage(null);
    }

    private void initIngredientsRecyclerView() {
        ingredientsAdapter = new UploadIngredientAdapter();
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
            Utils.showErrorAlert(R.string.empty_ingredients_error_massage, this);
            return false;
        }

        if (inputRecipe.getPreparation().size() == 0) {
            Utils.showErrorAlert(R.string.empty_preparation_error_message, this);
            return false;
        }

        if (!wasPhotoUploaded && isNewRecipe) {
            Utils.showErrorAlert(R.string.no_photo_error_message, this);
            return false;
        }

        return true;
    }

    private void setInputRecipe() {
        inputRecipe = new Recipe();
        if (editedRecipeID != null){
            inputRecipe.setId(editedRecipeID);
        }

        inputRecipe.setName(((TextInputLayout)findViewById(R.id.nameTextView)).getEditText().getText().toString());
        getInputIngredients();
        getInputPreparation();

        inputRecipe.setUploaderUID(FeedActivity.appUser.getUserID());
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

    private void startCameraActivity() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Utils.showErrorAlert(R.string.cant_create_photo_file_error, this);
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.cookit.fileProvider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_DIALOG_INDEX);
            }
        }
    }

    private File createImageFile() throws IOException {
        File image = File.createTempFile(
                "Recipe",  /* prefix */
                ".jpg",         /* suffix */
                getExternalFilesDir(Environment.DIRECTORY_PICTURES)      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        photoPath = image.getAbsolutePath();
        return image;
    }

    private void fillDataInCaseOfRecipeEdit() {
        if (getIntent().hasExtra("recipeToEdit")){
            isNewRecipe = false;

            Bundle recipeBundle = this.getIntent().getExtras().getBundle("recipeToEdit");
            inputRecipe = recipeBundle.getParcelable("recipeToEdit");
            editedRecipeID = inputRecipe.getId();
            ingredientsAdapter.quantities.remove(0);
            ingredientsAdapter.descriptions.remove(0);

            for(Ingredient i:inputRecipe.getIngredients()){
                ingredientsAdapter.quantities.add(i.getQuantity());
                ingredientsAdapter.descriptions.add(i.getDescription());
            }
            ingredientsAdapter.notifyDataSetChanged();

            prepareStagesAdapter.prepareStages.remove(0);

            for(String s:inputRecipe.getPreparation()){
                prepareStagesAdapter.prepareStages.add(s);
            }

            ingredientsAdapter.notifyDataSetChanged();
            prepareStagesAdapter.notifyDataSetChanged();

            ((TextInputLayout)findViewById(R.id.nameTextView)).getEditText().setText(inputRecipe.getName());

            Utils.putPicture(inputRecipe.getId(), this, new GenericListener<Bitmap>() {
                @Override
                public void onComplete(Bitmap data) {
                    ((ImageView)findViewById(R.id.uploadRecipeImageButton)).setImageBitmap(data);
                    ((ImageView)findViewById(R.id.uploadRecipeImageButton)).setEnabled(false);
                }
            });
        }
    }

    private void setProgressBarVisibility(int visibility) {
        findViewById(R.id.progressBarRecipeUpload).setVisibility(visibility);
    }
}
