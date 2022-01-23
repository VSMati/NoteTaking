package com.example.notetaking.controllers;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.notetaking.PreferenceHelper;
import com.example.notetaking.R;

import java.util.Arrays;

public class SettingsFragment extends PreferenceFragmentCompat{
    private static final String TAG = SettingsFragment.class.getSimpleName();

    private ListPreference mLanguagePreference;
    private ListPreference mThemePreference;
    private PreferenceHelper mPreferenceHelper;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences_settings, rootKey);
        mPreferenceHelper = PreferenceHelper.getInstance(requireContext());
        mLanguagePreference = getPreferenceManager().findPreference("language");
        mThemePreference = getPreferenceManager().findPreference("theme color");

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLanguagePreference.setOnPreferenceChangeListener((preference, newValue) -> {
            mPreferenceHelper.setLanguage((String) newValue);
            mPreferenceHelper.setLocale(requireContext(), mPreferenceHelper.getLanguage());
            return true;
        });

        mThemePreference.setOnPreferenceChangeListener((preference, newValue) -> {
            mPreferenceHelper.setThemeColor((String) newValue);
            return true;
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPreferenceHelper.triggerRebirth(requireContext()); //reload everything to make changes
    }
}
