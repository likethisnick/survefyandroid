package com.example.androidsurvefy;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidsurvefy.Adapter.NoteAdapter;
import com.example.androidsurvefy.Model.Note;
import com.example.androidsurvefy.Model.NoteResponse;
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

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Button backButton = findViewById(R.id.button_back);
        backButton.setOnClickListener(v -> finish());

        loadNotes();
    }

    private void loadNotes() {
        String token = AppContext.getInstance().getToken();

        if (token != null && !token.isEmpty()) {
            api = ApiClient.getApiService(token);
        }
        else {
            api = ApiClient.getApiService(null);
        }

        Call<NoteResponse> call = api.getNotes();

        call.enqueue(new Callback<NoteResponse>() {
            @Override
            public void onResponse(Call<NoteResponse> call, Response<NoteResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Note> notes = response.body().getNotes();
                    NoteAdapter adapter = new NoteAdapter(notes);
                    recyclerView.setAdapter(adapter);
                } else {
                    try {
                        String errorBody = response.errorBody() != null
                                ? response.errorBody().string()
                                : "empty error body";

                        Log.e("API_ERROR", "Server returned error: " + response.code() + "\n" + errorBody);

                        Toast.makeText(AvailableSurveysActivity.this,
                                "Error " + response.code() + ": " + errorBody,
                                Toast.LENGTH_LONG).show();

                    } catch (IOException e) {
                        Log.e("API_ERROR", "Failed to read error body", e);
                        Toast.makeText(AvailableSurveysActivity.this, "Unknown error", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<NoteResponse> call, Throwable t) {
                Log.e("API_ERROR", "Connecting to the server issue", t);
                Toast.makeText(AvailableSurveysActivity.this, "Connection error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
