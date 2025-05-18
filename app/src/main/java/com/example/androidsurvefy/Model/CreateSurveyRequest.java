package com.example.androidsurvefy.Model;

public class CreateSurveyRequest {
    private String name;
    private String description;

    public CreateSurveyRequest(String name, String description) {
        this.name = name;
        this.description = description;
    }
}