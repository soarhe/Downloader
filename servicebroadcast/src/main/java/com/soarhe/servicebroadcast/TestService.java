package com.soarhe.servicebroadcast;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by baidu on 16/7/6.
 */
public class TestService extends Service {

    TestReceiver mReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("soar", "register receiver");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mReceiver == null) {
            mReceiver = new TestReceiver();
            IntentFilter filter = new IntentFilter("com.soarhe.testbroad");
            this.registerReceiver(mReceiver, filter);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) {
            this.unregisterReceiver(mReceiver);
            mReceiver = null;
        }
        Log.d("soar", "unregister receiver");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
