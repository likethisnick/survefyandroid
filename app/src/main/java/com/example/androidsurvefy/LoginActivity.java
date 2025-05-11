package com.example.androidsurvefy;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidsurvefy.Model.LoginRequest;
import com.example.androidsurvefy.Model.TokenResponse;
import com.example.androidsurvefy.Network.ApiClient;
import com.example.androidsurvefy.Network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private ApiService api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText emailInput = findViewById(R.id.editTextEmail);
        EditText passwordInput = findViewById(R.id.editTextPassword);
        Button loginButton = findViewById(R.id.buttonLoginSubmit);
        TextView errorText = findViewById(R.id.textError);

        api = ApiClient.getApiService(null);

        loginButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString();
            String password = passwordInput.getText().toString();
            login(email, password);
        });
    }

    private void login(String email, String password) {
        LoginRequest loginRequest = new LoginRequest(email, password);
        TextView errorText = findViewById(R.id.textError);

        api.login(loginRequest).enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().token;
                    String userId = response.body().userId;

                    if (token == null || token.isEmpty()) {
                        Log.e("API", "empty token");
                        errorText.setVisibility(View.VISIBLE);
                        return;
                    }
                    AppContext.getInstance().setToken(token);
                    AppContext.getInstance().setUserId(userId);
                    finish();
                } else {
                    errorText.setVisibility(View.VISIBLE);
                    Log.e("API", "login error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
                Log.e("API", "Ошибка логина", t);
            }
        });
    }
}