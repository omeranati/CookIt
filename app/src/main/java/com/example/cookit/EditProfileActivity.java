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
import android.widget.ImageView;

import com.example.cookit.Model.GenericListener;
import com.example.cookit.Model.Listener;
import com.example.cookit.Model.Model;

import java.io.File;
import java.io.IOException;

public class EditProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Utils.setStatusbar(this);

        Toolbar toolbar = findViewById(R.id.profile_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);

        ((EditText)findViewById(R.id.fullNameEditText)).setText(FeedActivity.appUser.getFullName());

        Utils.putPicture(FeedActivity.appUser.getUserID(), this,null, new GenericListener<Bitmap>() {
            @Override
            public void onComplete(Bitmap data) {
                ((ImageView)findViewById(R.id.editUserImage)).setImageBitmap(data);
            }
        });
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
                Model.getInstance().updateUser(((EditText)findViewById(R.id.fullNameEditText)).getText().toString(),
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
}
