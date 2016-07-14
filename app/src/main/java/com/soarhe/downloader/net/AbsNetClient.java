package com.soarhe.downloader.net;

import java.io.InputStream;
import java.util.Map;

/**
 * Created by hejunwei on 16/6/17.
 */
public abstract class AbsNetClient {

    public abstract void start(String aKey, String aUrl, Map<String, String> aHeader, InetCallback aCallback);

    public abstract void stop(String aKey);


    public interface InetCallback {
        void onstart(Map<String, String> aResponseHeaders);
        void onDownloading(InputStream aIs);
        void onSuccess();
        void onFail(String aReason);
    }
}
