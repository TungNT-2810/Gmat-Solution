package org.iliat.gmat.database;

import android.util.Log;

import com.orm.SugarRecord;

import org.iliat.gmat.view_model.QuestionPackViewModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qhuydtvt on 4/4/2016.
 */
public class QuestionPack extends SugarRecord implements Serializable{
    private static final String TAG = QuestionPack.class.toString();

    private Long id;
    private String idInServer;
    private String availableTime;
    private List<Question> questionList;

    public QuestionPack(String idInServer, String availableTime) {
        this.idInServer = idInServer;
        this.availableTime = availableTime;
    }

    public QuestionPack(){}

    public String getIdInServer() {
        return idInServer;
    }

    public void setIdInServer(String idInServer) {
        this.idInServer = idInServer;
    }

    public String getAvailableTime() {
        return availableTime;
    }

    public void setAvailableTime(String availableTime) {
        this.availableTime = availableTime;
    }

    private void initQuestionList() {
        if(questionList == null) {
            questionList = new ArrayList<>();

            List<QuestionQuestionPackRel> questionQuestionPackRelList =
                    QuestionQuestionPackRel.find(QuestionQuestionPackRel.class,
                            "QUESTION_PACK = ?",
                            String.valueOf(this.getId()));

            for (QuestionQuestionPackRel qqpRel : questionQuestionPackRelList) {
                questionList.add(qqpRel.getQuestion());
            }
            Log.d(TAG, String.valueOf(questionList.size()));
        }
    }

    public List<Question> getQuestionList() {
        initQuestionList();
        return questionList;
    }

    public int getNumberOfQuestions() {
        initQuestionList();
        return questionList.size();
    }

    public Question getFirstQuestion() {
        initQuestionList();
        if(questionList.size() > 0) return questionList.get(0);
        return null;
    }

    public boolean isLastQuestionInPack(Question question) {
        initQuestionList();
        if (questionList.size() > 0) {
            Question lastQuestion = questionList.get(questionList.size() - 1);
            if (lastQuestion.getId() == question.getId()) {
                return true;
            }
            return  false;
        } else {
            return true;
        }
    }

    public Question getNextQuestion(Question question) {
        initQuestionList();
        int index = this.questionList.indexOf(question);
        if(index < questionList.size() - 1) return questionList.get(index + 1);
        return null;
    }

    public static List<QuestionPack> getAllQuestionPacks() {
        return QuestionPack.listAll(QuestionPack.class);
    }
}
