package com.soarhe.downloader.write;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;

import com.soarhe.downloader.task.TaskInfo;

/**
 * Created by hejunwei on 16/7/11.
 */
public class WriteManager {

    private static final long CHECK_INTEVAL = 1000 * 60 * 5;
    private static final int MSG_ADD = 0;
    private static final int MSG_CHECK = 1;
    private static WriteManager sInstance;
    private WritingBufferPool mPool;
    private WritingThread mWritingThread;
    private Handler mHandler;
    private HandlerThread mHandlerThread;

    private WriteManager() {
        mPool = new WritingBufferPool();
        mWritingThread = new WritingThread();
        mWritingThread.start();
        mHandlerThread = new HandlerThread("writeMgr", Process.THREAD_PRIORITY_BACKGROUND);
        mHandlerThread.start();
        mHandler = new WriteHandler(mHandlerThread.getLooper());
        mHandler.sendEmptyMessageDelayed(MSG_CHECK, CHECK_INTEVAL);
    }

    public void release() {
        if (mPool != null) {
            mPool.release();
            mPool = null;
        }
        if (mHandler != null) {
            mHandler = null;
        }
        if (mHandlerThread != null) {
            mHandlerThread.quit();
            mHandlerThread = null;
        }
        if (mWritingThread != null) {
            mWritingThread.release();
            mWritingThread = null;
        }
        sInstance = null;
    }

    public static WriteManager getInstance() {
        if (sInstance == null) {
            synchronized (WriteManager.class) {
                if (sInstance == null) {
                    sInstance = new WriteManager();
                }
            }
        }
        return sInstance;
    }

    public void writeFile(TaskInfo aInfo, byte[] aData, int aOffset, int aLength) {
        Bundle bundle = new Bundle();
        bundle.putByteArray("data", aData);
        bundle.putInt("offset", aOffset);
        bundle.putInt("length", aLength);
        Message msg = mHandler.obtainMessage(MSG_ADD, aInfo);
        msg.setData(bundle);
        msg.sendToTarget();
    }

    private class WriteHandler extends Handler {

        WriteHandler(Looper aLooper) {
            super(aLooper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_ADD:
                    if (msg.obj == null || !(msg.obj instanceof TaskInfo)) {
                        return;
                    }
                    Bundle bundle = msg.getData();
                    if (bundle == null) {
                        return;
                    }
                    WritingBuffer buf = mPool.getBuffer((TaskInfo) msg.obj,
                            bundle.getByteArray("data"),
                            bundle.getInt("offset"),
                            bundle.getInt("length"));
                    // 传至blocking队列
                    mWritingThread.offer(buf);
                    break;
                case MSG_CHECK:
                    mPool.adjustBuffercount();
                    this.sendEmptyMessageDelayed(MSG_CHECK, CHECK_INTEVAL);
                    break;
            }
        }
    }
}
