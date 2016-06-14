package org.iliat.gmat.interf;

import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by qhuydtvt on 3/14/2016.
 */
public interface JSONParser {
    void onDownload(InputStreamReader inputStream, String tag);
}
