package org.iliat.gmat.view_model;


import android.content.Context;
import android.util.Log;

import org.iliat.gmat.constant.Constant;
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

    public boolean isCompleted(){
        if(questionViewModels.size()>0){
            for(int i=0;i<questionViewModels.size();i++){
                if(questionViewModels.get(i).getQuestion().getUserAnswer()==-1){
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isNew(){
        if(questionViewModels.size()>0 && questionViewModels.get(0).getQuestion().getUserAnswer()==-1){
            return true;
        }
        return false;
    }

    public QuestionViewModel getContinueQuestionViewModel(){
        if(!isCompleted()){
            for(int i=0;i<questionViewModels.size();i++){
                if(questionViewModels.get(i).getQuestion().getUserAnswer()==-1){
                    return questionViewModels.get(i);
                }
            }
        }
        return null;
    }

    public int getNumberQuestionAnswered(){
        if(!isCompleted()){
            for(int i=0;i<questionViewModels.size();i++){
                if(questionViewModels.get(i).getQuestion().getUserAnswer()==-1){
                    return i;
                }
            }
        }
        return 0;
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
        if(questionViewModel!=null) {
            return questionPack.isLastQuestionInPack(questionViewModel.getQuestion());
        }
        return true;
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

    public int getGreyTag(){
        int tag = 0;
        for (int i = 0; i < questionViewModels.size(); i ++){
            if (questionViewModels.get(i).getTag() == Constant.TAG_GREY) tag++;
        }
        return tag;
    }

    public int getGreenTag(){
        int tag = 0;
        for (int i = 0; i < questionViewModels.size(); i ++){
            if (questionViewModels.get(i).getTag() == Constant.TAG_GREEN) tag++;
        }
        return tag;
    }

    public int getYellowTag(){
        int tag = 0;
        for (int i = 0; i < questionViewModels.size(); i ++){
            if (questionViewModels.get(i).getTag() == Constant.TAG_YELLOW) tag++;
        }
        return tag;
    }

    public int getRedTag(){
        int tag = 0;
        for (int i = 0; i < questionViewModels.size(); i ++){
            if (questionViewModels.get(i).getTag() == Constant.TAG_RED) tag++;
        }
        return tag;
    }

    public int getStarTag(){
        int tag = 0;
        for (int i = 0; i < questionViewModels.size(); i ++){
            if (questionViewModels.get(i).isStar() == true) tag++;
        }
        return tag;
    }
}
