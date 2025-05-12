package com.example.androidsurvefy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SurveyDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_detail);

        Button buttonOpenQuestions = findViewById(R.id.buttonOpenQuestions);


        String surveyId = getIntent().getStringExtra("id");
        String name = getIntent().getStringExtra("name");
        String description = getIntent().getStringExtra("description");
        String createdOn = getIntent().getStringExtra("createdOn");

        TextView textDetails = findViewById(R.id.textSurveyDetails);
        textDetails.setText("Survey Id: " + surveyId + "\n\n" +
                "Name: " + name + "\n\n" +
                "Description: " + description + "\n\n" +
                "Created On: " + createdOn);

        buttonOpenQuestions.setOnClickListener(v -> {
            Intent intent = new Intent(SurveyDetailActivity.this, QuestionsActivity.class);
            intent.putExtra("surveyId", surveyId);
            startActivity(intent);
        });

        Button backButton = findViewById(R.id.buttonBack);
        backButton.setOnClickListener(v -> finish());
    }
}