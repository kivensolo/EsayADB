package com.easyadb.adb.inter_face;

import com.easyadb.adb.action.ActionType;

public interface IActionListenner {
    void onStart(ActionType actionType);
    void onSuccess(ActionType actionType, String info);
    void onError(ActionType actionType, String info);
}
