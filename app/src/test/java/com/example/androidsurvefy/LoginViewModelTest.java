package com.example.androidsurvefy;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class LoginViewModelTest {

    private FakeApiService apiService;
    private LoginViewModel viewModel;

    @Before
    public void setUp() {
        apiService = new FakeApiService();
        viewModel = new LoginViewModel(apiService);
        AppContext.getInstance().setToken(null);
    }

    @Test
    public void loginWithValidCredentials_setsAccessToken() {
        viewModel.login("admin2@admin.com", "admin2");

        String token = AppContext.getInstance().getToken();
        assertNotNull("Token must be set after login", token);
        assertFalse("Token must not be empty", token.trim().isEmpty());
        assertEquals("token_admin2", token);
    }
}
