package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.instagram.databinding.ActivityPostBinding;
import com.example.instagram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

public class PostActivity extends AppCompatActivity {

    public static final String TAG = "PostActivity";

    private ActivityPostBinding binding;

    private EditText etDescription;
    private ImageView ivImage;
    private Button btnTakeImage;
    private Button btnPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        // Set up view binding
        binding = ActivityPostBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        etDescription = binding.etDescription;
        ivImage = binding.ivImage;
        btnPost = binding.btnPost;
        btnTakeImage = binding.btnTakeImage;

        queryPosts();
    }

    // Testing queries
    private void queryPosts(){
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error Posting: ", e);
                }
                for(Post post: posts){
                    Log.i(TAG, "Post: " + post.getDescription() + " User: @" + post.getUser().getUsername());
                }
            }
        });
    }
}