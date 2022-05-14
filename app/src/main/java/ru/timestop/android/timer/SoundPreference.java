package ru.timestop.android.timer;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import ru.timestop.android.myapplication.R;

public class SoundPreference extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.some_preferences);

    }


}
