package com.soarhe.downloader.task;

import java.util.HashMap;

/**
 * Created by baidu on 16/6/16.
 */
public abstract class AbsTask {

    public static final String TAG = "task";

    // page大小为8k,所以缓存最好是8k的倍数,这里的8k是测试后的经验值.
    public static final int BUF_SIZE = 1024 * 8;

    public TaskInfo mInfo;

    public AbsTask(TaskInfo aInfo) {
        mInfo = aInfo;
    }

    public abstract void start();

    public abstract void pause();

    public abstract void cancel();

    public abstract void fail();

    protected HashMap<String, String> getHeaderSync() {
        HashMap<String, String> headers = new HashMap<>();
        return null;
    }
}
