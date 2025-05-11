package com.example.androidsurvefy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Button checkSurveysButton = findViewById(R.id.button_check_surveys);
        checkSurveysButton.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, AvailableSurveysActivity.class);
            startActivity(intent);
        });

    }
}
