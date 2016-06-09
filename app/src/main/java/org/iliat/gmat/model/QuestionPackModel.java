package org.iliat.gmat.model;


import android.util.Log;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by hungtran on 4/17/16.
 */
public class QuestionPackModel extends RealmObject {
    @PrimaryKey
    private String id;
    private String availableTime;
    private RealmList<QuestionModel> questionList;
    private long totalTimeToFinish = 0;

    public long getTotalTimeToFinish() {
        return totalTimeToFinish;
    }

    public void setTotalTimeToFinish(long totalTimeToFinish) {
        this.totalTimeToFinish = totalTimeToFinish;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAvailableTime() {
        return availableTime;
    }

    public void setAvainableTime(String avainableTime) {
        this.availableTime = avainableTime;
    }

    public RealmList<QuestionModel> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(RealmList<QuestionModel> questionList) {
        this.questionList = questionList;
    }

    public boolean isLastQuestionInPack(QuestionModel question) {
        if (question.getIdInServer().equals(questionList.get(questionList.size() - 1).getIdInServer())) {
            Log.d("questionList.size() - 1", String.valueOf(questionList.size() - 1));
            return true;
        }
        return false;
    }

    public int getNumberOfQuestions() {
        return this.getQuestionList().size();
    }

    public int getNumberOfQuestionAnswered() {
        int count = 0;
        for (int i = 0; i < questionList.size(); i++) {
            if (questionList.get(i).getUserAnswer() != -1) {
                count++;
            }
        }
        return count;
    }
}
