package com.example.androidsurvefy.Network;

import com.example.androidsurvefy.Model.CreateQuestionOptionRequest;
import com.example.androidsurvefy.Model.CreateQuestionRequest;
import com.example.androidsurvefy.Model.CreateQuestionResponse;
import com.example.androidsurvefy.Model.CreateSurveyRequest;
import com.example.androidsurvefy.Model.CreateSurveyResponse;
import com.example.androidsurvefy.Model.LoginRequest;
import com.example.androidsurvefy.Model.ProfileResponse;
import com.example.androidsurvefy.Model.QuestionDto;
import com.example.androidsurvefy.Model.QuestionOptionDto;
import com.example.androidsurvefy.Model.TemplateSurveysResponse;
import com.example.androidsurvefy.Model.TokenResponse;
import com.example.androidsurvefy.Model.UpdateQuestionOptionRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @POST("auth/login")
    Call<TokenResponse> login(@Body LoginRequest loginRequest);
    @GET("pingauth")
    Call<ProfileResponse> getProfile(@Header("AUTHORIZATION") String authHeader);
    @POST("/auth/register")
    Call<TokenResponse> register(@Body LoginRequest request);
    @POST("NewSurvey")
    Call<CreateSurveyResponse> createTemplateSurvey(@Body CreateSurveyRequest request);
    @GET("TemplateSurveys")
    Call<TemplateSurveysResponse> getSurveys(@Query("CreatedByUserId") String userId);
    @GET("TemplateSurveys/all")
    Call<TemplateSurveysResponse> getAllSurveys();
    @GET("Questions")
    Call<List<QuestionDto>> getQuestionsBySurvey(@Query("templateSurveyId") String templateSurveyId);
    @GET("QuestionOptions")
    Call<List<QuestionOptionDto>> getOptions(@Query("templateSurveyId") String templateSurveyId);
    @POST("Questions")
    Call<CreateQuestionResponse> createQuestion(@Body CreateQuestionRequest request);
    @DELETE("questions/{id}")
    Call<Void> deleteQuestion(@Path("id") String questionId);
    @POST("Questions/options")
    Call<Void> createQuestionOption(@Body CreateQuestionOptionRequest request);
    @DELETE("questions/options/{id}")
    Call<Void> deleteQuestionOption(@Path("id") String optionId);
    @PUT("questions/options")
    Call<Void> updateQuestionOption(@Body UpdateQuestionOptionRequest request);
}