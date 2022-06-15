package com.example.instagram.models;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.concurrent.atomic.AtomicInteger;


@ParseClassName("Post")
public class Post extends ParseObject {

    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_USER = "user";
    public static final String KEY_LIKES = "likes";

    public boolean isLiked = false;

    private static final String TAG = "POST";

    public String getDescription(){
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description){
        put(KEY_DESCRIPTION, description);
    }

    public ParseFile getImage(){
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile parseFile){
        put(KEY_IMAGE, parseFile);
    }

    public ParseUser getUser(){return getParseUser(KEY_USER);}

    public void setUser(ParseUser parseUser){
        put(KEY_USER, parseUser);
    }

    public int getLikes() {
        return getNumber(KEY_LIKES).intValue();
    }
//    public int queryLikes() {
//        ParseQuery<Like> query = ParseQuery.getQuery(Like.class);
//        query.whereEqualTo("post", this);
//        query.countInBackground(((count, e) -> {
//            if (e != null) {
//                Log.e(TAG, "Error Retrieving Likes: ", e);
//                return;
//            }
//        }));
//    }

}
