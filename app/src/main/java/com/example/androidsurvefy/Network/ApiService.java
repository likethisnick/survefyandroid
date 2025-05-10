package com.example.androidsurvefy.Network;

import com.example.androidsurvefy.Model.LoginRequest;
import com.example.androidsurvefy.Model.ProfileResponse;
import com.example.androidsurvefy.Model.TokenResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService {

    @POST("auth/login")
    Call<TokenResponse> login(@Body LoginRequest loginRequest);

    @GET("pingauth")
    Call<ProfileResponse> getProfile(@Header("AUTHORIZATION") String authHeader);
}
