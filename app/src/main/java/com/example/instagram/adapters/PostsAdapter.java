package com.example.instagram.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instagram.R;
import com.example.instagram.databinding.ItemPostBinding;
import com.example.instagram.models.Post;
import com.parse.ParseFile;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private Context context;
    private List<Post> posts;

    public PostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // This is how to use view binding in adapter
        ItemPostBinding binding = ItemPostBinding.inflate(LayoutInflater
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

    protected class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvPostUsername;
        private TextView tvPostDescription;
        private TextView tvLikes;
        private ImageView ivPostImage;
        private ImageView ivPostProfileImage;

        public ViewHolder(@NonNull ItemPostBinding binding) {
            super(binding.getRoot());
            tvPostUsername = binding.tvPostUsername;
            tvPostDescription = binding.ivPostDescription;
            tvLikes = binding.tvLikes;
            ivPostImage = binding.ivPostImage;
            ivPostProfileImage = binding.ivPostProfileImage;
        }

        public void bind(Post post) {
            tvPostUsername.setText(post.getUser().getUsername());
            tvPostDescription.setText(post.getDescription());
            tvLikes.setText(
                    context.getResources()
                            .getString(R.string.likes_label,
                                    String.valueOf(post.getLikes()) ));
            ParseFile image = post.getImage();
            if (image != null) {
                Glide.with(context)
                        .load(image.getUrl())
                        .into(ivPostImage);
            }
            ParseFile profileImage = post.getUser().getParseFile("profileImage");
            if (profileImage != null) {
                Glide.with(context)
                        .load(profileImage.getUrl())
                        .circleCrop()
                        .into(ivPostProfileImage);
            }

        }
    }

    public void clear(){
        posts.clear();
        notifyDataSetChanged();
    }
}
