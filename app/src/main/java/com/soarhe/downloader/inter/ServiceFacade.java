package com.soarhe.downloader.inter;

import com.soarhe.downloader.task.TaskInfo;
import com.soarhe.downloader.task.TaskManager;
import com.soarhe.downloader.write.WriteManager;

/**
 * Created by hejunwei on 16/6/13.
 */
public class ServiceFacade {

    private static ServiceFacade sInstance;
    private TaskManager mTaskmgr;

    private ServiceFacade() {
        mTaskmgr = new TaskManager();
    }

    public static ServiceFacade getInstance() {
        if (sInstance == null) {
            synchronized (ServiceFacade.class) {
                if (sInstance == null) {
                    sInstance = new ServiceFacade();
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

    public void fail(String aKey) {
        mTaskmgr.fail(aKey);
    }

    public void release() {
        if (mTaskmgr != null) {
            mTaskmgr.release();
            mTaskmgr = null;
        }
        WriteManager.getInstance().release();
        sInstance = null;
    }

}
