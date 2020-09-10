package com.example.absenti.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "data_app";

    public SharedPrefManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setIdUser(String idUser){
        editor.putString("idUser", idUser);
        editor.apply();
    }
    public String getIdUser(){
        return pref.getString("idUser", "");
    }

    public void setMhsLoginStatus(boolean islogin){
        editor.putBoolean("Mlogin", islogin);
        editor.apply();
    }
    public void setDsnLoginStatus(boolean islogin){
        editor.putBoolean("Dlogin", islogin);
        editor.apply();
    }

    public boolean getMhsLoginStatus(){
        return pref.getBoolean("Mlogin", false);
    }
    public boolean getDsnLoginStatus(){
        return pref.getBoolean("Dlogin", false);
    }

}
