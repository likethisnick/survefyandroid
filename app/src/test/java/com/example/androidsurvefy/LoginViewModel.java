package com.example.androidsurvefy;

public class LoginViewModel {

    private final IApiService apiService;
    private final TokenStorage tokenStorage;

    public LoginViewModel(IApiService apiService, TokenStorage tokenStorage) {
        this.apiService = apiService;
        this.tokenStorage = tokenStorage;
    }

    public void login(String email, String password) {
        String token = apiService.login(email, password);
        if (token != null && !token.trim().isEmpty()) {
            tokenStorage.setToken(token);
        }
    }
}
