package org.iliat.gmat.network;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by hungtran on 3/13/16.
 */
public class JSONObjectID implements Serializable {

    private static final String OID = "oid";

    @SerializedName(OID)
    private String oid;

    public String getOID() {
        return oid;
    }
}
