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

        //button CHECK SURVEYS
        Button checkSurveysButton = findViewById(R.id.button_check_surveys);
        checkSurveysButton.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, AvailableSurveysActivity.class);
            startActivity(intent);
        });

        //button EDIT MY SURVEYS
        Button editButton = findViewById(R.id.button_edit_surveys);
        editButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditSurveysActivity.class);
            startActivity(intent);
        });


        //button CREATE NEW SURVEY
        Button createButton = findViewById(R.id.button_create_survey);
        createButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, CreateSurveyActivity.class);
            startActivity(intent);
        });

    }
}
