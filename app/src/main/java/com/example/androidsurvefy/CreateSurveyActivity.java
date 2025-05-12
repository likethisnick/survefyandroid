package com.example.androidsurvefy;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidsurvefy.Model.CreateSurveyRequest;
import com.example.androidsurvefy.Model.CreateSurveyResponse;
import com.example.androidsurvefy.Network.ApiClient;
import com.example.androidsurvefy.Network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateSurveyActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText descriptionEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_survey);

        nameEditText = findViewById(R.id.editTextName);
        descriptionEditText = findViewById(R.id.editTextDescription);
        Button createButton = findViewById(R.id.buttonCreate);
        Button backButton = findViewById(R.id.buttonBack);

        createButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString();
            String description = descriptionEditText.getText().toString();

            createSurvey(name,description);
            Toast.makeText(this, "Created: " + name, Toast.LENGTH_SHORT).show();
        });

        backButton.setOnClickListener(v -> finish());
    }


    private void createSurvey(String name, String description) {
        TextView errorText = findViewById(R.id.textError);

        String token = AppContext.getInstance().getToken();
        String userId = AppContext.getInstance().getUserId();

        if (token == null || token.isEmpty()) {
            errorText.setText("Authorization token missing.");
            errorText.setVisibility(View.VISIBLE);
            return;
        }

        CreateSurveyRequest request = new CreateSurveyRequest(name, description);

        ApiService api = ApiClient.getApiService(token);

        api.createTemplateSurvey(request).enqueue(new Callback<CreateSurveyResponse>() {
            @Override
            public void onResponse(Call<CreateSurveyResponse> call, Response<CreateSurveyResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String surveyId = response.body().getId();
                    Log.d("API", "Survey created with ID: " + surveyId);
                    finish();
                } else {
                    errorText.setText("Failed to create survey: " + response.code());
                    errorText.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<CreateSurveyResponse> call, Throwable t) {
                errorText.setText("Error: " + t.getMessage());
                errorText.setVisibility(View.VISIBLE);
                Log.e("API", "createSurvey error", t);
            }
        });
    }
}
