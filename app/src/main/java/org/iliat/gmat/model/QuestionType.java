package org.iliat.gmat.model;


import java.io.Serializable;

/**
 * Created by MrBom on 5/13/2016.
 */
public class QuestionType implements Serializable{
    private String code;
    private String typeName;
    private int totalQuestion;
    private int totalRightAnswer;
    private int totalAnswer;

    public QuestionType(String code ,String typeName, int totalQuestion, int totalAnswer, int totalRightAnswer) {
        this.code=code;
        this.typeName = typeName;
        this.totalQuestion = totalQuestion;
        this.totalRightAnswer = totalRightAnswer;
        this.totalAnswer=totalAnswer;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getTotalQuestion() {
        return totalQuestion;
    }

    public void setTotalQuestion(int totalQuestion) {
        this.totalQuestion = totalQuestion;
    }

    public int getTotalRightAnswer() {
        return totalRightAnswer;
    }

    public void setTotalRightAnswer(int totalRightAnswer) {
        this.totalRightAnswer = totalRightAnswer;
    }

    public int getTotalAnswer() {
        return totalAnswer;
    }

    public void setTotalAnswer(int totalAnswer) {
        this.totalAnswer = totalAnswer;
    }
}
