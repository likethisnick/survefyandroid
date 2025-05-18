package com.example.androidsurvefy.Model;

public class QuestionDto {
    private String id;
    private String createdOn;
    private String questionTypeId;
    private String templateSurveyId;
    private int questionOrder;
    private String questionText;

    public String getId() {
        return id;
    }
    public String getCreatedOn() {
        return createdOn;
    }

    public String getQuestionTypeId() {
        return questionTypeId;
    }

    public String getTemplateSurveyId() {
        return templateSurveyId;
    }

    public int getQuestionOrder() {
        return questionOrder;
    }

    public String getQuestionText() {
        return questionText;
    }
}