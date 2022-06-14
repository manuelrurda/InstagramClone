package com.example.instagram;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.parse.ParseFile;
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
    private String photoFileName = "instagram-photo.jpg";

    private ActivityResultLauncher<Intent> cameraResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Log.d(TAG, "onActivityResult:" + result.getData().toString());
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                        ivImage.setImageBitmap(takenImage);
                    } else {
                        Toast.makeText(PostActivity.this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
                    }
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
                savePost(description, photoFile, ParseUser.getCurrentUser());
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


    private void launchCamera() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        Uri fileProvider = FileProvider.getUriForFile(PostActivity.this, "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, app will crash.
        if (intent.resolveActivity(getPackageManager()) != null) {
            cameraResultLauncher.launch(intent);
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


    private void savePost(String description, File photoFile, ParseUser parseUser) {
        Post post = new Post();
        post.setDescription(description);
        post.setUser(parseUser);
        post.setImage(new ParseFile(photoFile));

        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error Posting: ", e);
                }
                etDescription.setText("");
                ivImage.setImageResource(0);
            }
        });
    }htiknnuluknn


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