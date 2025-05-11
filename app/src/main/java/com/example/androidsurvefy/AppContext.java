package com.example.androidsurvefy;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.LiveData;

public class AppContext extends Application {
    private static AppContext instance;
    private String token;
    private String userId;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static AppContext getInstance() {
        return instance;
    }

    public void setToken(String token) {
        this.token = token;
        tokenLiveData.postValue(token);
    }

    public void setUserId(String userId) {
        this.userId = userId;
        userIdLiveData.postValue(userId);
    }

    public String getToken() {
        return token;
    }

    public String getUserId() {
        return userId;
    }

    public LiveData<String> getUserIdLiveData() {
        return userIdLiveData;
    }

    private final MutableLiveData<String> tokenLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> userIdLiveData = new MutableLiveData<>();

    public LiveData<String> getTokenLiveData() {
        return tokenLiveData;
    }

    public void logout() {
        this.token = null;
        this.userId = null;
        tokenLiveData.postValue(null);
        userIdLiveData.postValue(null);
    }
}
