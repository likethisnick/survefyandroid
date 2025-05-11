package com.example.androidsurvefy;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidsurvefy.Model.ErrorResponse;
import com.example.androidsurvefy.Model.LoginRequest;
import com.example.androidsurvefy.Model.ProfileResponse;
import com.example.androidsurvefy.Model.TokenResponse;
import com.example.androidsurvefy.Network.ApiClient;
import com.example.androidsurvefy.Network.ApiService;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private ApiService api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText repeatPasswordInput = findViewById(R.id.editTextRepeatPassword);
        EditText emailInput = findViewById(R.id.editTextEmail);
        EditText passwordInput = findViewById(R.id.editTextPassword);
        Button registerButton = findViewById(R.id.buttonRegisterSubmit);
        TextView errorText = findViewById(R.id.textError);

        api = ApiClient.getApiService(null);

        registerButton.setOnClickListener(v -> {
            errorText.setVisibility(View.GONE);
            String email = emailInput.getText().toString();
            String password = passwordInput.getText().toString();
            String repeatPassword = repeatPasswordInput.getText().toString();

            if (!password.equals(repeatPassword)) {
                errorText.setText("Password doesn't match");
                errorText.setVisibility(View.VISIBLE);
                return;
            }


            register(email, password, errorText);
        });
    }

    private void register(String email, String password, TextView errorText) {
        LoginRequest request = new LoginRequest(email, password);

        api.register(request).enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().token;
                    String userId = response.body().userId;

                    if (token == null || token.isEmpty()) {
                        errorText.setText("Empty token");
                        errorText.setVisibility(View.VISIBLE);
                        return;
                    }

                    AppContext.getInstance().setToken(token);

                    if (userId != null) {
                        AppContext.getInstance().setUserId(userId);
                        errorText.setVisibility(View.GONE);
                        finish();
                    } else {
                        errorText.setVisibility(View.GONE);
                        finish();
                    }

                } else {
                    try {
                        if (response.errorBody() != null) {
                            String errorBody = response.errorBody().string();

                            Gson gson = new Gson();
                            ErrorResponse errorResponse = gson.fromJson(errorBody, ErrorResponse.class);

                            if (errorResponse != null && errorResponse.errors != null && !errorResponse.errors.isEmpty()) {
                                String message = TextUtils.join("\n", errorResponse.errors);
                                errorText.setText(message);
                            } else {
                                errorText.setText("Unknown registration error");
                            }
                        } else {
                            errorText.setText("Registration error");
                        }
                    } catch (Exception e) {
                        Log.e("API", "Response json parsing error", e);
                        errorText.setText("Registration error");
                    }

                    errorText.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
                errorText.setText("Ошибка соединения с сервером");
                errorText.setVisibility(View.VISIBLE);
                Log.e("API", "Ошибка регистрации", t);
            }
        });
    }

}