package org.iliat.gmat.network;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by MrBom on 5/16/2016.
 */
public class JSONQuestionTypeList {
    private static final String QUESTION_TYPE = "type";
    @SerializedName(QUESTION_TYPE)
    private List<JSONQuestionType> list;

    public List<JSONQuestionType> getList() {
        return list;
    }
}
