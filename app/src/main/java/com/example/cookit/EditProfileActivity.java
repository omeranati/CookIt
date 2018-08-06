package com.example.cookit;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.cookit.Model.Listener;
import com.example.cookit.Model.Model;

import java.io.File;
import java.io.IOException;

public class EditProfileActivity extends AppCompatActivity {

    private static final int CAMERA_DIALOG_INDEX = 0;
    private static final int GALLERY_DIALOG_INDEX = 1;
    private String photoPath;
    byte[] imageData;
    boolean wasImageUpdated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Toolbar toolbar = findViewById(R.id.profile_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);

        ((EditText)findViewById(R.id.fullNameEditText)).setText(FeedActivity.appUser.getFullName());
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
                Model.getInstance().updateUser(wasImageUpdated,
                                               ((EditText)findViewById(R.id.fullNameEditText)).getText().toString(),
                                               imageData,
                                               new Listener() {

                                                   @Override
                                                   public void onSuccess() {
                                                       finish();
                                                   }

                                                   @Override
                                                   public void onFail() {

                                                   }
                                               });

                break;
        }

        return false;
    }

    public void editPhoto(View view) {
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
        CustomImageView imageView = findViewById(R.id.editUserImageButton);
        switch(requestCode) {
            case CAMERA_DIALOG_INDEX:
                if(resultCode == RESULT_OK){
                    Uri outputFileUri = Uri.fromFile(new File(photoPath));
                    imageView.setImageURI(outputFileUri);
                    wasImageUpdated = true;

                    try {
                        Bitmap newBitmap = Utils.rotateImageIfNeeded(((BitmapDrawable)imageView.getDrawable()).getBitmap(), photoPath);
                        imageView.setImageBitmap(newBitmap);
                    } catch (IOException e) {
                        Utils.showErrorAlert(R.string.image_rotate_error_message, this);
                        wasImageUpdated = false;
                    }
                }
                break;
            case GALLERY_DIALOG_INDEX:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    imageView.setImageURI(selectedImage);
                    wasImageUpdated = true;
                }
                break;
        }
        if (wasImageUpdated) {
            imageData = Utils.getDataFromImageView(imageView);
            imageView.requestLayout();
            imageView.invalidate();
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
                "NewUser",  /* prefix */
                ".jpg",         /* suffix */
                getExternalFilesDir(Environment.DIRECTORY_PICTURES)      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        photoPath = image.getAbsolutePath();
        return image;
    }
}
