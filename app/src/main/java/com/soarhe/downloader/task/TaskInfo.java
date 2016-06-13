package com.soarhe.downloader.task;

import android.preference.PreferenceActivity;

import com.soarhe.downloader.INoProguard;
import com.squareup.okhttp.Headers;

import java.util.Map;

/**
 * Created by hejunwei on 16/6/13.
 */
public class TaskInfo implements INoProguard {

    public enum Status {
        RUNNING,
        PAUSED,
        WAITING,
        CANCEL,
        SUCCESS,
        FAIL
    }

    public String mKey;
    public String mUrl;
    public String mFilename;
    public String mSavepath;
    public String mType;
    public String mReferer;
    public long mTotalsize;
    public long mCurrentsize;

    private Map<String, String> mHeaders;
    private long mCreatedtime;
    private long mCompletetime;
    private String mTmpName;

    private TaskInfo(String aUrl) {

        mUrl = aUrl;
        mCreatedtime = System.currentTimeMillis();
        mKey = mUrl + mCreatedtime;
    }

    public void addHeader(String aKey, String aValue) {


    }
}
