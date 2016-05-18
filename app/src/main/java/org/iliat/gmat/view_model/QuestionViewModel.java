package org.iliat.gmat.view_model;

import android.content.Context;
import android.util.Log;

import org.iliat.gmat.database.UserAnswer;
import org.iliat.gmat.model.AnswerModel;
import org.iliat.gmat.model.QuestionModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;


public class QuestionViewModel implements Serializable{
    public static final int CHOICE_INDEX_UNDONE = -1;

    public static final int ANSWER_NOT_DONE = -1;
    public static final int ANSWER_INCORRECT = 0;
    public static final int ANSWER_CORRECT = 1;

    private static final String TAG = QuestionViewModel.class.toString();

    private QuestionModel question;
    //private UserAnswer userAnswer;
    private List<AnswerModel> answerChoices = null;

    public QuestionViewModel(QuestionModel question) {
        this.question = question;
    }

    public AnswerModel getUserAnswer() {
        return answerChoices.get(question.getUserAnswer());
    }

    public int getUserChoise(){
        return question.getUserAnswer();
    }

    public List<AnswerModel> getAnswerChoices() {
        if(answerChoices == null){
            Log.d(TAG, "getAnswerChoices NULL");
            answerChoices = question.getAnswerList();
        }
        return answerChoices;
    }

    public List<AnswerChoiceViewModel> getAnswerChoisesViewModel(){
        if(answerChoices == null){
            Log.d(TAG, "getAnswerChoices NULL");
            answerChoices = question.getAnswerList();
        }
        ArrayList<AnswerChoiceViewModel> arr = new ArrayList<AnswerChoiceViewModel>();
        for (AnswerModel answerModel : answerChoices){
            arr.add(new AnswerChoiceViewModel(answerModel));
        }
        return arr;
    }

    public void setAnswerChoices(List<AnswerModel> answerChoices) {
        this.answerChoices = answerChoices;
    }

    public int getRightAnswer(){
        return question.getRightAnswerIndex();
    }


    public String getStimulus() {return this.question.getStimulus();}

    public String getStem() {return question.getStem();}

    public boolean stemIsEmpty() {
        Log.d(TAG, "STEM: " + String.valueOf(question.getStem()));
        String stem = question.getStem();
        return (stem == null || stem.isEmpty());
    }

    public QuestionModel getQuestion() { return question; }

    public AnswerChoiceViewModel getAnswerChoiceViewModel(int index) {
        if(answerChoices == null){
            Log.d(TAG, "getAnswerChoices NULL");
            answerChoices = question.getAnswerList();
        }
        return new AnswerChoiceViewModel(answerChoices.get(index));
    }

    public int getNumberOfAnswerChoices() {
        if (answerChoices == null){
            Log.d(TAG, "getAnswerChoices NULL");
            answerChoices = question.getAnswerList();
        }
        return answerChoices.size();
    }

    public void selectAnswerChoice(int index) {
        question.setUserAnswer(index);
    }
    public int getAnswerStatus() {
        if(question.getUserAnswer() == CHOICE_INDEX_UNDONE) {
            return ANSWER_NOT_DONE;
        }
        else {
            return question.getUserAnswer() == question.getRightAnswerIndex() ? ANSWER_CORRECT : ANSWER_INCORRECT;
        }
    }

    public void clearUserAnswer() {
        question.setUserAnswer(CHOICE_INDEX_UNDONE);
    }

    public void saveUserAnswer() {
        question.setUserAnswer(question.getUserAnswer());

    }
}
