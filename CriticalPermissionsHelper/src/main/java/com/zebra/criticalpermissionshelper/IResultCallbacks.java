package com.zebra.criticalpermissionshelper;

public interface IResultCallbacks {
    void onSuccess(final String message, final String resultXML);
    void onError(final String message, final String resultXML);
    void onDebugStatus(final String message);
}
