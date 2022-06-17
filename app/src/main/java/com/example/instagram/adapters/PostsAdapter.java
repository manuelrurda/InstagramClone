package com.example.instagram.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instagram.R;
import com.example.instagram.databinding.ItemPostBinding;
import com.example.instagram.models.Like;
import com.example.instagram.models.Post;
import com.parse.CountCallback;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Date;
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
        private TextView tvTimeStamp;
        private ImageView ivPostImage;
        private ImageView ivPostProfileImage;

        private ImageButton btnLike;
        private ImageButton btnComment;

        public ViewHolder(@NonNull ItemPostBinding binding) {
            super(binding.getRoot());
            tvPostUsername = binding.tvPostUsername;
            tvPostDescription = binding.ivPostDescription;
            tvLikes = binding.tvLikes;
            tvTimeStamp = binding.tvTimeStamp;
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

            tvTimeStamp.setText(calculateTimeAgo(post.getCreatedAt()));

            bindLikeAmount(post);
            bindLikeButton(post);
        }


        private void bindLikeAmount(Post post) {
            ParseQuery<Like> query = ParseQuery.getQuery(Like.class);
            query.whereEqualTo(Like.KEY_POST, post);
            query.countInBackground(new CountCallback() {
                @Override
                public void done(int count, ParseException e) {
                    if(e != null){
                        Log.e(TAG, "LikeLabel: Error retrieving like data: ", e);
                    }
                    tvLikes.setText(context.getResources()
                            .getString(R.string.likes_label,
                                    String.valueOf(count)));
                }
            });
        }

        private void bindLikeButton(Post post) {
            ParseQuery<Like> query = ParseQuery.getQuery(Like.class);
            query.whereEqualTo(Like.KEY_USER, ParseUser.getCurrentUser());
            query.whereEqualTo(Like.KEY_POST, post);
            query.findInBackground(new FindCallback<Like>() {
                @Override
                public void done(List<Like> like, ParseException e) {
                    if(e != null){
                        Log.e(TAG, "LikeButton: Error retrieving like data: ", e);
                    }
                    Log.d(TAG, "LIKE MATCHING POST AND USERNAME: " + like.toString());

                    // Post has been liked by user
                    if(!like.isEmpty()){
                        post.setLikeState(true);
                        btnLike.setImageResource(R.drawable.ufi_heart_active);
                    }else{ // Post not liked by user
                        post.setLikeState(false);
                        btnLike.setImageResource(R.drawable.ufi_heart);
                    }
                }
            });
            setLikeListener(post);
        }

        private void setLikeListener(Post post) {
            btnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    post.updateLikes();
                    if(post.isLiked){
                        unlikePost(post);
                        btnLike.setImageResource(R.drawable.ufi_heart);
                    }else{
                        likePost(post);
                        btnLike.setImageResource(R.drawable.ufi_heart_active);
                    }
                    post.changeLikedState();
                }
            });
        }
    }

    public void likePost(Post post){
        Like like = new Like();
        like.setPost(post);
        like.setUser(ParseUser.getCurrentUser());
        like.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error Liking: ", e);
                }
            }
        });
    }

    public void unlikePost(Post post){
        ParseQuery<Like> query = ParseQuery.getQuery(Like.class);
        query.whereEqualTo(Like.KEY_USER, ParseUser.getCurrentUser());
        query.whereEqualTo(Like.KEY_POST, post);
        query.findInBackground(new FindCallback<Like>() {

            // TODO: move like count server side
            // TODO: add null detection
            @Override
            public void done(List<Like> like, ParseException e) {
                like.get(0).deleteInBackground(new DeleteCallback() {
                    @Override
                    public void done(ParseException e) {
                    }
                });
            }
        });
    }

    public void clear(){
        posts.clear();
        notifyDataSetChanged();
    }

    public static String calculateTimeAgo(Date createdAt) {

        int SECOND_MILLIS = 1000;
        int MINUTE_MILLIS = 60 * SECOND_MILLIS;
        int HOUR_MILLIS = 60 * MINUTE_MILLIS;
        int DAY_MILLIS = 24 * HOUR_MILLIS;

        try {
            createdAt.getTime();
            long time = createdAt.getTime();
            long now = System.currentTimeMillis();

            final long diff = now - time;
            if (diff < MINUTE_MILLIS) {
                return "just now";
            } else if (diff < 2 * MINUTE_MILLIS) {
                return "a minute ago";
            } else if (diff < 50 * MINUTE_MILLIS) {
                return diff / MINUTE_MILLIS + "m ago";
            } else if (diff < 90 * MINUTE_MILLIS) {
                return "an hour ago";
            } else if (diff < 24 * HOUR_MILLIS) {
                return diff / HOUR_MILLIS + "h ago";
            } else if (diff < 48 * HOUR_MILLIS) {
                return "yesterday";
            } else {
                return diff / DAY_MILLIS + "d ago";
            }
        } catch (Exception e) {
            Log.i("Error:", "getRelativeTimeAgo failed", e);
            e.printStackTrace();
        }

        return "";
    }
}
