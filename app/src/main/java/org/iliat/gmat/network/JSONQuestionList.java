package org.iliat.gmat.network;

import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

import java.util.List;

public class JSONQuestionList extends SugarRecord {

    private static final String VERSION = "version";
    private static final String QUESTIONS = "questions";


    @SerializedName(QUESTIONS)
    private List<JSONQuestion> list;



    public List<JSONQuestion> getList() {
        return list;
    }

//    public static QuestionModel getQuestion(String oid) {
//        Log.d("getQuestion - match", oid);
//        for(QuestionModel questionModel : inst.getList()) {
//            Log.d("getQuestion", questionModel.getId());
//            if(questionModel.getId().equals(oid)) {
//                return questionModel;
//            }
//        }
//        return null;
//    }

//    private static JSONQuestionList inst;
//
//    public static JSONQuestionList getInst() {
//        if(inst == null) {
//            Log.d("getInst", "inst == null");
//        }
//        return inst;
//    }
//
//    public static void loadQuestionList(InputStreamReader reader) {
//        inst = (new Gson()).fromJson(reader, JSONQuestionList.class);
//    }
}
