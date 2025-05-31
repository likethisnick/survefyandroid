package com.example.androidsurvefy;

public class FakeApiService implements IApiService {
    @Override
    public String login(String email, String password) {
        if ("admin2@admin.com".equals(email) && "admin2".equals(password)) {
            return "token_admin2";
        }
        return null;
    }
}
