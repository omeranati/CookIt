package com.example.cookit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.cookit.Model.Listener;
import com.example.cookit.Model.Model;

public class LoginActivity extends AppCompatActivity {

    Model modelInstance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        modelInstance = Model.getInstance();

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
        ((ProgressBar)findViewById(R.id.progressBar)).setVisibility(View.VISIBLE);
        final String emailAddress = ((EditText) findViewById(R.id.emailAddress)).getText().toString();
        modelInstance.login(
                //emailAddress,
                //((EditText) findViewById(R.id.password)).getText().toString(),
                "omer4554@gmail.com", "Lala123",
                new Listener() {
                    @Override
                    public void onSuccess() {
                        Intent intent= new Intent(getBaseContext(), FeedActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK  |Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("UID", modelInstance.getCurrentUserID());
                        ((EditText) findViewById(R.id.emailAddress)).setText("");
                        ((EditText) findViewById(R.id.password)).setText("");
                        startActivity(intent);
                    }

                    @Override
                    public void onFail() {

                    }
                });
    }
}


