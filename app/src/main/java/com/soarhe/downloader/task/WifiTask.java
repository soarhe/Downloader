package com.soarhe.downloader.task;

import android.util.Log;

import com.soarhe.downloader.net.AbsNetClient;
import com.soarhe.downloader.net.NetBridge;
import com.soarhe.downloader.write.WriteManager;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Map;

/**
 * Created by baidu on 16/6/17.
 */
public class WifiTask extends AbsTask {

    private NormalTaskCallback mCallback;
    private BufferedOutputStream mOutStream;

    public WifiTask(TaskInfo aInfo) {
        super(aInfo);
        mCallback = new NormalTaskCallback();
    }

    @Override
    public void start() {
        getHeaderSync();
        NetBridge.getInstance().start(mInfo.mKey, mInfo.mUrl, mInfo.getHeaders(), mCallback);
    }

    @Override
    public void pause() {
        NetBridge.getInstance().stop(mInfo.mKey);
        mInfo.mStatus = TaskInfo.Status.PAUSED;
    }

    @Override
    public void cancel() {
        NetBridge.getInstance().stop(mInfo.mKey);
        mInfo.mStatus = TaskInfo.Status.CANCEL;
    }

    @Override
    public void fail() {
        NetBridge.getInstance().stop(mInfo.mKey);
        mInfo.mStatus = TaskInfo.Status.FAIL;
    }

    private class NormalTaskCallback implements AbsNetClient.InetCallback {

        @Override
        public void onDownloading(InputStream aIs) {
            byte[] buf = null;
            try {
                if (mOutStream == null) {
                    mOutStream = new BufferedOutputStream(
                            new FileOutputStream(mInfo.getTmpFullpath(), true), BUF_SIZE);
                }
                buf = new byte[BUF_SIZE];
                int readcount = 0;
                int offset = 0;
                while ((readcount = aIs.read(buf)) != -1) {
                    WriteManager.getInstance().writeFile(mInfo, buf, offset, readcount);
                    offset += readcount;
//                    mOutStream.write(buf, 0, readcount);
//                    mOutStream.flush();
//                    // 更新状态
//                    mInfo.mCurrentsize += readcount;
//                    Log.d(TAG, "ondownloading: " + mInfo.mCurrentsize + "/" + mInfo.mTotalsize);
//                    if (mInfo.mTotalsize > 0 &&
//                            mInfo.mCurrentsize > mInfo.mTotalsize) {
//                        onFail("larger than total");
//                        NetBridge.getInstance().stop(mInfo.mKey);
//                        return;
//                    }
                    // callback
                }

            } catch (Exception e) {
                NetBridge.getInstance().stop(mInfo.mKey);
                onFail(e.getMessage());
            } finally {
                if (mOutStream != null) {
                    try {
                        mOutStream.close();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                    mOutStream = null;
                }
                buf = null;
            }
        }

        @Override
        public void onSuccess() {
            Log.d(TAG, "on Success");
        }

        @Override
        public void onFail(String aReason) {
            Log.d(TAG, "on Fail, reason: " + aReason);
        }
    }
}
