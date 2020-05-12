package com.example.parstagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";
    EditText etUsername;
    EditText etPassword;
    Button btnLogin;
    Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(ParseUser.getCurrentUser() != null){
            goMainActivity();
        }

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignup = findViewById(R.id.btnSignup);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "On click login button");
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                loginUser(username, password);
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "On click login button");
                ParseUser user = new ParseUser();
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                signupUser(user, username, password);

            }
        });

    }

    private void signupUser(ParseUser user, String username, String password) {
        user.setUsername(username);
        user.setPassword(password);

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    Toast.makeText(LoginActivity.this, "Issue!", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Issue to sign up " + e);
                    return;
                }
                Log.i(TAG, "Signed up succesfully!");
                Toast.makeText(LoginActivity.this, "Signed up succesfully!", Toast.LENGTH_SHORT).show();
                etUsername.setText("");
                etPassword.setText("");
            }
        });
    }

    private void loginUser(String username, String password) {
        Log.i(TAG, "Attempting to login user " + username);
        
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e != null){
                    //TODO: Better error handling
                    Toast.makeText(LoginActivity.this, "Issue!", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Issue to log in " + e);
                    return;
                }
                //TODO: Navigate to the main activity if the user has signed in properly  
                goMainActivity();
                Toast.makeText(LoginActivity.this, "Success!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private void goImageCaptureActivity() {
        Intent i = new Intent(getApplicationContext(), imageCaptureActivity.class);
        startActivity(i);
    }
}
