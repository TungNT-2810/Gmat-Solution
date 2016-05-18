package org.iliat.gmat.model;


import android.util.Log;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by hungtran on 4/17/16.
 */
public class QuestionPackModel extends RealmObject {
    @PrimaryKey
    private String id;
    private String avainableTime;
    private RealmList<QuestionModel> questionList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAvainableTime() {
        return avainableTime;
    }

    public void setAvainableTime(String avainableTime) {
        this.avainableTime = avainableTime;
    }

    public RealmList<QuestionModel> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(RealmList<QuestionModel> questionList) {
        this.questionList = questionList;
    }

    public boolean isLastQuestionInPack(QuestionModel question) {
        if(question.getIdInServer().equals(questionList.get(questionList.size() - 1).getIdInServer())){
            Log.d("questionList.size() - 1",String.valueOf(questionList.size() - 1));
            return true;
        }
        return false;
    }

    public int getNumberOfQuestions() {
        return this.getQuestionList().size();
    }
}
