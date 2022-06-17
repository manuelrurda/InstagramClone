package com.example.instagram.fragments;

import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.instagram.R;
import com.example.instagram.adapters.PostsAdapter;
import com.example.instagram.adapters.ProfileAdapter;
import com.example.instagram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private static final int INITIAL_POST_AMOUNT = 20;
    private static final String TAG = "ProfileFragment";
    private static final int GRID_COLUMNS = 3;

    private TextView tvProfileUsername;
    private ImageView ivProfileProfileImage;
    private TextView tvProfilePostAmount;

    private RecyclerView rvProfilePosts;
    private ProfileAdapter adapter;
    private List<Post> allUserPosts;

    private final ParseUser currentUser = ParseUser.getCurrentUser();

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvProfileUsername = view.findViewById(R.id.tvProfileUsername);
        tvProfileUsername.setText(currentUser.getUsername());

        ivProfileProfileImage = view.findViewById(R.id.ivProfileProfileImage);
        ParseFile profileImage = currentUser.getParseFile("profileImage");
        if (profileImage != null) {
            Glide.with(getContext())
                    .load(profileImage.getUrl())
                    .circleCrop()
                    .into(ivProfileProfileImage);
        }

        tvProfilePostAmount = view.findViewById(R.id.tvProfilePostAmount);
        setRecyclerView(view);
        queryPosts();
    }

    private void setRecyclerView(View view) {
        rvProfilePosts = view.findViewById(R.id.rvProfilePosts);
        allUserPosts = new ArrayList<>();
        adapter = new ProfileAdapter(getContext(), allUserPosts);
        rvProfilePosts.setAdapter(adapter);
        rvProfilePosts.setLayoutManager(new GridLayoutManager(getActivity(), GRID_COLUMNS));
    }

    protected void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.setLimit(INITIAL_POST_AMOUNT);
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error Posting: ", e);
                }
                allUserPosts.addAll(posts);
                adapter.notifyDataSetChanged();
                tvProfilePostAmount.setText(String.valueOf(allUserPosts.size()));
            }
        });
    }
}

