package ru.timestop.android.timer;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import static android.widget.Toast.LENGTH_LONG;

import ru.timestop.android.myapplication.R;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);
        Toast.makeText(this, "Hi", LENGTH_LONG).show();
    }
}
