package com.example.cookit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.cookit.Model.Listener;
import com.example.cookit.Model.Model;
import com.example.cookit.Model.WithFailMessageListener;

public class LoginActivity extends AppCompatActivity {

    private String password;
    private String email;

    Model modelInstance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        modelInstance = Model.getInstance();

        Utils.setStatusbar(this);

        ((ProgressBar)findViewById(R.id.progressBar)).setVisibility(View.INVISIBLE);

        // The user is already logged in. Launching the feed activity.
        if (Model.getInstance().getCurrentUserID() != null){
            Intent intent= new Intent(getBaseContext(), FeedActivity.class);
            intent.putExtra("UID", modelInstance.getCurrentUserID());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK  |Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // The user is already logged in. Launching the feed activity.
        if (Model.getInstance().getCurrentUserID() != null){
            Intent intent= new Intent(getBaseContext(), FeedActivity.class);
            intent.putExtra("UID", modelInstance.getCurrentUserID());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK  |Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void startSignUpActivity(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }


    public void login(View view) {
        if (validateInput()) {
            ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.VISIBLE);
            modelInstance.login(
                    email,
                    password,
                    new WithFailMessageListener() {
                        @Override
                        public void onSuccess() {
                            Intent intent = new Intent(getBaseContext(), FeedActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("UID", modelInstance.getCurrentUserID());
                            ((EditText) findViewById(R.id.emailAddress)).setText("");
                            ((EditText) findViewById(R.id.password)).setText("");
                            startActivity(intent);
                        }

                        @Override
                        public void onFail(String errorMessage) {
                            Utils.showDynamicErrorAlert(errorMessage, LoginActivity.this);
                            ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.INVISIBLE);
                        }
                    });
        }
    }

    private boolean validateInput() {
        password = ((EditText) findViewById(R.id.password)).getText().toString();
        email = ((EditText) findViewById(R.id.emailAddress)).getText().toString();

        if (email.length() == 0) {
            Utils.showErrorAlert(R.string.empty_email_error_message, this);
            return false;
        } else if (password.length() == 0) {
            Utils.showErrorAlert(R.string.empty_password_error_message, this);
            return false;
        }

        return true;
    }
}


