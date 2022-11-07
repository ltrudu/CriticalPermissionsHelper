package com.zebra.criticalpermissionshelper;

import android.content.Context;
import android.os.Handler;

/**
 * Created by Trudu Laurent on 2020/08/11.
 */

abstract class CommandBase {
    /*
    A TAG if we want to log something
     */
    protected static String TAG = "DIWrapperMX";

    /*
    A context to work with intents
     */
    protected Context mContext = null;

    protected CommandBaseSettings mSettings = null;

    /*
    A handler that will be used by the derived
    class to prevent waiting to loong for DW in case
    of problem
     */
    protected Handler mTimeOutHandler;

    /*
    What will be done at the end of the TimeOut
     */
    protected Runnable mTimeOutRunnable = new Runnable() {
        @Override
        public void run() {
            onTimeOut(mSettings);
        }
    };


    protected CommandBase(Context aContext)
    {
        mContext = aContext;
        mTimeOutHandler = new Handler(mContext.getMainLooper());
    }


    protected void execute(CommandBaseSettings settings)
    {
        mSettings = settings;
        /*
        Start time out mechanism
        Enabled by default in DWProfileBaseSettings
         */
        if(settings.mEnableTimeOutMechanism) {
            mTimeOutHandler.postDelayed(mTimeOutRunnable,
                    mSettings.mTimeOutMS);
        }
    }

    protected void onTimeOut(CommandBaseSettings settings)
    {
        cleanAll();
    }

    protected void cleanAll()
    {
        if(mTimeOutHandler != null)
        {
            mTimeOutHandler.removeCallbacks(mTimeOutRunnable);
        }
    }
}
