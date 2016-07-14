package com.soarhe.downloader.write;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;

import com.soarhe.downloader.inter.ServiceFacade;
import com.soarhe.downloader.task.TaskInfo;
import com.soarhe.downloader.task.TaskManager;

/**
 * Created by hejunwei on 16/7/11.
 */
public class WriteManager {

    private static final long CHECK_INTEVAL = 1000 * 60 * 5;
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

    public void recycle(WritingBuffer aBuf) {
        mPool.recycle(aBuf);
    }

    public void writeFile(TaskInfo aInfo, byte[] aData, int aOffset, int aLength) {
        WritingBuffer buf = mPool.getBuffer(aInfo, aData, aOffset, aLength);
        try {
            mWritingThread.offer(buf);
        } catch (Exception e) {
            e.printStackTrace();
            ServiceFacade.getInstance().fail(buf.mInfo.mKey);
        }
    }

    private class WriteHandler extends Handler {

        WriteHandler(Looper aLooper) {
            super(aLooper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_CHECK:
                    mPool.adjustBuffercount();
                    this.sendEmptyMessageDelayed(MSG_CHECK, CHECK_INTEVAL);
                    break;
            }
        }
    }
}
