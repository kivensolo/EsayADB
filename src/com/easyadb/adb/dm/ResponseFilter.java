package com.easyadb.adb.dm;

import java.util.HashMap;

public class ResponseFilter {
    public static HashMap<String,String> ERROR_KEYS = new HashMap<>();
    static {
        ERROR_KEYS.put("error","");
        ERROR_KEYS.put("unable","");
        ERROR_KEYS.put("Failed","");
        ERROR_KEYS.put("Failure","");
        ERROR_KEYS.put("cannot connect","");
        ERROR_KEYS.put("unable to connect to","");
    }

}
