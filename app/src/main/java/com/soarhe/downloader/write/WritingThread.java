package com.soarhe.downloader.write;

import com.soarhe.downloader.inter.ServiceFacade;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by hejunwei on 16/7/11.
 */
public class WritingThread extends Thread {

    private BlockingQueue<WritingBuffer> mBlockingQueue;
    private boolean mRunning;

    WritingThread() {
        mBlockingQueue = new LinkedBlockingQueue<>();
        mRunning = true;
    }

    void release() {
        mRunning = false;
        this.interrupt();
        mBlockingQueue.clear();
        mBlockingQueue = null;
    }

    void offer(WritingBuffer aBuf) {
        mBlockingQueue.offer(aBuf);
    }

    @Override
    public void run() {
        while (mRunning) {
            try {
                WritingBuffer buf = mBlockingQueue.take();
                // TODO: 16/7/11  错误处理,失败时需停止下载
                // 写!
                // 回调
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
