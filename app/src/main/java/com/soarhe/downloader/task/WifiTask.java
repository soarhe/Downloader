package com.soarhe.downloader.task;

import android.util.Log;

import com.soarhe.downloader.net.AbsNetClient;
import com.soarhe.downloader.net.NetBridge;
import com.soarhe.downloader.write.WriteManager;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Created by baidu on 16/6/17.
 */
public class WifiTask extends AbsTask {

    private NormalTaskCallback mCallback;

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
        public void onstart(Map<String, String> aResponseHeaders) {
            if (aResponseHeaders != null) {
                String encoding = aResponseHeaders.get("Transfer-Encoding");
                if (encoding != null && encoding.equals("chunked")) {
                    return;
                } else {
                    String length = aResponseHeaders.get("Content-Length");
                    if (length != null) {
                        try {
                            mInfo.mTotalsize = Long.valueOf(length);
                        } catch (Exception e) {
                            mInfo.mTotalsize = 0;
                        }
                    }
                }
            }
        }

        @Override
        public void onDownloading(InputStream aIs) {
            try {
                byte[] buf = new byte[BUF_SIZE];
                int readcount = 0;
                int offset = 0;
                while ((readcount = aIs.read(buf)) != -1) {
                    WriteManager.getInstance().writeFile(mInfo, buf, offset, readcount);
                    offset += readcount;
                }

            } catch (Exception e) {
                e.printStackTrace();
                NetBridge.getInstance().stop(mInfo.mKey);
                onFail(e.getMessage());
            }
        }

        @Override
        public void onSuccess() {
//            Log.d(TAG, "on Success");
        }

        @Override
        public void onFail(String aReason) {
            Log.d(TAG, "on Fail, reason: " + aReason);
        }
    }
}
