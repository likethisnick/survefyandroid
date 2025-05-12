package com.example.androidsurvefy;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidsurvefy.Model.ProfileResponse;
import com.example.androidsurvefy.Network.ApiClient;
import com.example.androidsurvefy.Network.ApiService;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    private ApiService api;
    protected String token;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View statusIndicator = findViewById(R.id.statusIndicator);
        Button buttonLogin = findViewById(R.id.button_login);
        Button buttonCheckLogin = findViewById(R.id.button_logincheck);
        Button logoutButton = findViewById(R.id.button_logout);
        Button buttonRegister = findViewById(R.id.button_register);
        Button buttonDashboard = findViewById(R.id.button_dashboard);

        // button LOGIN
        buttonLogin.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        // button CHECK AUTH
        buttonCheckLogin.setOnClickListener(v -> {
            String token = AppContext.getInstance().getToken();

            if (token != null && !token.isEmpty()) {
                api = ApiClient.getApiService(token);
                String authHeader = "Bearer " + token;
                fetchProfile(authHeader);
            } else {
                Log.e("AUTH", "Token doesn't exist, can't check login.");
                Toast.makeText(this, "No token set", Toast.LENGTH_SHORT).show();
            }
        });

        // button LOG OUT
        logoutButton.setVisibility(View.GONE);
        AppContext.getInstance().getTokenLiveData().observe(this, token -> {
            boolean loggedIn = token != null && !token.isEmpty();

            statusIndicator.setBackground(getDrawable(
                    loggedIn ? R.drawable.indicator_circle_green : R.drawable.indicator_circle_grey
            ));

            if (loggedIn) {
                logoutButton.setVisibility(View.VISIBLE);
            }
        });
        logoutButton.setOnClickListener(v -> {
            AppContext.getInstance().logout();
            logoutButton.setVisibility(View.GONE);
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
        });

        // button REGISTER
        buttonRegister.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        //button DASHBOARD
        buttonDashboard.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
            startActivity(intent);
        });

        // GREEN RED Indicator
        statusIndicator.setBackground(getDrawable(R.drawable.indicator_circle_grey));
        AppContext.getInstance().getTokenLiveData().observe(this, token -> {
            if (token != null && !token.isEmpty()) {
                statusIndicator.setBackground(getDrawable(R.drawable.indicator_circle_green));
            }
        });

        // visibility button change when logged
        AppContext.getInstance().getUserIdLiveData().observe(this, userId -> {
            boolean loggedIn = userId != null && !userId.isEmpty();

            buttonLogin.setVisibility(loggedIn ? View.GONE : View.VISIBLE);
            buttonRegister.setVisibility(loggedIn ? View.GONE : View.VISIBLE);
            buttonDashboard.setVisibility(loggedIn ? View.VISIBLE : View.GONE);
        });
    }
    private void fetchProfile(String authHeader) {
        api.getProfile(authHeader).enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String userId = response.body().userId;
                    String token1 = response.body().token;
                    Boolean isAuth = response.body().isAuth;
                    Toast.makeText(MainActivity.this, "Current user id: " + userId, Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        View statusIndicator = findViewById(R.id.statusIndicator);
                        statusIndicator.setBackground(getDrawable(R.drawable.indicator_circle_red));
                        Toast.makeText(MainActivity.this, "Session expired", Toast.LENGTH_SHORT).show();
                        String errorBody = response.errorBody() != null
                                ? response.errorBody().string()
                                : "no error body";

                        Log.e("API", "Profile error: " + response.code() + "\n" + errorBody);
                    } catch (IOException e) {
                        Log.e("API", "Error reading body on fetch profile", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                View statusIndicator = findViewById(R.id.statusIndicator);
                statusIndicator.setBackground(getDrawable(R.drawable.indicator_circle_red));
                Log.e("API", "Get profile api error", t);
            }
        });
    }
}
