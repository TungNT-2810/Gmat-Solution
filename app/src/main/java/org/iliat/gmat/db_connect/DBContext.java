package org.iliat.gmat.db_connect;

import org.iliat.gmat.constant.Constant;
import org.iliat.gmat.model.QuestionModel;
import org.iliat.gmat.model.QuestionPackModel;
import org.iliat.gmat.model.QuestionSubTypeModel;
import org.iliat.gmat.model.QuestionTypeModel;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by MrBom on 6/10/2016.
 */
public class DBContext {
    private static Realm realm = Realm.getDefaultInstance();

    public static void updateStarForEachQuestion(QuestionModel questionModel){
        realm.beginTransaction();
        questionModel.setStar(!questionModel.isStar());
        realm.copyToRealmOrUpdate(questionModel);
        realm.commitTransaction();
    }

    public static void updateTagForEachQuestion(QuestionModel questionModel, int tagId){
        realm.beginTransaction();
        questionModel.setTagId(tagId);
        realm.copyToRealmOrUpdate(questionModel);
        realm.commitTransaction();
    }

    public static int getNumberQuestionBySubTypeCode(String code) {
        return realm.where(QuestionModel.class).equalTo("subType", code).distinct("id").size();
    }

    public static int getNumberQuestionByTypeAndSubTypeCode(String type, String subType) {
        return realm.where(QuestionModel.class).equalTo("type", type)
                .equalTo("subType", subType).distinct("id").size();
    }

    public static int getNumberQuestionAnsweredBySubTypeCode(String code) {
        return realm.where(QuestionModel.class).equalTo("subType", code).notEqualTo("userAnswer", -1)
                .distinct("id").size();
    }

    public static QuestionTypeModel getQuestionTypeByCode(String code) {
        return realm.where(QuestionTypeModel.class).equalTo("code", code).findFirst();
    }

    public static int getNumberQustionAnsweredByTypeAndSubType(String type, String subType) {
        return realm.where(QuestionModel.class).equalTo("type", type).equalTo("subType", subType)
                .notEqualTo("userAnswer", -1).distinct("id").size();
    }

    public static int getNumberQustionByType(String type) {
        return realm.where(QuestionModel.class).equalTo("type", type).distinct("id").size();
    }

    public static RealmResults<QuestionModel> getAllQuestionAnsweredByTypeAndSubType(String type, String subType) {
        return realm.where(QuestionModel.class).equalTo("type", type).equalTo("subType", subType)
                .notEqualTo("userAnswer", -1).findAll().distinct("id");
    }

    public static int getNumberCorrectByTypeAndSubType(String type, String subType) {
        RealmResults<QuestionModel> list = realm.where(QuestionModel.class).equalTo("type", type)
                .equalTo("subType", subType).findAll().distinct("id");
        int count = 0;
        for (QuestionModel q : list) {
            if (q.getUserAnswer() == q.getRightAnswerIndex()) {
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

    public static int getNumberOfOtherQuestionByType(String type, RealmList<QuestionSubTypeModel> list) {
        int count = 0;
        for (int i = 0; i < list.size(); i++) {
            count += realm.where(QuestionModel.class).equalTo("type", type)
                    .equalTo("subType", list.get(i).getCode()).count();
        }
        return getNumberQustionByType(type) - count;
    }

    public static int getNumberOfTagByTagId(int id, String type, String subType) {
        return (int) realm.where(QuestionModel.class).equalTo("type", type).equalTo("subType", subType)
                .equalTo("tagId", id).notEqualTo("userAnswer", -1).count();
    }

    public static int getNumberOfStar(boolean isStar, String type, String subType) {
        return (int) realm.where(QuestionModel.class).equalTo("type", type).equalTo("subType", subType)
                .equalTo("isStar", isStar).notEqualTo("userAnswer", -1).count();
    }

    public static RealmList<QuestionModel> getAllQuestionModelByPackId(String id) {
        return realm.where(QuestionPackModel.class).equalTo("id", id).findFirst().getQuestionList();
    }

    public static RealmResults<QuestionModel> getAllQuestionAnsweredByTagId(int id) {
        if(id== Constant.TAG_STAR){
            return realm.where(QuestionModel.class).notEqualTo("userAnswer",-1).equalTo("isStar", true).findAll();
        }
        return realm.where(QuestionModel.class).notEqualTo("userAnswer",-1).equalTo("tagId", id).findAll();
    }

}
