package com.example.instagram.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instagram.R;
import com.example.instagram.databinding.ItemPostBinding;
import com.example.instagram.models.Like;
import com.example.instagram.models.Post;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private static final String TAG = "ADAPTER";
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

        private ImageButton btnLike;
        private ImageButton btnComment;

        public ViewHolder(@NonNull ItemPostBinding binding) {
            super(binding.getRoot());
            tvPostUsername = binding.tvPostUsername;
            tvPostDescription = binding.ivPostDescription;
            tvLikes = binding.tvLikes;
            ivPostImage = binding.ivPostImage;
            ivPostProfileImage = binding.ivPostProfileImage;

            btnLike = binding.btnLike;
            btnComment = binding.btnComment;
        }

        public void bind(Post post) {
            tvPostUsername.setText(post.getUser().getUsername());
            tvPostDescription.setText(post.getDescription());
            tvLikes.setText(
                    context.getResources()
                            .getString(R.string.likes_label,
                                    String.valueOf(post.getLikes())));
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

            btnComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ParseUser currentUser = ParseUser.getCurrentUser();
                    JSONArray postsLiked = currentUser.getJSONArray("postsLiked");
                }
            });
        }
    }

    public void clear(){
        posts.clear();
        notifyDataSetChanged();
    }

}
