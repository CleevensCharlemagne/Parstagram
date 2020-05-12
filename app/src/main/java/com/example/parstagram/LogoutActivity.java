package com.example.parstagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseUser;

public class LogoutActivity extends AppCompatActivity {

    private Button btnLogout;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        btnLogout = findViewById(R.id.btnLogout);

        // Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.user);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.post:
                        goImageCaptureActivity();
                        return true;

                    case R.id.user:
                        return true;
                }
                return false;
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Log out any existing session
                ParseUser currentUser = ParseUser.getCurrentUser();
                if (currentUser != null) {
                    ParseUser.logOut();
                    Intent i = new Intent(LogoutActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                } else if(currentUser == null){
                    Toast.makeText(LogoutActivity.this, "already sign out", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void goPostActivity() {
        Intent i = new Intent(LogoutActivity.this, MainActivity.class);
        startActivity(i);
    }

    private void goImageCaptureActivity() {
        Intent i = new Intent(getApplicationContext(), imageCaptureActivity.class);
        startActivity(i);
    }
}
