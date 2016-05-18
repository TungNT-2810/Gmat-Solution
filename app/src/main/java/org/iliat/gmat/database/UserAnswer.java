package org.iliat.gmat.database;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import java.io.Serializable;

/**
 * Created by qhuydtvt on 4/4/2016.
 */
public class UserAnswer extends SugarRecord implements Serializable {

    public static int CHOICE_INDEX_UNDONE = -1;
    private int choiceIndex = CHOICE_INDEX_UNDONE;
    private Question question;

    public UserAnswer() {
        choiceIndex = -1;
    }

    public UserAnswer(Question question) {
        this.question = question;
    }

    public int getChoiceIndex() {
        return choiceIndex;
    }

    public void setChoiceIndex(int choiceIndex) {
        this.choiceIndex = choiceIndex;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
