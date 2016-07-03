package com.soarhe.downloader.inter;

import com.soarhe.downloader.task.TaskInfo;
import com.soarhe.downloader.task.TaskManager;

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

}
