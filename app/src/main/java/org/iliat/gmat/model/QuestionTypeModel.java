package org.iliat.gmat.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by MrBom on 5/16/2016.
 */
public class QuestionTypeModel extends RealmObject {
    @PrimaryKey
    private String code;
    @Required
    private String detail;

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
