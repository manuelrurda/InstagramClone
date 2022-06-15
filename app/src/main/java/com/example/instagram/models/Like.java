package com.example.instagram.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Like")
public class Like extends ParseObject {
    public static final String KEY_USER = "user";
    public static final String KEY_POST = "post";

    public ParseObject getUser() {return getParseObject(KEY_USER);}

    public void setUser (ParseObject parseUser) {put(KEY_USER, parseUser);}

    public ParseObject getPost() {return getParseObject(KEY_POST);}

    public void setPost (ParseObject parsePost) {put(KEY_POST, parsePost);}
}
