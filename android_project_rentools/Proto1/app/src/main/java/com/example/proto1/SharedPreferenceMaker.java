package com.example.proto1;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceMaker {
    private final Context context;

    public SharedPreferenceMaker(Context context) {
        this.context = context;
    }

    private SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences("Login",MODE_PRIVATE);
    }

    public SharedPreferences.Editor getSPEditor() {
        return getSharedPreferences().edit();
    }

    public boolean getCheckboxStatus() {
        return getSharedPreferences().getBoolean("status", false);
    }

    public String getUserEmail() {
        return getSharedPreferences().getString("user_now",null);
    }

    public String getSearchedCity(){
        return getSharedPreferences().getString("cityNow",null);
    }

    public String getUserName(){
        return getSharedPreferences().getString("username", null);
    }

    public String getPassword(){
        return getSharedPreferences().getString("pass", null);
    }

    public String getLanguage(){
        return getSharedPreferences().getString("lang","en");
    }
}
