package com.example.androidsurvefy;

public class InMemoryTokenStorage implements TokenStorage {
    private String token;

    @Override
    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String getToken() {
        return token;
    }
}