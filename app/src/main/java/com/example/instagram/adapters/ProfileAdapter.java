package com.example.instagram.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instagram.databinding.ItemProfileBinding;
import com.example.instagram.models.Post;
import com.parse.ParseFile;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder>{

    private static final String TAG = "ADAPTER";
    private Context context;
    private List<Post> posts;

    public ProfileAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProfileBinding binding = ItemProfileBinding.inflate(LayoutInflater
                .from(parent.getContext()), parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView ivProfilePost;

        public ViewHolder(@NonNull ItemProfileBinding binding) {
            super(binding.getRoot());
            ivProfilePost = binding.ivProfilePost;
        }

        public void bind(Post post) {
            ParseFile profilePost = post.getImage();
            if (profilePost != null) {
                Glide.with(context)
                        .load(profilePost.getUrl())
                        .into(ivProfilePost);
            }
        }
    }
}
