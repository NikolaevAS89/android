package ru.timestop.android.myapplication;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

public class SoundPreference extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.some_preferences);

    }


}
