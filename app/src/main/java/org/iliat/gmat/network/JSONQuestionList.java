package org.iliat.gmat.network;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class JSONQuestionList {

    private static final String VERSION = "version";
    private static final String QUESTIONS = "questions";

    @SerializedName(QUESTIONS)
    private List<JSONQuestion> list;

    public List<JSONQuestion> getList() {
        return list;
    }

}
