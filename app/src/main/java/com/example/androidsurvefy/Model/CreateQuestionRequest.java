package com.example.androidsurvefy.Model;

public class CreateQuestionRequest {
    private String templateSurveyId;
    private String questionTypeId;
    private int questionOrder;
    private String questionText;
    public String getTemplateSurveyId() { return templateSurveyId; }
    public void setTemplateSurveyId(String id) { this.templateSurveyId = id; }

    public String getQuestionTypeId() { return questionTypeId; }
    public void setQuestionTypeId(String id) { this.questionTypeId = id; }

    public int getQuestionOrder() { return questionOrder; }
    public void setQuestionOrder(int order) { this.questionOrder = order; }

    public String getQuestionText() { return questionText; }
    public void setQuestionText(String text) { this.questionText = text; }
}
