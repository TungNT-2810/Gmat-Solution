package org.iliat.gmat.enitity;

import org.json.JSONObject;

/**
 * Created by qhuydtvt on 3/9/2016.
 */
public interface JSONPostDownloadHandler {
    void onPostDownload(JSONObject jsonObject, String tag);
}
