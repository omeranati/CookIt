package com.example.cookit;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.cookit.Model.Model;
import com.example.cookit.Model.WithFailMessageListener;

import java.io.File;
import java.io.IOException;

public class SignUpActivity extends AppCompatActivity {

    private static final int CAMERA_DIALOG_INDEX = 0;
    private static final int GALLERY_DIALOG_INDEX = 1;
    private String photoPath;
    private boolean wasPhotoUploaded = false;
    private byte[] imageData;
    private String emailAddress;
    private String fullName;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setProgressBarVisibility(View.INVISIBLE);
        Utils.setStatusbar(this);
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
                                requestPermissions();
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

    public void signUp(final View view) {
        if(validateInput()) {
            setProgressBarVisibility(View.VISIBLE);
            Model.getInstance().signUp(
                    emailAddress,
                    password,
                    fullName,
                    imageData,
                    new WithFailMessageListener() {
                        @Override
                        public void onSuccess() {
                            final Intent intent = new Intent(getBaseContext(), FeedActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("UID", Model.getInstance().getCurrentUserID());
                            startActivity(intent);
                            getCallingActivity();
                        }

                        @Override
                        public void onFail(String failMessage) {
                            setProgressBarVisibility(View.INVISIBLE);
                            Utils.showDynamicErrorAlert(failMessage, SignUpActivity.this);
                        }
                    }
            );
        }
    }

    private void setProgressBarVisibility(int visibility) {
        findViewById(R.id.progressBar2).setVisibility(visibility);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        ImageView imageView = findViewById(R.id.uploadUserImageButton);
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
                        Uri imageUri = imageReturnedIntent.getData();

                        // Get and resize profile image
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(imageUri, filePathColumn, null, null, null);
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String picturePath = cursor.getString(columnIndex);
                        cursor.close();

                        imageView.setImageBitmap(Utils.rotateImageIfNeeded(MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri), picturePath));
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
                "User",  /* prefix */
                ".jpg",         /* suffix */
                getExternalFilesDir(Environment.DIRECTORY_PICTURES)      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        photoPath = image.getAbsolutePath();
        return image;
    }

    private boolean validateInput() {
        emailAddress = ((EditText) findViewById(R.id.emailAddress)).getText().toString();
        fullName = ((EditText) findViewById(R.id.fullName)).getText().toString();
        password = ((EditText) findViewById(R.id.password)).getText().toString();

        if (emailAddress.length() == 0) {
            Utils.showErrorAlert(R.string.empty_email_error_message, this);
            return false;
        }

        if (fullName.length() == 0) {
            Utils.showErrorAlert(R.string.empty_name_error_message, this);
            return false;
        }

        if (password.length() == 0) {
            Utils.showErrorAlert(R.string.empty_password_error_message, this);
            return false;
        }

        if (!wasPhotoUploaded) {
            Utils.showErrorAlert(R.string.no_photo_error_message, this);
            return false;
        }

        return true;
    }

    private void requestPermissions() {
        if (!(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},3);
        }
    }
}
