package ru.timestop.android.splash;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import ru.timestop.android.CenterCropDrawable;
import ru.timestop.android.myapplication.R;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ConstraintLayout constraintLayout = new ConstraintLayout(this);
        constraintLayout.setBackground(new CenterCropDrawable(ResourcesCompat.getDrawable(this.getResources(), R.drawable.chip, null)));
        this.setContentView(constraintLayout);

    }
}
