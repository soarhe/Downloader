package com.soarhe.downloader.utils;

import android.os.Environment;

import com.soarhe.downloader.task.TaskInfo;

/**
 * Created by baidu on 16/6/17.
 */
public class Utils {

    public static String getFilenamebyInfo(TaskInfo aInfo) {
        return "test.apk";
    }

    public static String getDefaultPath() {
        if (Environment.isExternalStorageEmulated()) {
            return Environment.getExternalStoragePublicDirectory("sDownload").toString();
        } else {
            return null;
        }
    }
}
