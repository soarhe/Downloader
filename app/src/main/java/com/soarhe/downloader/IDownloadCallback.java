package com.soarhe.downloader;

import java.util.Map;

/**
 * Created by baidu on 16/6/16.
 */
public interface IDownloadCallback {

    void onStart(String aKey);

    void onGetHeaders(String aKey, Map<String, String> aResponseHeaders);

    void onPause(String aKey);

    void onDownloading(String aKey, long aTotalsize, long aCurrentsize);

    void onSuccess(String aKey);

    void onFail(String aKey, String aReason);

    void onCancel(String aKey);
}
