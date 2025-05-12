package com.example.androidsurvefy.Model;

import java.util.List;

public class TemplateSurveysResponse {
    private List<TemplateSurveyDto> surveys;

    public List<TemplateSurveyDto> getSurveys() {
        return surveys;
    }

    public void setSurveys(List<TemplateSurveyDto> surveys) {
        this.surveys = surveys;
    }
}
