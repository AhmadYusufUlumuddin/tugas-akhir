package com.example.arkacamataadmin.config;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.arkacamataadmin.SignInActivity;

import java.util.HashMap;

public class SharePreference {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "ar_kacamata_admin";
    private static final String IS_LOGIN = "LOGIN";
    public static final String KEY_ID_ADMIN = "id_tb_admin";
    public static final String KEY_USERNAME = "username";

    public SharePreference(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void create_session(String id, String username){
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_ID_ADMIN, id);
        editor.putString(KEY_USERNAME, username);
        editor.commit();
    }

    public void update(String username){
        editor.putString(KEY_USERNAME, username);
        editor.commit();
    }
    public void logoutUser(){
        editor.clear();
        editor.commit();

        Intent i = new Intent(_context, SignInActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_ID_ADMIN, pref.getString(KEY_ID_ADMIN, null));
        user.put(KEY_USERNAME, pref.getString(KEY_USERNAME, null));
        return user;
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}
