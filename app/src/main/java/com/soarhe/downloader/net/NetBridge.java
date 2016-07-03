package com.soarhe.downloader.net;

import java.util.Map;

/**
 * Created by baidu on 16/6/17.
 */
public class NetBridge {

    private static NetBridge sInstance;
    private AbsNetClient mNetClient;

    private NetBridge() {
        mNetClient = new OkHttpClient();
    }

    public static NetBridge getInstance() {
        if (sInstance == null) {
            synchronized (NetBridge.class) {
                if (sInstance == null) {
                    sInstance = new NetBridge();
                }
            }
        }
        return sInstance;
    }

    public void changeNet(AbsNetClient aNetClient) {
        mNetClient = aNetClient;
    }

    public void start(String aKey, String aUrl, Map<String, String> aHeader
            , AbsNetClient.InetCallback aCallback) {
        mNetClient.start(aKey, aUrl, aHeader, aCallback);
    }

    public void stop(String aKey) {
        mNetClient.stop(aKey);
    }
}
