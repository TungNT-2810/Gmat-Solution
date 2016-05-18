package org.iliat.gmat.network;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by qhuydtvt on 4/3/2016.
 */
public class JSONQuestionPack {

    private static final String ID = "_id";
    private static final String AVAILABLE_TIME = "available_time";
    private static final String QUESTION_IDS = "question_ids";

    @SerializedName(ID)
    private JSONObjectID id;

    @SerializedName(AVAILABLE_TIME)
    private String availableTime;

    @SerializedName(QUESTION_IDS)
    private List<String> questionIds;

    public String getId() {
        return id.getOID();
    }

    public String getAvailableTime() {
        return availableTime;
    }

    public List<String> getQuestionIds() {
        return questionIds;
    }
}
