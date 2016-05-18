package org.iliat.gmat.database;

import com.orm.SugarRecord;

/**
 * Created by qhuydtvt on 4/4/2016.
 */
public class Utils {
    public static boolean tableExists(String tableName) {
//        SugarRecord.executeQuery(String.format("SELECT count(*) FROM sqlite_master WHERE " +
//                "type = 'table' AND name = '%s'", tableName));
//
        return false;
    }
}
