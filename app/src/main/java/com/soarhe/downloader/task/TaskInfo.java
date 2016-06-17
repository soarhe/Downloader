package com.soarhe.downloader.task;

import com.soarhe.downloader.utils.Headers;

import java.util.Map;

/**
 * Created by hejunwei on 16/6/13.
 */
public class TaskInfo implements Comparable<TaskInfo> {


    @Override
    public int compareTo(TaskInfo another) {
        // priority
        if (mPriority != another.mPriority) {
            return mPriority.ordinal() - another.mPriority.ordinal();
        } else {
            // createtime
            return (int) (another.mCreatedtime - mCreatedtime);
        }
    }

    public enum Status {
        RUNNING,
        PAUSED,
        WAITING,
        CANCEL,
        SUCCESS,
        FAIL
    }

    public enum Priority {
        BACKGROUND,
        LOW,
        MEDIUM,
        HIGH
    }

    public String mKey;
    public String mUrl;
    public String mFilename;
    public String mSavepath;
    public String mType;
    public String mReferer;
    public Status mStatus;
    public long mTotalsize;
    public long mCurrentsize;
    public Priority mPriority;

    private Headers mHeaders;
    private long mCreatedtime;
    private long mCompletetime;
    private String mTmpName;

    private TaskInfo(String aUrl) {
        mHeaders = new Headers();
        mUrl = aUrl;
        mCreatedtime = System.currentTimeMillis();
        mKey = mUrl + mCreatedtime;
    }

    public void addHeader(String aKey, String aValue) {
        mHeaders.put(aKey, aValue);
    }

    public Map<String, String> getHeaders() {
        return mHeaders.toMap();
    }

}
