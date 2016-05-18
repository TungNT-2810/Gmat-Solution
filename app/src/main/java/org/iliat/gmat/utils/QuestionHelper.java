package org.iliat.gmat.utils;


import android.util.Log;

import com.google.gson.Gson;

import org.iliat.gmat.model.AnswerModel;
import org.iliat.gmat.model.QuestionModel;
import org.iliat.gmat.model.QuestionPackModel;
import org.iliat.gmat.network.APIUrls;
import org.iliat.gmat.network.JSONAnswerChoice;
import org.iliat.gmat.network.JSONQuestion;
import org.iliat.gmat.network.JSONQuestionList;
import org.iliat.gmat.network.JSONQuestionPack;
import org.iliat.gmat.network.JSONQuestionPackList;
import org.iliat.gmat.interf.OnDownloadFinished;

import java.io.IOException;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by hungtran on 5/4/16.
 */
public class QuestionHelper {
    private OnDownloadFinished onDownloadFinished;

    private Realm realm;

    public void setRealm(Realm realm) {
        this.realm = realm;
    }

    public void downloadQuestionPacks(){
        String url = APIUrls.QUESTION_PACKS_API;
        Request request = new Request.Builder()
                .url(url)
                .build();
        OkHttpClient mHttpClient = new OkHttpClient();
        mHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("FUCK","MAI LAI 1");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                JSONQuestionPackList jsonQuestionPackList = (
                        new Gson()).fromJson(response.body().charStream(),
                        JSONQuestionPackList.class);
                saveQuestionPacks(jsonQuestionPackList);
                if (onDownloadFinished != null){
                    onDownloadFinished.downloadFinish();
                }
            }
        });
    }

    private void saveQuestionPacks(JSONQuestionPackList jsonQuestionPackList) {
        Log.d("QuestionHelper", "JSONQuestionPackSize: " + String.valueOf(jsonQuestionPackList.getList().size()));
        for(JSONQuestionPack jsonQuestionPack : jsonQuestionPackList.getList()) {
            realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            if (realm.where(QuestionPackModel.class).equalTo("id", jsonQuestionPack.getId()).findFirst() == null){
                QuestionPackModel questionPackModel = new QuestionPackModel();
                questionPackModel.setAvainableTime(jsonQuestionPack.getAvailableTime());
                questionPackModel.setId(jsonQuestionPack.getId());
                questionPackModel.setQuestionList(new RealmList<QuestionModel>());
                for(String id : jsonQuestionPack.getQuestionIds()) {
                    QuestionModel questionModel = realm.where(QuestionModel.class).equalTo("idInServer", id).findFirst();
                    questionPackModel.getQuestionList().add(questionModel);
                }
                realm.copyToRealmOrUpdate(questionPackModel);
            }
            realm.commitTransaction();
        }
    }

    public void downloadQuestionInServer() {
        String url = APIUrls.QUESTIONS_API;
        Request request = new Request.Builder()
                .url(url)
                .build();
        OkHttpClient mHttpClient = new OkHttpClient();
        mHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("FUCK","MAI LAI 1");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                JSONQuestionList jsonQuestionList = (
                        new Gson()).fromJson(response.body().charStream(),
                        JSONQuestionList.class);

                saveQuestions(jsonQuestionList);
                downloadQuestionPacks();
            }
        });
    }
    private void saveQuestions(JSONQuestionList jsonQuestionList) {
        realm = Realm.getDefaultInstance();
        for (JSONQuestion jsonQuestion : jsonQuestionList.getList()) {
            {
                realm.beginTransaction();
                if (realm.where(QuestionModel.class).equalTo("id", jsonQuestion.getId()).findFirst() == null){
                    RealmList<AnswerModel> answerModels = new RealmList<AnswerModel>();
                    for (JSONAnswerChoice jsonAnswerChoice : jsonQuestion.getAnswerChoiceList()) {
                        AnswerModel answerModel = new AnswerModel();
                        answerModel.setId(jsonQuestion.getId() + jsonAnswerChoice.getIndex());
                        answerModel.setChoise(jsonAnswerChoice.getChoice());
                        answerModel.setExplanation(jsonAnswerChoice.getExplanation());
                        answerModel.setIndex(jsonAnswerChoice.getIndex());
                        answerModels.add(answerModel);
                    }
                    QuestionModel questionModel = new QuestionModel();
                    questionModel.setId(jsonQuestion.getId());
                    questionModel.setIdInServer(jsonQuestion.getId());
                    questionModel.setType(jsonQuestion.getType());
                    questionModel.setSubType(jsonQuestion.getSubType());
                    questionModel.setStimulus(jsonQuestion.getStimulus());
                    questionModel.setStem(jsonQuestion.getStem());
                    questionModel.setRightAnswerIndex(jsonQuestion.getRightAnswer());
                    questionModel.setAnswerList(answerModels);
                    realm.copyToRealmOrUpdate(questionModel);
                }
                realm.commitTransaction();
            }
        }
    }

    public void setOnDownloadFinished(OnDownloadFinished onDownloadFinished) {
        this.onDownloadFinished = onDownloadFinished;
    }
}
