package com.example.androidsurvefy;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidsurvefy.Adapter.AllSurveyAdapter;
import com.example.androidsurvefy.Model.TemplateSurveyDto;
import com.example.androidsurvefy.Model.TemplateSurveysResponse;
import com.example.androidsurvefy.Network.ApiClient;
import com.example.androidsurvefy.Network.ApiService;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AvailableSurveysActivity extends AppCompatActivity {

    private ApiService api;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_surveys);

        recyclerView = findViewById(R.id.recyclerSurveys);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Button backButton = findViewById(R.id.button_back);
        backButton.setOnClickListener(v -> finish());

        loadAllSurveys();
    }

    private void loadAllSurveys() {
        ApiService api = ApiClient.getApiService(AppContext.getInstance().getToken());

        api.getAllSurveys().enqueue(new Callback<TemplateSurveysResponse>() {
            @Override
            public void onResponse(Call<TemplateSurveysResponse> call, Response<TemplateSurveysResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<TemplateSurveyDto> surveys = response.body().getSurveys();

                    AllSurveyAdapter adapter = new AllSurveyAdapter(surveys);
                    recyclerView.setAdapter(adapter);
                } else {
                    Log.e("API", "Loading error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<TemplateSurveysResponse> call, Throwable t) {
                Log.e("API", "Communication error", t);
            }
        });
    }
}
