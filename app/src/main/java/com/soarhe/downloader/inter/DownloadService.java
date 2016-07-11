package com.soarhe.downloader.inter;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.soarhe.downloader.task.TaskInfo;

/**
 * Created by hejunwei on 16/6/19.
 */
public class DownloadService extends Service {

    public static final String ACTION_START = "com.soarhe.downloader.action.start";
    public static final String ACTION_PAUSE = "com.soarhe.downloader.action.pause";
    public static final String ACTION_CANCEL = "com.soarhe.downloader.action.cancel";
    public static final String ACTION_RESUME = "com.soarhe.downloader.action.resume";

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals(ACTION_START)) {
            TaskInfo info = intent.getParcelableExtra("data");
            if (info != null) {
                ServiceFacade.getInstance().start(info);
            }
        } else if (intent.getAction().equals(ACTION_PAUSE)) {
            String key = intent.getStringExtra("key");
            if (!TextUtils.isEmpty(key)) {
                ServiceFacade.getInstance().pause(key);
            }
        } else if (intent.getAction().equals(ACTION_CANCEL)) {
            String key = intent.getStringExtra("key");
            if (!TextUtils.isEmpty(key)) {
                ServiceFacade.getInstance().cancel(key);
            }
        } else if (intent.getAction().equals(ACTION_RESUME)) {
            String key = intent.getStringExtra("key");
            if (!TextUtils.isEmpty(key)) {
                ServiceFacade.getInstance().resume(key);
            }
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ServiceFacade.getInstance().release();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
