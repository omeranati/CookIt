package com.example.cookit;

import android.content.Intent;
import android.graphics.ColorSpace;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.cookit.Model.Listener;
import com.example.cookit.Model.Model;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ((ProgressBar)findViewById(R.id.progressBar2)).setVisibility(View.INVISIBLE);
    }

    public void signUp(View view) {
        ((ProgressBar)findViewById(R.id.progressBar2)).setVisibility(View.VISIBLE);
        final String emailAddress = ((EditText) findViewById(R.id.emailAddress)).getText().toString();
        final String fullName = ((EditText) findViewById(R.id.fullName)).getText().toString();
        Model.getInstance().signUp(
                emailAddress,
                ((EditText) findViewById(R.id.password)).getText().toString(),
                fullName,
                new Listener() {
                    @Override
                    public void onSuccess() {
                        final Intent intent= new Intent(getBaseContext(), FeedActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK  |Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("UID", Model.getInstance().getCurrentUserID());
                        startActivity(intent);
                        getCallingActivity();
                    }

                    @Override
                    public void onFail() {

                    }
                });
    }
}
