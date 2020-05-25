package com.example.parstagram;

import com.parse.ParseFile;
import com.parse.ParseUser;

public class User extends ParseUser {

    public static final String KEY_PROFILE = "profile";

    public ParseFile getProfileImage(){
        return getParseFile(KEY_PROFILE);
    }

    public void setKeyProfileImage(ParseFile profileImage){
        put(KEY_PROFILE, profileImage);
    }
}
