package com.example.notetaking;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.view.View;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Used to manage SharedPreferences and make runtime changes in configuration*/
public class PreferenceHelper {
    private static final String SHARED_PREF_NAME = "SharedPreferences";
    private static final Map<String,String> keys = new HashMap<>();
    private static SharedPreferences sSharedPreferences;
    private static PreferenceHelper sPreferenceHelper;
    private static final String THEME_PURPLE = "1";
    private static final String THEME_TURQUO = "2";

    private PreferenceHelper(Context context) {
        sSharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        keys.put("language","language");
        keys.put("theme","theme color");
    }

    public static PreferenceHelper getInstance(Context context) {
        if (sPreferenceHelper == null) {
            sPreferenceHelper = new PreferenceHelper(context);
        }
        return sPreferenceHelper;
    }

    public String getLanguage() {
        return sSharedPreferences.getString(keys.get("language"),"en");
    }

    public void setLanguage(String languageCode) {
        SharedPreferences.Editor editor = sSharedPreferences.edit();
        editor.putString(keys.get("language"),languageCode).apply();
    }

    public String getThemeColor() {
        return sSharedPreferences.getString(keys.get("theme"),THEME_PURPLE);
    }

    public void setThemeColor(String themeColor) {
        SharedPreferences.Editor editor = sSharedPreferences.edit();
        editor.putString(keys.get("theme"),themeColor).apply();
    }

    public Context setLocale(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources res = context.getResources();
        Configuration config = res.getConfiguration();
        config.setLocale(locale);
        res.updateConfiguration(config,res.getDisplayMetrics());
        context = context.createConfigurationContext(config);
        return context;
    }

    public void setTheme(Context context, String themeColor) {
        switch (themeColor) {
            case THEME_PURPLE:
                context.setTheme(R.style.Theme_NoteTaking);
                break;
            case THEME_TURQUO:
                context.setTheme(R.style.Turquoise);
                break;
        }
    }

    public void setBackgroundToTheme(View itemView) {
        String themeColor = getThemeColor();
        switch (themeColor) {
            case THEME_PURPLE:
                itemView.setBackgroundResource(R.drawable.note_bg);
                break;
            case THEME_TURQUO:
                itemView.setBackgroundResource(R.drawable.note_bg_turquoise);
                break;
        }
    }

    public void triggerRebirth(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
        ComponentName componentName = intent.getComponent();
        Intent mainIntent = Intent.makeRestartActivityTask(componentName);
        context.startActivity(mainIntent);
        Runtime.getRuntime().exit(0);
    }
}
