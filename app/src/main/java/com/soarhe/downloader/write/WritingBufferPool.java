package com.soarhe.downloader.write;

import com.soarhe.downloader.task.TaskInfo;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by hejunwei on 16/7/11.
 */
public class WritingBufferPool {

    private static final int MAX_POOL_COUNT = 128;
    private Queue<WritingBuffer> mBufferQueue;
    private int mCurrentBuffer;

    WritingBufferPool() {
        mBufferQueue = new LinkedList<>();
        mCurrentBuffer = 0;
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
            mCurrentBuffer++;
        }
        return buf;
    }

    void recycle(WritingBuffer aBuf) {
        if (mCurrentBuffer >= MAX_POOL_COUNT) {
            aBuf.release();
            mCurrentBuffer --;
        } else {
            mBufferQueue.offer(aBuf);
        }
    }

    void adjustBuffercount() {
        if (mBufferQueue.size() >= mCurrentBuffer) {
            int target;
            if (mCurrentBuffer > MAX_POOL_COUNT) {
                target = MAX_POOL_COUNT;
            } else {
                target = MAX_POOL_COUNT / 2;
            }
            while (mCurrentBuffer > target) {
                WritingBuffer buf = mBufferQueue.poll();
                buf.release();
                mCurrentBuffer --;
            }
        }
    }
}
