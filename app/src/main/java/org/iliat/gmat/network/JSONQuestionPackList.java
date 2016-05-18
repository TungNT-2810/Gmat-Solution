package org.iliat.gmat.network;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by qhuydtvt on 3/14/2016.
 */
public class JSONQuestionPackList {

    @SerializedName("question_packs")
    private List<JSONQuestionPack> list;

    public List<JSONQuestionPack> getList() {
        return list;
    }

//    private static JSONQuestionPackList inst;
//
//    public static JSONQuestionPackList getInst() {
//        if(inst == null) {
//            Log.d("getInst", "inst == null");
//        }
//        return inst;
//    }
//
//    public static void loadFromJson(InputStreamReader reader) {
//        inst = (new Gson()).fromJson(reader, JSONQuestionPackList.class);
//    }
//
//    private static List<QuestionPack> todayQuestionPack;
}
