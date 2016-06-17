package com.soarhe.downloader.utils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by baidu on 16/6/13.
 */
public final class Headers {

    private Map<String, String> mHeaderMap;

    public Headers() {
        mHeaderMap = new HashMap<>();
    }

    public String get(String aKey) {
        return mHeaderMap.get(aKey);
    }

    public void put(String aKey, String aValue) {
        mHeaderMap.put(aKey, aValue);
    }

    public com.squareup.okhttp.Headers toOkHeaders() {
        return com.squareup.okhttp.Headers.of(mHeaderMap);
    }

    public Map<String, String> toMap() {
        return mHeaderMap;
    }

    public String toString() {
        JSONObject obj = new JSONObject();
        try {
            for (String key : mHeaderMap.keySet()) {
                obj.putOpt(key, mHeaderMap.get(key));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj.toString();
    }

    public static Headers from(String aJsonString) {
        Headers headers = new Headers();
        try {
            JSONObject obj = new JSONObject(aJsonString);
            Iterator<String> iter = obj.keys();
            while (iter.hasNext()) {
                String key = iter.next();
                headers.put(key, obj.getString(key));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return headers;
    }
}
