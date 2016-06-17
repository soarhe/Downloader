package com.soarhe.downloader.task;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.soarhe.downloader.IDownloadCallback;
import com.soarhe.downloader.utils.Utils;

import java.util.HashMap;
import java.util.List;

/**
 * Created by hejunwei on 16/6/13.
 */
public final class TaskManager {

    private static final int MSG_CHECKLIST = 0;
    private static final int MSG_INITSTOREDTASK = 1;
    private static final int MSG_ADDTASK = 2;

    private HashMap<String, AbsTask> mTaskMap;
    private IDownloadCallback mObserver;
    private WaitingQueue mWaitingQueue;
    private RunningList mRunningList;
    private HandlerThread mInnerThread;
    private InnerHandler mHandler;
    private int mMaxDownloadNum;

    public TaskManager() {
        mTaskMap = new HashMap<>();
        mRunningList = new RunningList();
        mWaitingQueue = new WaitingQueue();
        mRunningList = new RunningList();
        mMaxDownloadNum = 3;

        mInnerThread = new HandlerThread("inner");
        mInnerThread.start();
        mHandler = new InnerHandler(mInnerThread.getLooper());
        mHandler.sendEmptyMessage(MSG_INITSTOREDTASK);
        mHandler.obtainMessage(MSG_CHECKLIST).sendToTarget();
    }

    private void initStoredTask() {
    }

    public void setObserver(IDownloadCallback aObserver) {
        mObserver = aObserver;
    }

    public String addTask(TaskInfo aInfo) {
        // all kinds of checking

        // fill info's essential fields
        if (TextUtils.isEmpty(aInfo.mFilename)) {
            aInfo.mFilename = Utils.getFilenamebyInfo(aInfo);
        }
        if (TextUtils.isEmpty(aInfo.mSavepath)) {
            aInfo.mFilename = Utils.getDefaultPath();
        }
        if (TextUtils.isEmpty(aInfo.mFilename) || TextUtils.isEmpty(aInfo.mSavepath)) {
            return null;
        }

        // choose task
        AbsTask task = null;
        if (TextUtils.isEmpty(aInfo.mType)) {
            task = new NormalTask(aInfo);
        }

        if (task != null) {
            Message msg = mHandler.obtainMessage(MSG_ADDTASK, task);
            msg.sendToTarget();
            return aInfo.mKey;
        } else {
            return null;
        }
    }

    public void pause(String aKey) {
        AbsTask task = mTaskMap.get(aKey);
        if (task != null && task.mInfo != null) {
            if (task.mInfo.mStatus == TaskInfo.Status.RUNNING) {
                task.pause();
            } else if (task.mInfo.mStatus == TaskInfo.Status.WAITING) {
                task.mInfo.mStatus = TaskInfo.Status.PAUSED;
            } else {
                // do nothing
            }
        }
        if (mObserver != null) {
            mObserver.onPause(aKey);
        }
    }

    public void resume(String aKey) {
        AbsTask task = mTaskMap.get(aKey);
        if (task != null && task.mInfo != null) {
            if (task.mInfo.mStatus == TaskInfo.Status.PAUSED) {
                task.mInfo.mStatus = TaskInfo.Status.WAITING;
                mHandler.sendEmptyMessage(MSG_ADDTASK);
            }
        }
    }

    public void cancel(String aKey) {
        AbsTask task = mTaskMap.get(aKey);
        if (task != null && task.mInfo != null) {
            if (task.mInfo.mStatus == TaskInfo.Status.RUNNING) {
                task.cancel();
            } else {
                task.mInfo.mStatus = TaskInfo.Status.CANCEL;
            }
        }
        if (mObserver != null) {
            mObserver.onCancel(aKey);
        }
    }

    public void quit() {
        mTaskMap.clear();
        mTaskMap = null;
        mRunningList.clear();
        mRunningList = null;
        mWaitingQueue.clear();
        mWaitingQueue = null;
        mHandler = null;
        mInnerThread.quit();
        mInnerThread = null;
    }

    private void refresh() {
        if (!mHandler.hasMessages(MSG_CHECKLIST)) {
            mHandler.sendEmptyMessage(MSG_CHECKLIST);
        }
    }

    private class InnerHandler extends Handler {

        InnerHandler(Looper aLooper) {
            super(aLooper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_CHECKLIST:
                    mWaitingQueue.checkstatus();
                    mRunningList.checkstatus();
                    // 暂停冗余task,回到waitinglist
                    List<AbsTask> tasks = mRunningList.checkRunningNum(mMaxDownloadNum);
                    if (tasks != null) {
                        for (AbsTask task : tasks) {
                            mWaitingQueue.offer(task);
                        }
                    }
                    // 补满running list
                    int remainNum = mRunningList.getRemainNum(mMaxDownloadNum);
                    while (remainNum > 0) {
                        AbsTask task = (AbsTask) mWaitingQueue.poll();
                        if (task != null) {
                            mRunningList.add(task);
                            remainNum --;
                        } else {
                            break;
                        }
                    }
                    if (mRunningList.size() > 0 || mWaitingQueue.size() > 0) {
                        sendEmptyMessageDelayed(MSG_CHECKLIST, 3000);
                    }
                    break;
                case MSG_INITSTOREDTASK:
                    initStoredTask();
                    break;
                case MSG_ADDTASK:
                    if (msg.obj != null && msg.obj instanceof AbsTask) {
                        AbsTask task = (AbsTask) msg.obj;
                        if (!mTaskMap.containsKey(task.mInfo.mKey)) {
                            mTaskMap.put(task.mInfo.mKey, task);
                        }
                        mWaitingQueue.offer(task);
                        refresh();
                    }
                    break;
                default:
                    break;
            }
        }
    }



}
