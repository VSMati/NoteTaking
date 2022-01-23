package com.example.notetaking;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Class is used to set user's settings any time new Activity is created*/
public class SettingsCompat extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        PreferenceHelper ph = PreferenceHelper.getInstance(this);
        ph.setTheme(this,ph.getThemeColor());
        super.onCreate(savedInstanceState);
        ph.setLocale(this, ph.getLanguage());
    }
}
