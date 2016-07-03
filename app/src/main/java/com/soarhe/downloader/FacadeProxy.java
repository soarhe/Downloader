package com.soarhe.downloader;

import android.content.ContentProvider;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;

import com.soarhe.downloader.inter.DownloadService;
import com.soarhe.downloader.inter.UpdateProvider;
import com.soarhe.downloader.task.TaskInfo;

/**
 * Created by hejunwei on 16/6/13.
 */
public class FacadeProxy {

    private static FacadeProxy sInstance;
    private boolean mInited;
    private Context mContext;

    private FacadeProxy() {
        mInited = false;
    }

    public static FacadeProxy getInstance() {
        if (sInstance == null) {
            synchronized (FacadeProxy.class) {
                if (sInstance == null) {
                    sInstance = new FacadeProxy();
                }
            }
        }
        return sInstance;
    }

    public void init(Context aContext) {
        if (!mInited) {
            mContext = aContext.getApplicationContext();
            mInited = true;
            mContext.getContentResolver().registerContentObserver(UpdateProvider.CONTENT_URI,
                    true,
                    new ContentObserver(new Handler()) {
                        @Override
                        public void onChange(boolean selfChange) {
                            super.onChange(selfChange);
                        }

                        @Override
                        public void onChange(boolean selfChange, Uri uri) {
                            super.onChange(selfChange, uri);
                        }

                        @Override
                        public boolean deliverSelfNotifications() {
                            return super.deliverSelfNotifications();
                        }
                    }
            );
        }
    }

    // actions
    public String start(TaskInfo aInfo) {
        Intent intent = new Intent(mContext, DownloadService.class);
        intent.setAction(DownloadService.ACTION_START);
        intent.putExtra("data", aInfo);
        mContext.startService(intent);
        return aInfo.mKey;
    }

    public void pause(String aKey) {
        Intent intent = new Intent(mContext, DownloadService.class);
        intent.setAction(DownloadService.ACTION_PAUSE);
        intent.putExtra("key", aKey);
        mContext.startService(intent);
    }

    public void resume(String aKey) {
        Intent intent = new Intent(mContext, DownloadService.class);
        intent.setAction(DownloadService.ACTION_RESUME);
        intent.putExtra("key", aKey);
        mContext.startService(intent);
    }

    public void cancel(String aKey) {
        Intent intent = new Intent(mContext, DownloadService.class);
        intent.setAction(DownloadService.ACTION_CANCEL);
        intent.putExtra("key", aKey);
        mContext.startService(intent);
    }

    // gets
    public TaskInfo getDownloadInfo(String aKey) {
        return null;
    }

    // observer
    public void setObserver() {

    }
}
