package org.iliat.gmat.network;

import com.google.gson.annotations.SerializedName;

/**
 * Created by MrBom on 9/6/2016.
 */
public class JSONQuestionSubType {
    private static final String CODE = "code";
    private static final String DETAIL = "detail";
    @SerializedName(DETAIL)
    private String detail;

    @SerializedName(CODE)
    private String code;

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
