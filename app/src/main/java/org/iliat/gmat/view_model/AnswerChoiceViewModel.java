package org.iliat.gmat.view_model;


import org.iliat.gmat.database.AnswerChoice;
import org.iliat.gmat.model.AnswerModel;

import java.io.Serializable;

/**
 * Created by hungtran on 4/4/16.
 */
public class AnswerChoiceViewModel implements Serializable{

    private AnswerModel answerChoice;

    public AnswerModel getAnswerChoice() {
        return answerChoice;
    }

    public AnswerChoiceViewModel(AnswerModel answerChoice) {
        this.answerChoice = answerChoice;
    }

    public String getChoice() {
        return answerChoice.getChoise();
    }

    public String getExplanation() {
        return answerChoice.getExplanation();
    }

    public int getIndex() {
        return answerChoice.getIndex();
    }
}
