package org.iliat.gmat.view_model;

import java.util.List;

/**
 * Created by hungtran on 4/4/16.
 */
public class QuestionReviewViewModel {
    private String stimulus;
    private String stem;
    private List<AnswerChoiceViewModel> answerChoiceViewModels;
    private int userAnswer;
    private int rightAnswer;

    public QuestionReviewViewModel(String stimulus, String stem, List<AnswerChoiceViewModel> answerChoiceViewModels, int userAnswer, int rightAnswer) {
        this.stimulus = stimulus;
        this.stem = stem;
        this.answerChoiceViewModels = answerChoiceViewModels;
        this.userAnswer = userAnswer;
        this.rightAnswer = rightAnswer;
    }

    public String getStimulus() {
        return stimulus;
    }

    public void setStimulus(String stimulus) {
        this.stimulus = stimulus;
    }

    public String getStem() {
        return stem;
    }

    public void setStem(String stem) {
        this.stem = stem;
    }

    public List<AnswerChoiceViewModel> getAnswerChoiceViewModels() {
        return answerChoiceViewModels;
    }

    public void setAnswerChoiceViewModels(List<AnswerChoiceViewModel> answerChoiceViewModels) {
        this.answerChoiceViewModels = answerChoiceViewModels;
    }

    public int getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(int userAnswer) {
        this.userAnswer = userAnswer;
    }

    public int getRightAnswer() {
        return rightAnswer;
    }

    public void setRightAnswer(int rightAnswer) {
        this.rightAnswer = rightAnswer;
    }

    public boolean isCorrect() {
        return rightAnswer == userAnswer;
    }
}


