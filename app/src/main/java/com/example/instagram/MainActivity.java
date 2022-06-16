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
    private static final int INITIAL_POST_AMOUNT = 20;

    private ActivityMainBinding binding;

    private Button btnLogout;
    private Button btnPostActivity;
    private RecyclerView rvPosts;
    private SwipeRefreshLayout swipeRefresh;

    private BottomNavigationView bottomNavigation;

    private PostsAdapter adapter;
    private List<Post> allPosts;

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

        swipeRefresh = binding.swipeRefresh;
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {fetchPostsAsync();}
        });

        setRecyclerView();

        final FragmentManager fragmentManager = getSupportFragmentManager();
        final Fragment postFragment = new PostFragment();
//        final Fragment fragment2 = new SecondFragment();
//        final Fragment fragment3 = new ThirdFragment();

        bottomNavigation = binding.bottomNavigation;
        bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.actionHome:
                        replaceFragment(new Fragment());
                        break;

                    case R.id.actionPost:
                        replaceFragment(postFragment);
                        break;

                    case R.id.actionProfile:
                        replaceFragment(new Fragment());
                        break;
                }
                return true;
            }
           private void replaceFragment(Fragment fragment) {
               fragmentManager.beginTransaction()
                       .replace(binding.flContainer.getId(), fragment)
                       .commit();
           }
        });

        queryPosts();

    }

    private void fetchPostsAsync() {
        adapter.clear();
        queryPosts();
        swipeRefresh.setRefreshing(false);
    }

    private void setRecyclerView() {
        rvPosts = binding.rvPosts;
        allPosts = new ArrayList<>();
        adapter = new PostsAdapter(this, allPosts);
        rvPosts.setAdapter(adapter);
        rvPosts.setLayoutManager(new LinearLayoutManager(this));
    }hrdrfjuurr

    private void logoutUser() {
        ParseUser.logOutInBackground();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void queryPosts(){
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.setLimit(INITIAL_POST_AMOUNT);
        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error Posting: ", e);
                }
                allPosts.addAll(posts);
                adapter.notifyDataSetChanged();
            }
        });
    }
}