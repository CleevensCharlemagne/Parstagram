package com.example.parstagram;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;

@ParseClassName("Profile")
public class Profile extends ParseObject {
    public static final String KEY_PROFILE = "profile";
    public static final String KEY_USER = "user";
    public static final String KEY_CREATED_AT = "createdAt";

    public ParseFile getProfile(){
        return getParseFile(KEY_PROFILE);
    }

    public void setProfile(ParseFile image){
        put(KEY_PROFILE, image);
    }

    public ParseUser getUser(){
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user){
        put(KEY_USER, user);
    }

    public Date getCreatedAt() { return getDate(KEY_CREATED_AT);}
}
