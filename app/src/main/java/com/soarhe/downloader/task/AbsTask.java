package com.soarhe.downloader.task;

/**
 * Created by baidu on 16/6/16.
 */
public abstract class AbsTask {

    public TaskInfo mInfo;

    public AbsTask(TaskInfo aInfo) {
        mInfo = aInfo;
    }

    public abstract void start();

    public abstract void pause();

    public abstract void cancel();
}
