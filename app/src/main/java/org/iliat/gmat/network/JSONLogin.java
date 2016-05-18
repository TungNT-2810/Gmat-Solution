package org.iliat.gmat.network;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hungtran on 4/19/16.
 */
public class JSONLogin {
    private static final String LOGIN_STATUS = "login_status";
    private static final String LOGIN_MESSAGE = "login_message";

    @SerializedName(LOGIN_STATUS)
    private int login_status;
    @SerializedName(LOGIN_MESSAGE)
    private String login_message;

    public int getLogin_status() {
        return login_status;
    }

    public void setLogin_status(int login_status) {
        this.login_status = login_status;
    }

    public String getLogin_message() {
        return login_message;
    }

    public void setLogin_message(String login_message) {
        this.login_message = login_message;
    }

    //    public String getLoginMessage() {
//        return loginMessage;
//    }
//
//    public void setLoginMessage(String loginMessage) {
//        this.loginMessage = loginMessage;
//    }
//
//    public Integer getLoginStatus() {
//        return loginStatus;
//    }
//
//    public void setLoginStatus(Integer loginStatus) {
//        this.loginStatus = loginStatus;
//    }
}
