package org.iliat.gmat.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by MrBom on 5/16/2016.
 */
public class QuestionTypeModel extends RealmObject {
    @PrimaryKey
    private String code;
    private String detail;
    private RealmList<QuestionSubTypeModel> listSubType;

    public RealmList<QuestionSubTypeModel> getListSubType() {
        return listSubType;
    }

    public void setListSubType(RealmList<QuestionSubTypeModel> listSubType) {
        this.listSubType = listSubType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
