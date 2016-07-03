package com.soarhe.downloader.task;

/**
 * Created by baidu on 16/6/16.
 */
public abstract class AbsTask {

    public static final String TAG = "task";

    // page大小为4k,所以缓存最好是4k的倍数,这里的8k是测试后的经验值.
    public static final int BUF_SIZE = 1024 * 8;

    public TaskInfo mInfo;

    public AbsTask(TaskInfo aInfo) {
        mInfo = aInfo;
    }

    public abstract void start();

    public abstract void pause();

    public abstract void cancel();
}
