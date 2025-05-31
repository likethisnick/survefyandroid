package com.example.androidsurvefy;

public class LoginViewModel {

    private final IApiService apiService;

    public LoginViewModel(IApiService apiService) {
        this.apiService = apiService;
    }

    public void login(String email, String password) {
        String token = apiService.login(email, password);
        if (token != null && !token.trim().isEmpty()) {
            AppContext.getInstance().setToken(token);
        }
    }
}