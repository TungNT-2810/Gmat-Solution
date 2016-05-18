package org.iliat.gmat.enitity;

/**
 * Created by qhuydtvt on 3/19/2016.
 */
public class UserChoice {

    private String questionId;
    private int choice = -1;

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public int getChoice() {
        return choice;
    }

    public void setChoice(int choice) {
        this.choice = choice;
    }
}
