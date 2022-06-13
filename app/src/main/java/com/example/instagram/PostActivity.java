package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.instagram.databinding.ActivityPostBinding;
import com.example.instagram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.List;

public class PostActivity extends AppCompatActivity {

    public static final String TAG = "PostActivity";
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 15;

    private ActivityPostBinding binding;

    private EditText etDescription;
    private ImageView ivImage;
    private Button btnTakeImage;
    private Button btnPost;

    private File photoFile;
    private String photoFileName = "photo.jpg";

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
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = etDescription.getText().toString();
                if(description.isEmpty()){
                    Toast.makeText(PostActivity.this, "Description cannot be empty...", Toast.LENGTH_LONG).show();
                    return;
                }
                savePost(description, ParseUser.getCurrentUser());
            }
        });

        btnTakeImage = binding.btnTakeImage;
        btnTakeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
            }
        });

        queryPosts();
    }

    //TODO: implement ActivityResultLauncher instead of startActivityForResult
    private void launchCamera() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        Uri fileProvider = FileProvider.getUriForFile(PostActivity.this, "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }

    }

    public File getPhotoFileUri(String fileName) {
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }

    private void savePost(String description, ParseUser parseUser) {
        Post post = new Post();
        post.setDescription(description);
        post.setUser(parseUser);

        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error Posting: ", e);
                }
                etDescription.setText("");
            }
        });
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