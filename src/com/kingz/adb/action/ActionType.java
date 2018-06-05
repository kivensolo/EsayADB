package com.kingz.adb.action;

import java.util.HashMap;
import java.util.Map;

/**
 * author: King.Z <br>
 * date:  2017/12/7 15:52 <br>
 * description: Action类型 <br>
 */
public enum ActionType {
    EMPTY("empty"),
    CONNECT("connect"),
    DISCONNECT("disconnect"),
    CLEAR("clear"),
    UNINSTALL("uninstall"),
    FORCE_STOP("stop"),
    DEVICES("show devices"),
    SHOW_HEAP("showHeap"),
    SHOW_PROC_RANK("showProcRank"),
    DUMP_HEAP("dempHeap"),
    CATCH_SCREEN("敬请期待更多功能"),
    INPUT_TEXT("inputText"),
    START_APP("startApp");


    private static final Map<String,ActionType> stringToEnum = new HashMap<>();
    static{
        for(ActionType act:values()){
            stringToEnum.put(act.toString(),act);
        }
    }
    private final String type;
    ActionType(String type) {
        this.type = type;
    }

    public String value(){
        return toString();
    }

    @Override
    public String toString() {
        return type;
    }

    public static ActionType fromString(String type){
        return stringToEnum.get(type);
    }
}
