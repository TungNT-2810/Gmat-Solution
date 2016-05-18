package org.iliat.gmat.network;

import com.google.gson.annotations.SerializedName;



/**
 * Created by qhuydtvt on 3/28/2016.
 */
public class JSONAnswerChoice {

    private static final String INDEX = "index";
    private static final String CHOICE = "choice";
    private static final String EXPLANATION = "explanation";

    @SerializedName(INDEX)
    private int index;

    @SerializedName(CHOICE)
    private String choice;

    @SerializedName(EXPLANATION)
    private String explanation;

    public int getIndex() {
        return index;
    }

    public String getChoice() { return choice; }

    public String getExplanation() {
        return explanation;
    }
}
