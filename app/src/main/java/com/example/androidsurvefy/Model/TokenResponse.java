package com.example.androidsurvefy.Model;

import com.google.gson.annotations.SerializedName;

public class TokenResponse {
    @SerializedName("token")
    public String token;

    @SerializedName("userId")
    public String userId;

    // и другие поля, если есть
}