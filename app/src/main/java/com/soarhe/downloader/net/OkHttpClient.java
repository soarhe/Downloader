package com.soarhe.downloader.net;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by baidu on 16/6/17.
 */
public class OkHttpClient extends AbsNetClient{

    private com.squareup.okhttp.OkHttpClient mClient;

    public OkHttpClient() {
        mClient = new com.squareup.okhttp.OkHttpClient();
    }

    @Override
    public void start(final String aKey, String aUrl, Map<String, String> aHeader, final InetCallback aCallback) {
        final Request request = new Request.Builder()
                .url(aUrl)
                .headers(Headers.of(aHeader))
                .tag(aKey)
                .build();
        Call call = mClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                if (aCallback != null) {
                    aCallback.onFail(e.getMessage());
                }
            }

            @Override
            public void onResponse(Response response) throws IOException {
                try {
                    // get header
                    if (aCallback != null) {
                        Map<String, String> headersMap = new HashMap<>();
                        Headers headers = response.headers();
                        for (int i = 0; i < headers.size(); i++) {
                            headersMap.put(headers.name(i), headers.value(i));
                        }
                        aCallback.onstart(headersMap);
                    }
                    //downloading
                    InputStream is = response.body().byteStream();
                    if (aCallback != null) {
                        aCallback.onDownloading(is);
                    }

                    //success
                    if (aCallback != null) {
                        aCallback.onSuccess();
                    }

                } catch (Exception e) {
                    onFailure(request, new IOException(e));
                }
            }
        });
    }

    @Override
    public void stop(String aKey) {
        mClient.cancel(aKey);
    }

}
