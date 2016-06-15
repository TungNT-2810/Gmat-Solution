package org.iliat.gmat.network;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class JSONQuestionPackList {

    @SerializedName("question_packs")
    private List<JSONQuestionPack> list;

    public List<JSONQuestionPack> getList() {
        return list;
    }
}
