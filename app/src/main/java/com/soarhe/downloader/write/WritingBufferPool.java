package com.soarhe.downloader.write;

import android.util.Log;

import com.soarhe.downloader.task.TaskInfo;
import com.soarhe.downloader.task.TaskManager;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by hejunwei on 16/7/11.
 */
public class WritingBufferPool {

    private static final int MAX_POOL_COUNT = 128;
    private Queue<WritingBuffer> mBufferQueue;
    private AtomicInteger mCurrentBuffer;

    WritingBufferPool() {
        mBufferQueue = new ConcurrentLinkedQueue<>();
        mCurrentBuffer = new AtomicInteger(0);
    }

    void release() {
        if (mBufferQueue != null) {
            mBufferQueue.clear();
            mBufferQueue = null;
        }
    }

    WritingBuffer getBuffer(TaskInfo aInfo, byte[] aData, int aOffset, int aLength) {
        WritingBuffer buf = mBufferQueue.poll();
        if (buf != null) {
            buf.set(aInfo, aOffset, aLength, aData);
        } else {
            buf = new WritingBuffer(aInfo, aOffset, aLength, aData);
            mCurrentBuffer.incrementAndGet();
        }
//        Log.d(TaskManager.TAG, "get buffer, buffer count: " + mCurrentBuffer.get());
        return buf;
    }

    void recycle(WritingBuffer aBuf) {
        if (mCurrentBuffer.get() >= MAX_POOL_COUNT) {
            aBuf.release();
            mCurrentBuffer.decrementAndGet();
        } else {
            mBufferQueue.offer(aBuf);
        }
//        Log.d(TaskManager.TAG, "recycle buffer, buffer count: " + mCurrentBuffer.get());
    }

    void adjustBuffercount() {
        if (mBufferQueue.size() >= mCurrentBuffer.get()) {
            int target;
            if (mCurrentBuffer.get() > MAX_POOL_COUNT) {
                target = MAX_POOL_COUNT;
            } else {
                target = MAX_POOL_COUNT / 2;
            }
            while (mCurrentBuffer.get() > target) {
                WritingBuffer buf = mBufferQueue.poll();
                buf.release();
                mCurrentBuffer.decrementAndGet();
            }
        }
    }
}
