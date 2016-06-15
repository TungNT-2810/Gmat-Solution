package org.iliat.gmat.utils;


import android.util.Log;

import com.google.gson.Gson;

import org.iliat.gmat.db_connect.DBContext;
import org.iliat.gmat.interf.OnDownloadFinished;
import org.iliat.gmat.network.APIUrls;
import org.iliat.gmat.network.JSONQuestionList;
import org.iliat.gmat.network.JSONQuestionPackList;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class QuestionHelper {
    private OnDownloadFinished onDownloadFinished;

    public void downloadQuestionPacks() {
        String url = APIUrls.QUESTION_PACKS_API;
        Request request = new Request.Builder()
                .url(url)
                .build();
        OkHttpClient mHttpClient = new OkHttpClient();
        mHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("FUCK", "MAI LAI 1");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                JSONQuestionPackList jsonQuestionPackList = (
                        new Gson()).fromJson(response.body().charStream(),
                        JSONQuestionPackList.class);
                DBContext.getInst().saveQuestionPacks(jsonQuestionPackList);
                if (onDownloadFinished != null) {
                    onDownloadFinished.downloadFinish();
                }
            }
        });
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
                Log.d("FUCK", "MAI LAI 1");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                JSONQuestionList jsonQuestionList = (
                        new Gson()).fromJson(response.body().charStream(),
                        JSONQuestionList.class);
                DBContext.getInst().saveQuestions(jsonQuestionList);
                downloadQuestionPacks();
            }
        });
    }


    public void setOnDownloadFinished(OnDownloadFinished onDownloadFinished) {
        this.onDownloadFinished = onDownloadFinished;
    }
}
