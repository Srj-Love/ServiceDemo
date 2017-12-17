package com.srjlove.servicedemo.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.util.Log;

import com.srjlove.servicedemo.R;

/**
 * Created by Suraj on 12/16/2017.
 */

public class MyIntentService extends IntentService {

    private static final String TAG = "MyIntentService";
    public MyIntentService() {
        super("MyIntentService"); // within a super we need to name our BackGroung thread or worker thread
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: Thread Name: "+ Thread.currentThread().getName());
    }

    // IntentService also provide onStartCommand, onBind but not needed in IntentService

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.i(TAG, "onHandleIntent: Thread Name: " + Thread.currentThread().getName());
        assert intent != null;
        int i = intent.getIntExtra(getString(R.string.sleep), 1);
        ResultReceiver mReceiver = intent.getParcelableExtra(getString(R.string.receiver));
        int ctr =1;
        while (ctr<=i){
            Log.i(TAG, "onHandleIntent: Counter is now :"+ ctr);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ctr++;
        }
        Bundle mBundle = new Bundle();
        mBundle.putString(getString(R.string.bundle_result), "Counter stopped at "+ ctr+ " second");
        mReceiver.send(25,mBundle);// can send d result back to activity through ResultReceiver, BroadcastReciever,
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: Thread Name: "+ Thread.currentThread().getName());
    }
}
