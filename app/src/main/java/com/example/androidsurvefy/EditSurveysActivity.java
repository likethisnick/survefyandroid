package com.example.androidsurvefy;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidsurvefy.Adapter.SurveyAdapter;
import com.example.androidsurvefy.Model.TemplateSurveyDto;
import com.example.androidsurvefy.Model.TemplateSurveysResponse;
import com.example.androidsurvefy.Network.ApiClient;
import com.example.androidsurvefy.Network.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditSurveysActivity extends AppCompatActivity {
    private ApiService api;
    private TextView textError;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_surveys);

        Button backButton = findViewById(R.id.buttonBack);
        textError = findViewById(R.id.textError);
        recyclerView = findViewById(R.id.recyclerSurveys);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        api = ApiClient.getApiService(AppContext.getInstance().getToken());
        String userId = AppContext.getInstance().getUserId();
        loadSurveys(userId);

        backButton.setOnClickListener(v -> finish());
    }

    private void loadSurveys(String userId) {
        api.getSurveys(userId).enqueue(new Callback<TemplateSurveysResponse>() {
            @Override
            public void onResponse(Call<TemplateSurveysResponse> call, Response<TemplateSurveysResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<TemplateSurveyDto> surveys = response.body().getSurveys();

                    if (surveys.isEmpty()) {
                        textError.setText("No surveys found.");
                        textError.setVisibility(View.VISIBLE);
                    } else {
                        recyclerView.setAdapter(new SurveyAdapter(surveys, EditSurveysActivity.this));
                        textError.setVisibility(View.GONE);
                    }
                } else {
                    textError.setText("Loading error: " + response.code());
                    textError.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<TemplateSurveysResponse> call, Throwable t) {
                textError.setText("Server error: " + t.getMessage());
                textError.setVisibility(View.VISIBLE);
                Log.e("API", "loadSurveys error", t);
            }
        });
    }
}
