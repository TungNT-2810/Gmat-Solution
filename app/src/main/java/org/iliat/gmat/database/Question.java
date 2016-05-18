package org.iliat.gmat.database;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;
import com.orm.dsl.Unique;

import org.iliat.gmat.network.JSONAnswerChoice;
import org.iliat.gmat.network.JSONObjectID;

import java.io.Serializable;
import java.util.List;

/**
 * Created by qhuydtvt on 4/4/2016.
 */
public class Question extends SugarRecord implements Serializable {

    private Long id;
    private String idInServer;
    private String type;
    private String subType;
    private String stimulus;
    private String stem;
    private int rightAnswerIndex;

    public Question(String idInServer, String type, String subType, String stimulus, String stem, int rightAnswerIndex) {
        this.idInServer = idInServer;
        this.type = type;
        this.subType = subType;
        this.stimulus = stimulus;
        this.stem = stem;
        this.rightAnswerIndex = rightAnswerIndex;
    }

    @Override
    public Long getId() {
        return id;
    }

    public Question(){}

    public String getIdInServer() {
        return idInServer;
    }

    public void setIdInServer(String idInServer) {
        this.idInServer = idInServer;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getStimulus() {
        return stimulus;
    }

    public void setStimulus(String stimulus) {
        this.stimulus = stimulus;
    }

    public String getStem() {
        return stem;
    }

    public void setStem(String stem) {
        this.stem = stem;
    }

    public int getRightAnswerIndex() {
        return rightAnswerIndex;
    }

    public void setRightAnswerIndex(int rightAnswerIndex) {
        this.rightAnswerIndex = rightAnswerIndex;
    }

//    public List<AnswerChoice> getAnswerChoiceList(){
//        return AnswerChoice.find(AnswerChoice.class, "question=?", String.valueOf(getId()));
//    }
}
