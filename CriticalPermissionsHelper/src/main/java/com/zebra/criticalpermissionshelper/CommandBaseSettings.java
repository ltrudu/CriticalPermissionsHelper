package com.zebra.criticalpermissionshelper;

class CommandBaseSettings
{
    /*
    Use this to track the source of the intent
     */
    protected String mCommandId = "";

    /*
    Some method return only errors (StartScan, StopScan)
    We do not need a time out for them
     */
    protected boolean mEnableTimeOutMechanism = true;

    /*
    A time out, in case we don't receive an answer
    from PrintConnect
     */
    protected long mTimeOutMS = 5000;
}
