package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.instagram.databinding.ActivityMainBinding;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    private ActivityMainBinding binding;

    private Button btnLogout;
    private Button btnPostActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        btnLogout = binding.btnLogout;
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
        btnPostActivity = binding.btnPostActivity;
        btnPostActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goPostActivity();
            }
        });




    }

    private void goPostActivity() {
        Intent intent = new Intent(MainActivity.this, PostActivity.class);
        startActivity(intent);
    }

    private void logoutUser() {
        ParseUser.logOutInBackground();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}