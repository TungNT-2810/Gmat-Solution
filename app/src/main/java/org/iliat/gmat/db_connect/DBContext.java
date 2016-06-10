package org.iliat.gmat.db_connect;

import org.iliat.gmat.model.QuestionModel;
import org.iliat.gmat.model.QuestionTypeModel;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by MrBom on 6/10/2016.
 */
public class DBContext {
    private static Realm realm=Realm.getDefaultInstance();

    public static int getNumberQuestionBySubTypeCode(String code){
        return realm.where(QuestionModel.class).equalTo("subType",code).distinct("id").size();
    }

    public static int getNumberQuestionByTypeSubTypeCode(String type, String subType) {
        return realm.where(QuestionModel.class).equalTo("type", type)
                .equalTo("subType",subType).distinct("id").size();
    }
    public static int getNumberQuestionAnsweredBySubTypeCode(String code){
        return realm.where(QuestionModel.class).equalTo("subType",code).notEqualTo("userAnswer",-1)
                .distinct("id").size();
    }

    public static QuestionTypeModel getQuestionTypeByCode(String code) {
        return realm.where(QuestionTypeModel.class).equalTo("code", code).findFirst();
    }

    public static int getNumberQustionAnsweredByTypeAndSubType(String type, String subType){
        return realm.where(QuestionModel.class).equalTo("type",type).equalTo("subType",subType)
                .notEqualTo("userAnswer",-1).distinct("id").size();
    }

    public static int getNumberQustionByType(String type) {
        return realm.where(QuestionModel.class).equalTo("type", type).distinct("id").size();
    }

    public static int getNumberCorrectByTypeAndSubType(String type, String subType) {
        RealmResults<QuestionModel> list= realm.where(QuestionModel.class).equalTo("type", type)
                .equalTo("subType", subType).findAll().distinct("id");
        int count=0;
        for(QuestionModel q : list){
            if(q.getUserAnswer()==q.getRightAnswerIndex()){
                count++;
            }
        }
        return count;
    }

    public static int getNumberCorrectByType(String type) {
        RealmResults<QuestionModel> list = realm.where(QuestionModel.class).equalTo("type", type)
                .findAll().distinct("id");
        int count = 0;
        for (QuestionModel q : list) {
            if (q.getUserAnswer() == q.getRightAnswerIndex()) {
                count++;
            }
        }
        return count;
    }

}
