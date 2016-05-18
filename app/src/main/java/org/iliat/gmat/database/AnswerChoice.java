package org.iliat.gmat.database;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import java.io.Serializable;

/**
 * Created by qhuydtvt on 4/4/2016.
 */
public class AnswerChoice extends SugarRecord implements Serializable {
    private int idx;
    private String choice;
    private String explanation;

//    Link to question
    private Question question;

    public AnswerChoice(){}

    public AnswerChoice(int idx, String choice, String explanation, Question question) {
        this.idx = idx;
        this.choice = choice;
        this.explanation = explanation;
        this.question = question;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getChoice() {
        return choice;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}