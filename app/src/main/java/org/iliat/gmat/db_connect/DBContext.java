package org.iliat.gmat.db_connect;

import org.iliat.gmat.constant.Constant;
import org.iliat.gmat.model.AnswerModel;
import org.iliat.gmat.model.QuestionModel;
import org.iliat.gmat.model.QuestionPackModel;
import org.iliat.gmat.model.QuestionSubTypeModel;
import org.iliat.gmat.model.QuestionTypeModel;
import org.iliat.gmat.network.JSONAnswerChoice;
import org.iliat.gmat.network.JSONQuestion;
import org.iliat.gmat.network.JSONQuestionList;
import org.iliat.gmat.network.JSONQuestionPack;
import org.iliat.gmat.network.JSONQuestionPackList;
import org.iliat.gmat.network.JSONQuestionSubType;
import org.iliat.gmat.network.JSONQuestionType;
import org.iliat.gmat.network.JSONQuestionTypeList;
import org.iliat.gmat.view_model.QuestionPackViewModel;
import org.iliat.gmat.view_model.QuestionViewModel;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by MrBom on 6/10/2016.
 */
public class DBContext {
    private Realm realm;

    private DBContext() {
        realm = Realm.getDefaultInstance();
    }

    //singleton
    private static DBContext inst;

    public static DBContext getInst() {
        if (inst == null) {
            inst = new DBContext();
        }
        return inst;
    }

    public void updateStarForEachQuestion(QuestionModel questionModel) {
        realm.beginTransaction();
        questionModel.setStar(!questionModel.isStar());
        realm.copyToRealmOrUpdate(questionModel);
        realm.commitTransaction();
    }

    public void updateTagForEachQuestion(QuestionModel questionModel, int tagId) {
        realm.beginTransaction();
        questionModel.setTagId(tagId);
        realm.copyToRealmOrUpdate(questionModel);
        realm.commitTransaction();
    }

    public int getNumberQuestionBySubTypeCode(String code) {
        return realm.where(QuestionModel.class).equalTo("subType", code).distinct("id").size();
    }

    public int getNumberQuestionByTypeAndSubTypeCode(String type, String subType) {
        return realm.where(QuestionModel.class).equalTo("type", type)
                .equalTo("subType", subType).distinct("id").size();
    }

    public int getNumberQuestionAnsweredBySubTypeCode(String code) {
        return (int) realm.where(QuestionModel.class).equalTo("subType", code).notEqualTo("userAnswer", -1)
                .count();
    }

    public int getNumberQuestionAnsweredByTypeCode(String code) {
        return (int) realm.where(QuestionModel.class).equalTo("type", code).notEqualTo("userAnswer", -1)
                .count();
    }

    public QuestionTypeModel getQuestionTypeByCode(String code) {
        return realm.where(QuestionTypeModel.class).equalTo("code", code).findFirst();
    }

    public int getNumberQustionAnsweredByTypeAndSubType(String type, String subType) {
        return realm.where(QuestionModel.class).equalTo("type", type).equalTo("subType", subType)
                .notEqualTo("userAnswer", -1).distinct("id").size();
    }

    public int getNumberQustionByType(String type) {
        return realm.where(QuestionModel.class).equalTo("type", type).distinct("id").size();
    }

    public RealmResults<QuestionModel> getAllQuestionAnsweredByTypeAndSubType(String type, String subType) {
        return realm.where(QuestionModel.class).equalTo("type", type).equalTo("subType", subType)
                .notEqualTo("userAnswer", -1).findAll().distinct("id");
    }

    public int getNumberCorrectByTypeAndSubType(String type, String subType) {
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

    public int getNumberCorrectQuestion() {
        RealmResults<QuestionModel> list = realm.where(QuestionModel.class).findAll().distinct("id");
        int count = 0;
        for (QuestionModel q : list) {
            if (q.getUserAnswer() == q.getRightAnswerIndex()) {
                count++;
            }
        }
        return count;
    }

    public int getNumberCorrectByType(String type) {
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

    public int getNumberOfOtherQuestionByType(String type, RealmList<QuestionSubTypeModel> list) {
        int count = 0;
        for (int i = 0; i < list.size(); i++) {
            count += realm.where(QuestionModel.class).equalTo("type", type)
                    .equalTo("subType", list.get(i).getCode()).count();
        }
        return getNumberQustionByType(type) - count;
    }

    public int getNumberOfTagByTagId(int id, String type, String subType) {
        return (int) realm.where(QuestionModel.class).equalTo("type", type).equalTo("subType", subType)
                .equalTo("tagId", id).notEqualTo("userAnswer", -1).count();
    }

    public int getNumberOfStar(boolean isStar, String type, String subType) {
        return (int) realm.where(QuestionModel.class).equalTo("type", type).equalTo("subType", subType)
                .equalTo("isStar", isStar).notEqualTo("userAnswer", -1).count();
    }

    public RealmList<QuestionModel> getAllQuestionModelByPackId(String id) {
        return realm.where(QuestionPackModel.class).equalTo("id", id).findFirst().getQuestionList();
    }

    public RealmResults<QuestionModel> getAllQuestionAnsweredByTagId(int id) {
        if (id == Constant.TAG_STAR) {
            return realm.where(QuestionModel.class).notEqualTo("userAnswer", -1).equalTo("isStar", true).findAll();
        }
        return realm.where(QuestionModel.class).notEqualTo("userAnswer", -1).equalTo("tagId", id).findAll();
    }

    public QuestionPackModel getQuestionPackModelById(String id) {
        return realm.where(QuestionPackModel.class).equalTo("id", id).findFirst();
    }

    public void saveTotalTimeToFinish(QuestionPackViewModel questionPackViewModel, long totalTime) {
        realm.beginTransaction();
        questionPackViewModel.setTotalTimeToFinish(totalTime);
        realm.copyToRealmOrUpdate(questionPackViewModel.getQuestionPack());
        realm.commitTransaction();
    }

    public void saveTimeFinishForEachQuestion(QuestionViewModel questionViewModel, long time) {
        realm.beginTransaction();
        questionViewModel.getQuestion().setTimeToFinish((int) time);
        realm.copyToRealmOrUpdate(questionViewModel.getQuestion());
        realm.commitTransaction();
    }

    public void saveTagForEachQuestion(QuestionViewModel questionViewModel, int tagId) {
        realm.beginTransaction();
        questionViewModel.getQuestion().setTagId(Constant.TAG_GREY);
        realm.copyToRealmOrUpdate(questionViewModel.getQuestion());
        realm.commitTransaction();
    }

    public List<QuestionPackModel> getAllQuestionPack() {
        return realm.where(QuestionPackModel.class).findAllSorted("availableTime", Sort.ASCENDING);
    }

    public int getNumberOfQuestion() {
        return (int) realm.where(QuestionModel.class).count();
    }

    public int getNumberOfQuestionAnswered() {
        return (int) realm.where(QuestionModel.class).notEqualTo("userAnswer", -1).count();
    }

    public RealmResults<QuestionTypeModel> getAllQuestiontype() {
        return realm.where(QuestionTypeModel.class).findAll();
    }

    public RealmResults<QuestionModel> getAllQuestionByType(String typeCode) {
        return realm.where(QuestionModel.class).equalTo("type", typeCode).findAll();
    }

    public int getNumberOfQuestionByTagId(int tagId) {
        if (tagId == Constant.TAG_STAR) {
            return (int) realm.where(QuestionModel.class).equalTo("isStar", true).count();
        }
        return (int) realm.where(QuestionModel.class).equalTo("tagId", tagId).count();
    }

    public long getTotalTime() {
        return (long) realm.where(QuestionModel.class).sum("timeToFinish");
    }

    public void saveQuestionPacks(JSONQuestionPackList jsonQuestionPackList) {
        for (JSONQuestionPack jsonQuestionPack : jsonQuestionPackList.getList()) {
            realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            if (getQuestionPackModelById(jsonQuestionPack.getId()) == null) {
                QuestionPackModel questionPackModel = new QuestionPackModel();
                questionPackModel.setAvainableTime(jsonQuestionPack.getAvailableTime());
                questionPackModel.setId(jsonQuestionPack.getId());
                questionPackModel.setQuestionList(new RealmList<QuestionModel>());
                for (String id : jsonQuestionPack.getQuestionIds()) {
                    QuestionModel questionModel = realm.where(QuestionModel.class).equalTo("idInServer", id).findFirst();
                    questionPackModel.getQuestionList().add(questionModel);
                }
                realm.copyToRealmOrUpdate(questionPackModel);
            }
            realm.commitTransaction();
        }
    }

    public void saveQuestionType(JSONQuestionTypeList jsonQuestionTypeList) {
        realm = Realm.getDefaultInstance();
        RealmList<QuestionSubTypeModel> subTypeList = new RealmList<>();
        for (JSONQuestionType jsonQuestionType : jsonQuestionTypeList.getList()) {
            subTypeList.clear();
            realm.beginTransaction();
            List<JSONQuestionSubType> jsonQuestionSubTypeList = jsonQuestionType.getSubTypeList();
            if (jsonQuestionSubTypeList != null && jsonQuestionSubTypeList.size() > 0) {
                for (JSONQuestionSubType jsonQuestionSubType : jsonQuestionSubTypeList) {
                    QuestionSubTypeModel questionSubTypeModel = new QuestionSubTypeModel();
                    questionSubTypeModel.setCode(jsonQuestionSubType.getCode());
                    questionSubTypeModel.setDetail(jsonQuestionSubType.getDetail());
                    subTypeList.add(questionSubTypeModel);
                }
            }
            QuestionTypeModel questionTypeModel = new QuestionTypeModel();
            questionTypeModel.setCode(jsonQuestionType.getCode());
            questionTypeModel.setDetail(jsonQuestionType.getDetail());
            questionTypeModel.setListSubType(subTypeList);
            realm.copyToRealmOrUpdate(questionTypeModel);
            realm.commitTransaction();
        }
    }

    public void saveQuestions(JSONQuestionList jsonQuestionList) {
        for (JSONQuestion jsonQuestion : jsonQuestionList.getList()) {
            realm.beginTransaction();
            if (realm.where(QuestionModel.class).equalTo("id", jsonQuestion.getId()).findFirst() == null) {
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
