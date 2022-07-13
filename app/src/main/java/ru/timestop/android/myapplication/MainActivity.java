package ru.timestop.android.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import ru.timestop.android.camera.CameraActivity;
import ru.timestop.android.chat.ChatActivity;
import ru.timestop.android.location.LocationActivity;
import ru.timestop.android.movies.MovieActivity;
import ru.timestop.android.splash.SplashActivity;
import ru.timestop.android.timer.TimerActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button bttnMovies;
    private Button bttnCamera;
    private Button bttnChat;
    private Button bttnTimer;
    private Button bttnLocation;
    private Button bttnSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        this.setTheme(R.style.Theme_MyApplication);

        bttnMovies = this.findViewById(R.id.bttnMovies);
        bttnMovies.setOnClickListener(this);

        bttnChat = this.findViewById(R.id.bttnChat);
        bttnChat.setOnClickListener(this);

        bttnCamera = this.findViewById(R.id.bttnCamera);
        bttnCamera.setOnClickListener(this);

        bttnTimer = this.findViewById(R.id.bttnTimer);
        bttnTimer.setOnClickListener(this);

        bttnLocation = this.findViewById(R.id.bttnLocation);
        bttnLocation.setOnClickListener(this);

        bttnSplash = this.findViewById(R.id.bttnSplash);
        bttnSplash.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (bttnMovies.getId() == v.getId()) {
            Intent intent = new Intent(this, MovieActivity.class);
            startActivity(intent);
        } else if (bttnTimer.getId() == v.getId()) {
            Intent intent = new Intent(this, TimerActivity.class);
            startActivity(intent);
        } else if (bttnCamera.getId() == v.getId()) {
            Intent intent = new Intent(this, CameraActivity.class);
            startActivity(intent);
        } else if (bttnChat.getId() == v.getId()) {
            Intent intent = new Intent(this, ChatActivity.class);
            startActivity(intent);
        } else if (bttnLocation.getId() == v.getId()) {
            Intent intent = new Intent(this, LocationActivity.class);
            startActivity(intent);
        } else if (bttnSplash.getId() == v.getId()) {
            Intent intent = new Intent(this, SplashActivity.class);
            startActivity(intent);
        } else {
            Log.d("Error", "Id = " + v.getId());
        }
    }
}