package org.iliat.gmat.network;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by MrBom on 5/16/2016.
 */
public class JSONQuestionType {
    private static final String CODE = "code";
    private static final String DETAIL = "detail";
    private static final String SUB_TYPE = "sub_types";
    @SerializedName(DETAIL)
    private String detail;

    @SerializedName(CODE)
    private String code;

    @SerializedName(SUB_TYPE)
    private List<JSONQuestionSubType> subTypeList;

    public List<JSONQuestionSubType> getSubTypeList() {
        return subTypeList;
    }

    public void setSubTypeList(List<JSONQuestionSubType> subTypeList) {
        this.subTypeList = subTypeList;
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
