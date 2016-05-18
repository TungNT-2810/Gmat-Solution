package org.iliat.gmat.view_model;


import android.content.Context;
import android.util.Log;

import org.iliat.gmat.model.QuestionModel;
import org.iliat.gmat.model.QuestionPackModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by qhuydtvt on 4/4/2016.
 */
public class QuestionPackViewModel implements Serializable {
    private List<QuestionViewModel> questionViewModels;
    private boolean isShowDetail;
    private boolean isChecked;

    private QuestionPackModel questionPack;


    public String getId() {
        return questionPack.getId();
    }


    public List<QuestionViewModel> getQuestionViewModels() {
        return questionViewModels;
    }

    public QuestionPackViewModel(Context mContext) {
        questionViewModels = new ArrayList<QuestionViewModel>();
        for(QuestionModel questionModel : questionPack.getQuestionList()){
            questionViewModels.add(new QuestionViewModel(questionModel));
        }
    }

    public QuestionPackViewModel(QuestionPackModel questionPack, Context mContext) {
        this.questionPack = questionPack;
        questionViewModels = new ArrayList<QuestionViewModel>();
        for(QuestionModel questionModel : questionPack.getQuestionList()){
            questionViewModels.add(new QuestionViewModel(questionModel));
        }
    }
    public QuestionPackViewModel(QuestionPackModel questionPack) {
        this.questionPack = questionPack;
        questionViewModels = new ArrayList<QuestionViewModel>();
        for(QuestionModel questionModel : questionPack.getQuestionList()){
            questionViewModels.add(new QuestionViewModel(questionModel));
        }
    }

    public String getAvailableTime() {return questionPack.getAvainableTime();}

    public QuestionPackModel getQuestionPack(){return questionPack;}

    public QuestionViewModel getFirstQuestionViewModel() {
        if(questionViewModels.size() > 0) {
            return questionViewModels.get(0);
        }
        return null;
    }

    public QuestionViewModel getNextQuestionViewModel(QuestionViewModel questionViewModel) {
        int idx = questionViewModels.indexOf(questionViewModel);
        int nextIdx = idx + 1;
        Log.d("NEXT IDX", String.valueOf(nextIdx));
        if(nextIdx < questionViewModels.size()){
            return questionViewModels.get(nextIdx);
        }
        return null;
    }


    public boolean isLastQuestionInPack(QuestionViewModel questionViewModel) {
        Log.d("TAG222",questionViewModel.getStimulus());
        return questionPack.isLastQuestionInPack(questionViewModel.getQuestion());
    }

    public boolean isShowDetail() {
        return isShowDetail;
    }

    public void setIsShowDetail(boolean isShowDetail) {
        this.isShowDetail = isShowDetail;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public int getNumberOfCorrectAnswers() {
        int ret = 0;

        for(QuestionViewModel questionViewModel : questionViewModels) {
            if(questionViewModel.getAnswerStatus() == QuestionViewModel.ANSWER_CORRECT)
                ret++;
        }
        return ret;
    }

    public int getNumberOfQuestions() {
        return questionPack.getNumberOfQuestions();
    }


    public void saveUserAnswers() {
        for(QuestionViewModel questionViewModel : questionViewModels) {
            questionViewModel.saveUserAnswer();
        }
    }

    public void clearUserAnswers() {
        for(QuestionViewModel questionViewModel : questionViewModels) {
            questionViewModel.clearUserAnswer();
        }
    }
}
