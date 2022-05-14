package ru.timestop.android.timer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import ru.timestop.android.myapplication.R;

public class TimerActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener
        , View.OnClickListener {

    private SeekBar skbrTimeLeft;
    private Button bttnControll;
    private Button bttnPreference;
    private TextView txvTimeLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        this.setTheme(R.style.Theme_MyApplication);
        bttnControll = this.findViewById(R.id.bttnControll);
        bttnControll.setOnClickListener(this);

        bttnPreference = this.findViewById(R.id.bttnPreference);
        bttnPreference.setOnClickListener(this);

        txvTimeLeft = this.findViewById(R.id.txvTimeLeft);
        txvTimeLeft.setFocusable(false);
        txvTimeLeft.setEnabled(false);

        skbrTimeLeft = this.findViewById(R.id.skbrTimeLeft);
        skbrTimeLeft.setEnabled(true);
        skbrTimeLeft.setFocusable(true);
        skbrTimeLeft.setMax(600);
        skbrTimeLeft.setOnSeekBarChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = this.getMenuInflater();
        menuInflater.inflate(R.menu.settings_menu, menu);
        return true;
    }

    /*public void doIncress(View view) {
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
    }*/


    @SuppressLint("SetTextI18n")
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        int minutes = progress / 60;
        int seconds = progress % 60;
        txvTimeLeft.setText(minutes + ":" + seconds);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        //DO_NOTHING
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        //DO_NOTHING
    }


    @Override
    public void onClick(View v) {
        if (bttnControll.getId() == v.getId()) {
            bttnControll.setEnabled(false);
            bttnControll.setFocusable(false);
            skbrTimeLeft.setEnabled(false);
            skbrTimeLeft.setFocusable(false);
            CountDownTimer timer = new CountDownTimer(1000L * this.skbrTimeLeft.getProgress(), 1000L) {
                @SuppressLint("SetTextI18n")
                @Override
                public void onTick(long millisUntilFinished) {
                    Log.d("Tick", "millisUntilFinished=" + millisUntilFinished);
                    long minutes = millisUntilFinished / 1000L / 60L;
                    long seconds = millisUntilFinished / 1000L % 60L;
                    skbrTimeLeft.setProgress((int) millisUntilFinished / 1000);
                    txvTimeLeft.setText(minutes + ":" + seconds);
                }

                @Override
                public void onFinish() {
                    skbrTimeLeft.setEnabled(true);
                    skbrTimeLeft.setFocusable(true);
                    bttnControll.setEnabled(true);
                    bttnControll.setFocusable(true);
                    MediaPlayer player = MediaPlayer.create(getApplicationContext(), R.raw.kalimba);
                    player.start();
                }
            };
            timer.start();
        } else {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            boolean isSoundEnable = preferences.getBoolean("enable_sound", false);

            Log.d("Preference", ">>>>>>>" + isSoundEnable);
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        //return super.onOptionsItemSelected(item);
        return true;
    }
}