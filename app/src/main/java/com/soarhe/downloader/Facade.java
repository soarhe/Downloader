package com.soarhe.downloader;

/**
 * Created by hejunwei on 16/6/13.
 */
public final class Facade implements INoProguard{

    private static Facade sInstance;
    private boolean mInited;

    private Facade() {
        mInited = false;
    }

    public static Facade getInstance() {
        if (sInstance == null) {
            synchronized (sInstance) {
                if (sInstance == null) {
                    sInstance = new Facade();
                }
            }
        }
        return sInstance;
    }

}
