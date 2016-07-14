package com.soarhe.downloader.write;

import com.soarhe.downloader.task.AbsTask;
import com.soarhe.downloader.task.TaskInfo;

/**
 * Created by hejunwei on 16/7/11.
 */
public class WritingBuffer {

    public static final int BUF_SIZE = AbsTask.BUF_SIZE;

    public TaskInfo mInfo;
    public int mOffset;
    public int mLength;
    public byte[] mBuffer;

    /**
     * constructor
     * @param aInfo info
     * @param aOffset offset
     * @param aLength length
     * @param aData data
     */
    WritingBuffer(TaskInfo aInfo, int aOffset, int aLength, byte[] aData) {
        mInfo = aInfo;
        mOffset = aOffset;
        mLength = aLength;
        if (mBuffer == null) {
            mBuffer = new byte[BUF_SIZE];
        }
        System.arraycopy(aData, 0, mBuffer, 0, mLength);
    }

    void set(TaskInfo aInfo, int aOffset, int aLength, byte[] aData) {
        mInfo = aInfo;
        mOffset = aOffset;
        mLength = aLength;
        if (mBuffer == null) {
            mBuffer = new byte[BUF_SIZE];
        }
        System.arraycopy(aData, 0, mBuffer, 0, mLength);
    }

    void release() {
        mInfo = null;
        mBuffer = null;
    }



}
