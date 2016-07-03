package com.soarhe.downloader.task;

import android.os.Parcel;
import android.os.Parcelable;

import com.soarhe.downloader.utils.Headers;

import java.util.Map;

/**
 * Created by hejunwei on 16/6/13.
 */
public class TaskInfo implements Comparable<TaskInfo>, Parcelable {


    protected TaskInfo(Parcel in) {
        mKey = in.readString();
        mUrl = in.readString();
        mFilename = in.readString();
        mSavepath = in.readString();
        mType = in.readString();
        mReferer = in.readString();
        mTotalsize = in.readLong();
        mCurrentsize = in.readLong();
        mCreatedtime = in.readLong();
        mCompletetime = in.readLong();
        mTmpName = in.readString();
        mHeaders = Headers.from(in.readHashMap(getClass().getClassLoader()));
    }

    public static final Creator<TaskInfo> CREATOR = new Creator<TaskInfo>() {
        @Override
        public TaskInfo createFromParcel(Parcel in) {
            return new TaskInfo(in);
        }

        @Override
        public TaskInfo[] newArray(int size) {
            return new TaskInfo[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mKey);
        dest.writeString(mUrl);
        dest.writeString(mFilename);
        dest.writeString(mSavepath);
        dest.writeString(mType);
        dest.writeString(mReferer);
        dest.writeLong(mTotalsize);
        dest.writeLong(mCurrentsize);
        dest.writeLong(mCreatedtime);
        dest.writeLong(mCompletetime);
        dest.writeString(mTmpName);
        dest.writeMap(mHeaders.toMap());
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
    public Status mStatus = Status.WAITING;
    public long mTotalsize;
    public long mCurrentsize;
    public Priority mPriority;
    public String mProgress;

    private Headers mHeaders;
    private long mCreatedtime;
    private long mCompletetime;
    public String mTmpName;

    public TaskInfo(String aUrl) {
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

    public String getTmpFullpath() {
        return mSavepath + mTmpName;
    }



}
