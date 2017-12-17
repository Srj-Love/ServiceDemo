package com.srjlove.servicedemo.services;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.srjlove.servicedemo.R;

/**
 * Created by Suraj on 12/17/2017.
 */

public class MyMessangerService extends Service {
    private static final String TAG = "MyMessangerService";
    Messenger messenger = new Messenger(new IncomingHandler()); // create new Handler


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder(); // same as BindService() simly returns instance of binder object
    }

    // this IncomingHandler handler will simply handle amsg from a different process or the thread
    private class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msgFromActivity) {
            switch (msgFromActivity.what) { // msg.what: will give the CONSTANT Value
                case 44:

                    Bundle mBundle = msgFromActivity.getData();
                    int one = mBundle.getInt(getString(R.string.et_one), 1);
                    int two = mBundle.getInt(getString(R.string.et_two), 1);
                    Toast.makeText(MyMessangerService.this, "Result: " + add(one, two), Toast.LENGTH_SHORT).show();

                    int result = add(one, two);

                    // Send data back to the Activity
                    Messenger incomingMessenger = msgFromActivity.replyTo;
                    Message msgToActivity = Message.obtain(null, 87);

                    Bundle bundleToActivity = new Bundle();
                    bundleToActivity.putInt("res", result);
                    msgToActivity.setData(bundleToActivity);


                    try {
                        messenger.send(msgToActivity);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                    break;
                default:
                    super.handleMessage(msgFromActivity);
            }

        }
    }

    public int add(int a, int b) {
        return a + b;
    }

}
