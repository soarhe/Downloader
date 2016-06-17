package com.soarhe.downloader.task;

import com.soarhe.downloader.net.AbsNetClient;
import com.soarhe.downloader.net.NetBridge;

import java.io.InputStream;
import java.util.Map;

/**
 * Created by baidu on 16/6/17.
 */
public class NormalTask extends AbsTask {

    private NormalTaskCallback mCallback;

    public NormalTask(TaskInfo aInfo) {
        super(aInfo);
        mCallback = new NormalTaskCallback();
    }

    @Override
    public void start() {
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

    private class NormalTaskCallback implements AbsNetClient.InetCallback {

        @Override
        public void onGetHeaders(Map<String, String> aHeaders) {

        }

        @Override
        public void onDownloading(InputStream aIs) {

        }

        @Override
        public void onSuccess() {

        }

        @Override
        public void onFail(String aReason) {

        }
    }
}
