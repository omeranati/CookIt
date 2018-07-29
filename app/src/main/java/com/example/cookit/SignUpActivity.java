package com.example.cookit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.cookit.Model.Listener;
import com.example.cookit.Model.Model;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

    }

    public void signUp(View view) {
        Model.getInstance().signUp(
                ((EditText) findViewById(R.id.emailAddress)).getText().toString(),
                ((EditText) findViewById(R.id.password)).getText().toString(),
                new Listener() {
                    @Override
                    public void onSuccess() {
                        final Intent intent= new Intent(getBaseContext(), FeedActivity.class);
                       // intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);

                    }

                    @Override
                    public void onFail() {

                    }
                });
    }
}
