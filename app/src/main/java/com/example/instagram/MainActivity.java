package com.example.instagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.instagram.adapters.PostsAdapter;
import com.example.instagram.databinding.ActivityMainBinding;
import com.example.instagram.fragments.HomeFragment;
import com.example.instagram.fragments.PostFragment;
import com.example.instagram.models.Post;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    private ActivityMainBinding binding;

    private Button btnLogout;

    private BottomNavigationView bottomNavigation;

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

        final FragmentManager fragmentManager = getSupportFragmentManager();
        final Fragment homeFragment = new HomeFragment();
        final Fragment postFragment = new PostFragment();
//        final Fragment fragment3 = new ThirdFragment();

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
                        replaceFragment(fragmentManager, new Fragment());
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

    private void logoutUser() {
        ParseUser.logOutInBackground();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}