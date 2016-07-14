package com.soarhe.downloader.write;

import android.util.Log;

import com.soarhe.downloader.inter.ServiceFacade;
import com.soarhe.downloader.task.TaskManager;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by hejunwei on 16/7/11.
 */
public class WritingThread extends Thread {

    private BlockingQueue<WritingBuffer> mBlockingQueue;
    private boolean mRunning;
    private HashMap<String, RandomAccessFile> mRAFileMap;

    WritingThread() {
        mBlockingQueue = new LinkedBlockingQueue<>();
        mRunning = true;
        mRAFileMap = new HashMap<>();
    }

    void release() {
        mRunning = false;
        this.interrupt();
        mBlockingQueue.clear();
        mBlockingQueue = null;
        if (mRAFileMap != null) {
            mRAFileMap.clear();
            mRAFileMap = null;
        }
    }

    void offer(WritingBuffer aBuf) throws Exception {
        if (!mRAFileMap.containsKey(aBuf.mInfo.mKey)) {
            RandomAccessFile raFile = new RandomAccessFile(aBuf.mInfo.mSavepath + aBuf.mInfo.mFilename, "rw");
            mRAFileMap.put(aBuf.mInfo.mKey, raFile);
        }
        mBlockingQueue.offer(aBuf);
    }

    @Override
    public void run() {
        while (mRunning) {
            try {
                WritingBuffer buf = mBlockingQueue.take();
                // 写!
                try {
                    if (write(buf)) {
                        // 回调完成
                        Log.d(TaskManager.TAG, buf.mInfo.mFilename + " complete");
                    } else {
                        // 回调进度
                        Log.d(TaskManager.TAG, buf.mInfo.mFilename + " " + buf.mInfo.mCurrentsize + "/" + buf.mInfo.mTotalsize);
                    }
                } catch (Exception e) {
                    // 失败
                    ServiceFacade.getInstance().fail(buf.mInfo.mKey);
                    // 回调失败
                } finally {
                    WriteManager.getInstance().recycle(buf);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean write(WritingBuffer aBuf) throws Exception {
        RandomAccessFile raFile = mRAFileMap.get(aBuf.mInfo.mKey);
        if (raFile == null) {
            raFile = new RandomAccessFile(aBuf.mInfo.mSavepath + aBuf.mInfo.mFilename, "rw");
            mRAFileMap.put(aBuf.mInfo.mKey, raFile);
        }
        raFile.seek(aBuf.mOffset);
        raFile.write(aBuf.mBuffer, 0, aBuf.mLength);
        aBuf.mInfo.mCurrentsize += aBuf.mLength;
        Log.d(TaskManager.TAG, "write file: offset = " + aBuf.mOffset + " length: " + aBuf.mLength + " current: " + aBuf.mInfo.mCurrentsize);
        // TODO: 16/7/13 更新progress map
        if (aBuf.mInfo.mTotalsize > 0 && aBuf.mInfo.mCurrentsize > aBuf.mInfo.mTotalsize) {
            RandomAccessFile file = mRAFileMap.remove(aBuf.mInfo.mKey);
            try {
                file.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            throw new IOException("larger than total size");
        } else if (aBuf.mInfo.mCurrentsize == aBuf.mInfo.mTotalsize) {
            // 写完了
            RandomAccessFile file = mRAFileMap.remove(aBuf.mInfo.mKey);
            try {
                file.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        } else {
            return false;
        }
    }
}
