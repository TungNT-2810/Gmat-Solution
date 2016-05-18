package org.iliat.gmat.dao;

/**
 * Created by hungtran on 3/12/16.
 */
public class GMATAPI {

    public static final String API_ROOT = "https://g-service.herokuapp.com/api";
    public static final String QUESTIONS_API = API_ROOT + "/questions";
    public static final String QUESTION_PACKS_API = API_ROOT + "/question_packs";
    /**
     * method : POST
     * BODY {"username","password"}
     * encoding FORM
     * result login_status : 0,1
     *         login_message : String
     */
    public static final String LOGIN_API = API_ROOT + "/login";
}
