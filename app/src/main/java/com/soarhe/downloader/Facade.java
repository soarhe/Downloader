package com.soarhe.downloader;

import com.soarhe.downloader.task.TaskInfo;
import com.soarhe.downloader.task.TaskManager;

/**
 * Created by hejunwei on 16/6/13.
 */
public final class Facade {

    private static Facade sInstance;
    private boolean mInited;
    private TaskManager mTaskmgr;

    private Facade() {
        mInited = false;
        mTaskmgr = new TaskManager();
    }

    public static Facade getInstance() {
        if (sInstance == null) {
            synchronized (sInstance) {
                if (sInstance == null) {
                    sInstance = new Facade();
                }
            }
        }
        return sInstance;
    }

    public String start(TaskInfo aInfo) {
        return mTaskmgr.addTask(aInfo);
    }

    public void pause(String aKey) {
        mTaskmgr.pause(aKey);
    }

    public void resume(String aKey) {
        mTaskmgr.resume(aKey);
    }

    public void cancel(String aKey) {
        mTaskmgr.cancel(aKey);
    }

    public void setObserver(IDownloadCallback aObserver) {
        mTaskmgr.setObserver(aObserver);
    }

}
