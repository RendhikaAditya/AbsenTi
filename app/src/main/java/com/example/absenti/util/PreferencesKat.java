package com.example.absenti.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.absenti.actifity.AddActivity;

public class PreferencesKat {
    public static final String id_kategori = "id_kategori";


    private static SharedPreferences getSharedPreference(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setId_kategori(Context context, String id){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(id_kategori, id);
        editor.apply();
    }

    public static String getId_kategori(Context context){
        return getSharedPreference(context).getString(id_kategori, "");
    }

    public static void clearUser(Context context) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.remove(id_kategori);
    }
}
