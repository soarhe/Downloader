package com.soarhe.servicebroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by baidu on 16/7/6.
 */
public class TestReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("soar", "receive Broadcast " + intent.toString());
    }
}
