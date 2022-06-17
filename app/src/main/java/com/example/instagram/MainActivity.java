package com.example.instagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.instagram.databinding.ActivityMainBinding;
import com.example.instagram.databinding.ToolBarBinding;
import com.example.instagram.fragments.HomeFragment;
import com.example.instagram.fragments.PostFragment;
import com.example.instagram.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.parse.ParseUser;


public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    private ActivityMainBinding binding;

    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

//        btnLogout = ToolBarBinding.inflate(getLayoutInflater()).btnLogout;

        final FragmentManager fragmentManager = getSupportFragmentManager();
        final Fragment homeFragment = new HomeFragment();
        final Fragment postFragment = new PostFragment();
        final Fragment profileFragment = new ProfileFragment();

        // Initial fragment
        replaceFragment(fragmentManager, homeFragment);
        bottomNavigation = binding.bottomNavigation;
        bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.actionHome:
                        replaceFragment(fragmentManager, homeFragment);
                        break;

                    case R.id.actionPost:
                        replaceFragment(fragmentManager, postFragment);
                        break;

                    case R.id.actionProfile:
                        replaceFragment(fragmentManager, profileFragment);
                        break;

                }
                return true;
            }
        });
    }

    private void replaceFragment(FragmentManager fragmentManager, Fragment fragment) {
        fragmentManager.beginTransaction()
                .replace(binding.flContainer.getId(), fragment)
                .commit();
    }

}