package com.example.androidsurvefy.Model;

public class CreateQuestionOptionRequest {
    private String questionId;
    private int questionOptionOrder;
    private String questionOptionText;
    private String templateSurveyId;

    public String getQuestionId() { return questionId; }
    public void setQuestionId(String questionId) { this.questionId = questionId; }

    public int getQuestionOptionOrder() { return questionOptionOrder; }
    public void setQuestionOptionOrder(int order) { this.questionOptionOrder = order; }

    public String getQuestionOptionText() { return questionOptionText; }
    public void setQuestionOptionText(String text) { this.questionOptionText = text; }

    public String getTemplateSurveyId() { return templateSurveyId; }
    public void setTemplateSurveyId(String templateSurveyId) { this.templateSurveyId = templateSurveyId; }
}
