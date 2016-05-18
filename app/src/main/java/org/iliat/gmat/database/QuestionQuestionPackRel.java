package org.iliat.gmat.database;

import com.orm.SugarRecord;

/**
 * Created by qhuydtvt on 4/4/2016.
 */
public class QuestionQuestionPackRel extends SugarRecord {
    private Question question;
    private QuestionPack questionPack;

    public QuestionQuestionPackRel(Question question, QuestionPack questionPack) {
        this.question = question;
        this.questionPack = questionPack;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public QuestionPack getQuestionPack() {
        return questionPack;
    }

    public void setQuestionPack(QuestionPack questionPack) {
        this.questionPack = questionPack;
    }

    public QuestionQuestionPackRel(){}


}
