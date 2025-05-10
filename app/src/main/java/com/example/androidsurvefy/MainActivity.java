package com.example.androidsurvefy;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidsurvefy.Adapter.NoteAdapter;
import com.example.androidsurvefy.Model.LoginRequest;
import com.example.androidsurvefy.Model.Note;
import com.example.androidsurvefy.Model.NoteResponse;
import com.example.androidsurvefy.Model.ProfileResponse;
import com.example.androidsurvefy.Model.TokenResponse;
import com.example.androidsurvefy.Network.ApiClient;
import com.example.androidsurvefy.Network.ApiService;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    NoteAdapter adapter;
    private ApiService api;
    protected String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5219/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = ApiClient.getApiService(null);
        loginAndFetchProfile("admin2@admin.com", "admin2");

    }

    private void loadNotes() {
    /*    Call<NoteResponse> call = ApiClient.getNotesApi().getNotes();
        call.enqueue(new Callback<NoteResponse>() {
            @Override
            public void onResponse(Call<NoteResponse> call, Response<NoteResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Note> notes = response.body().getNotes();
                    adapter = new NoteAdapter(notes);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<NoteResponse> call, Throwable t) {
                Log.e("API_ERROR", "Error: ", t);
            }
        });*/
    }

    private void loginAndFetchProfile(String email, String password) {
        LoginRequest loginRequest = new LoginRequest(email, password);

        api.login(loginRequest).enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().token;

                    if (token == null || token.isEmpty()) {
                        Log.e("API", "Получен пустой токен. Запрос профиля не будет выполнен.");
                        return;
                    }

                    String authHeader = "Bearer " + token;

                    api.getProfile(authHeader).enqueue(new Callback<ProfileResponse>() {
                        @Override
                        public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                String userId = response.body().userId;
                                String token1 = response.body().token;
                                
                                Boolean isauth = response.body().isAuth;

                                return;
                            } else {
                                try {
                                    String errorBody = response.errorBody() != null
                                            ? response.errorBody().string()
                                            : "no error body";

                                    Log.e("API", "Ошибка профиля: " + response.code() + "\nТело ошибки: " + errorBody);
                                } catch (IOException e) {
                                    Log.e("API", "Ошибка чтения тела ошибки", e);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ProfileResponse> call, Throwable t) {
                            Log.e("API", "Ошибка запроса профиля", t);
                        }
                    });

                } else {
                    try {
                        String errorBody = response.errorBody() != null
                                ? response.errorBody().string()
                                : "no error body";
                        Log.e("API", "❌ Ошибка входа: " + response.code() + "\n" + errorBody);
                    } catch (IOException e) {
                        Log.e("API", "Ошибка чтения тела ошибки при логине", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
                Log.e("API", "❌ Ошибка логина", t);
            }
        });
    }
}
