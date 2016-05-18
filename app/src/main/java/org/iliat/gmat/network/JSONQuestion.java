package org.iliat.gmat.network;

import com.google.gson.annotations.SerializedName;


import java.util.List;

/**
 * Created by qhuydtvt on 4/3/2016.
 */
public class JSONQuestion {

    private static final String ID = "_id";
    private static final String TYPE = "type";
    private static final String SUB_TYPE = "sub_type";
    private static final String STIMULUS = "stimulus";
    private static final String STEM = "stem";
    private static final String ANSWER_CHOICES = "answer_choices";
    private static final String RIGHT_ANSWER = "right_answer";

    @SerializedName(ID)
    private JSONObjectID id;

    @SerializedName(TYPE)
    private String type;

    @SerializedName(SUB_TYPE)
    private String subType;

    @SerializedName(STIMULUS)
    private String stimulus;

    @SerializedName(STEM)
    private String stem;

    @SerializedName(ANSWER_CHOICES)
    private List<JSONAnswerChoice> answerChoiceList;

    @SerializedName(RIGHT_ANSWER)
    private int rightAnswer;

    public String getId() {
        return id.getOID();
    }

    public String getType() {
        return type;
    }

    public String getSubType() {
        return subType;
    }

    public String getStimulus() {
        return stimulus;
    }

    public String getStem() {
        return stem;
    }

    public List<JSONAnswerChoice> getAnswerChoiceList() {
        return answerChoiceList;
    }

    public int getRightAnswer() {
        return rightAnswer;
    }
}
