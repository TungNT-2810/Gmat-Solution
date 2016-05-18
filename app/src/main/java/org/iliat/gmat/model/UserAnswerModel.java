package org.iliat.gmat.model;


/**
 * Created by hungtran on 4/19/16.
 */
public class UserAnswerModel {
    public static int CHOICE_INDEX_UNDONE = -1;
    private int choiceIndex = CHOICE_INDEX_UNDONE;
    private QuestionModel question;

    public UserAnswerModel() {
        choiceIndex = CHOICE_INDEX_UNDONE;
    }

    public UserAnswerModel(int choiceIndex, QuestionModel question) {
        this.choiceIndex = choiceIndex;
        this.question = question;
    }

    public int getChoiceIndex() {
        return choiceIndex;
    }

    public void setChoiceIndex(int choiceIndex) {
        this.choiceIndex = choiceIndex;
    }

    public QuestionModel getQuestion() {
        return question;
    }

    public void setQuestion(QuestionModel question) {
        this.question = question;
    }
}

