package com.example.androidsurvefy.Model;

public class UpdateQuestionOptionRequest {
    private String id;
    private String questionOptionText;
    private int questionOptionOrder;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getQuestionOptionText() { return questionOptionText; }
    public void setQuestionOptionText(String questionOptionText) { this.questionOptionText = questionOptionText; }

    public int getQuestionOptionOrder() { return questionOptionOrder; }
    public void setQuestionOptionOrder(int questionOptionOrder) { this.questionOptionOrder = questionOptionOrder; }
}
