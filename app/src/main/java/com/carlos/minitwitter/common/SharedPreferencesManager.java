package com.carlos.minitwitter.common;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {

    private static final String APP_SETTING_FILES = "APP_SETTING";

    public SharedPreferencesManager() {}

    public static SharedPreferences getSharedPreferences() {
        return MyApplication.getContext().getSharedPreferences(APP_SETTING_FILES, Context.MODE_PRIVATE);
    }

    public static void setString(String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getString(String key) {
        return getSharedPreferences().getString(key, null);
    }
}
