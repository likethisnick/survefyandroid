package com.example.androidsurvefy.Model;

public class QuestionOptionDto {
    private String id;
    private String createdOn;
    private String questionId;
    private int questionOptionOrder;
    private String questionOptionText;
    private String templateSurveyId;

    public String getId() { return id; }
    public String getCreatedOn() { return createdOn; }
    public String getQuestionId() { return questionId; }
    public int getQuestionOptionOrder() { return questionOptionOrder; }
    public String getQuestionOptionText() { return questionOptionText; }
    public String getTemplateSurveyId() { return templateSurveyId; }
}