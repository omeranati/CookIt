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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ((ProgressBar)findViewById(R.id.progressBar)).setVisibility(View.INVISIBLE);
       // login(new View(this.getBaseContext()));
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Model.getInstance().getCurrentUserID() != null){
            final Intent intent= new Intent(getBaseContext(), FeedActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        ((EditText)findViewById(R.id.emailAddress)).requestFocus();
        ((EditText)findViewById(R.id.emailAddress)).setText("");
        ((EditText)findViewById(R.id.password)).setText("");
    }

    public void startSignUpActivity(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }


    public void login(View view) {
        ((ProgressBar)findViewById(R.id.progressBar)).setVisibility(View.VISIBLE);
        final String emailAddress = ((EditText) findViewById(R.id.emailAddress)).getText().toString();
        Model.getInstance().login(
                //emailAddress,
                //((EditText) findViewById(R.id.password)).getText().toString(),
                "omer4554@gmail.com", "Lala123",
                new Listener() {
                    @Override
                    public void onSuccess() {
                        final Intent intent= new Intent(getBaseContext(), FeedActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        intent.putExtra("emailAddress", emailAddress);
                        startActivity(intent);
                        ((EditText) findViewById(R.id.emailAddress)).setText("");
                        ((EditText) findViewById(R.id.password)).setText("");
                    }

                    @Override
                    public void onFail() {

                    }
                });
    }
}


