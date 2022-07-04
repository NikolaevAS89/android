package ru.timestop.android.location;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import ru.timestop.android.myapplication.R;

public class LocationActivity extends AppCompatActivity implements View.OnClickListener,
        OnSuccessListener<LocationSettingsResponse>,
        OnFailureListener {
    private final static int CHECK_SETTINGS_CODE = 0x111;
    private final static int CHECK_PERMISSION_CODE = 0x110;

    private Button bttnStart;
    private Button bttnStop;
    private TextView txtE;
    private TextView txtW;
    private TextView txtAltitude;
    private TextView txtTime;
    private Snackbar snackbar = null;

    private boolean isTracked = false;

    private FusedLocationProviderClient fusedLocationClient;

    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private SettingsClient settingsClient;
    private LocationSettingsRequest locationSettingsRequest;

    private void initLocationRequest() {
        this.locationRequest = LocationRequest.create();
        this.locationRequest.setInterval(12000L);
        this.locationRequest.setFastestInterval(2000L);
        this.locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void initLocationCallback() {
        this.locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                updateLocation(locationResult.getLastLocation());
            }
        };
    }

    private void initSettingsRequest() {
        locationSettingsRequest = new LocationSettingsRequest.Builder()
                .addLocationRequest(this.locationRequest).build();
    }

    private void startLocationUpdates() {
        bttnStart.setEnabled(false);
        settingsClient.checkLocationSettings(locationSettingsRequest)
                .addOnSuccessListener(this, this)
                .addOnFailureListener(this, this);
    }

    private void stopLocationUpdate() {
        bttnStop.setEnabled(false);
        fusedLocationClient.removeLocationUpdates(locationCallback)
                .addOnSuccessListener(this, task -> setTrecked(false))
                .addOnFailureListener(this, task -> setTrecked(true));
    }

    private void setTrecked(boolean isTracked) {
        this.isTracked = isTracked;
        this.bttnStart.setEnabled(!isTracked);
        this.bttnStop.setEnabled(isTracked);
    }

    private void updateLocation(Location location) {
        if (location != null) {
            txtAltitude.setText(String.valueOf(location.getAltitude()));
            txtW.setText(String.valueOf(location.getLatitude()));
            txtE.setText(String.valueOf(location.getLongitude()));
        }
        txtTime.setText(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now()));
    }

    private void checkPermissionAndRun() {
        int fine_location_permit = ActivityCompat.checkSelfPermission(LocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (fine_location_permit == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
                    .addOnSuccessListener(this, task -> setTrecked(true))
                    .addOnFailureListener(this, task -> setTrecked(false));
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            bttnStart.setEnabled(true);
            showSnacbar();
        } else {
            bttnStart.setEnabled(true);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, CHECK_PERMISSION_CODE);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_location);
        this.bttnStart = this.findViewById(R.id.bttnStart);
        this.bttnStart.setOnClickListener(this);
        this.bttnStop = this.findViewById(R.id.bttnStop);
        this.bttnStop.setOnClickListener(this);
        this.txtE = this.findViewById(R.id.txtE);
        this.txtW = this.findViewById(R.id.txtW);
        this.txtTime = this.findViewById(R.id.txtTime);
        this.txtAltitude = this.findViewById(R.id.txtAltitude);

        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        this.settingsClient = LocationServices.getSettingsClient(this);

        this.initLocationRequest();
        this.initLocationCallback();
        this.initSettingsRequest();
    }

    @Override
    public void onClick(View v) {
        if (bttnStart.getId() == v.getId()) {
            startLocationUpdates();
        } else if (bttnStop.getId() == v.getId()) {
            stopLocationUpdate();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, CHECK_PERMISSION_CODE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isTracked) {
            stopLocationUpdate();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isTracked) {
            startLocationUpdates();
        }

    }

    @Override
    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
        this.checkPermissionAndRun();
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        bttnStart.setEnabled(true);
        if (e instanceof ResolvableApiException) {
            try {
                ((ResolvableApiException) e).startResolutionForResult(LocationActivity.this, CHECK_SETTINGS_CODE);
            } catch (IntentSender.SendIntentException sendEx) {
                sendEx.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Please change location settings in manual mode", Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("STEP", "8.1");
        if (requestCode == CHECK_SETTINGS_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                startLocationUpdates();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CHECK_PERMISSION_CODE) {
            System.out.println(Arrays.toString(permissions));
            System.out.println(Arrays.toString(grantResults));
            if (Arrays.stream(grantResults).anyMatch(itm -> itm == PackageManager.PERMISSION_GRANTED)) {
                startLocationUpdates();
            } else {
                Toast.makeText(this, "Please change location settings in manual mode", Toast.LENGTH_LONG)
                        .show();
            }
        }
    }


    private void showSnacbar() {
        if (snackbar == null) {
            snackbar = Snackbar.make(findViewById(android.R.id.content),
                            "Need location permission",
                            Snackbar.LENGTH_INDEFINITE)
                    .setAction("LocationPermission", this);
        }
        snackbar.show();
    }
}