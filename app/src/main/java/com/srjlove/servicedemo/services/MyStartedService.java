package com.srjlove.servicedemo.services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.srjlove.servicedemo.R;

/**
 * Created by Suraj on 12/15/2017.
 */

public class MyStartedService extends Service {

    private static final String TAG = "MyStartedService";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: Thread Name = " + Thread.currentThread().getName());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind: Thread Name = " + Thread.currentThread().getName());
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand: Thread Name = " + Thread.currentThread().getName());

        new MyTask().execute(intent.getIntExtra(getString(R.string.sleep), 1));
        return START_NOT_STICKY;

    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: Thread Name = " + Thread.currentThread().getName());
        super.onDestroy();
    }

    private  class MyTask extends AsyncTask<Integer, String, String> {
        private static final String TAG = "MyTask";

        @Override
        protected String doInBackground(Integer... integers) {
            int i = 1;

            while (i <= integers[0]) {
                try {
                    publishProgress("Counter is now " + i);// give results in string
                      Thread.sleep(1000);
                     // Thread.sleep(integers[0]);
                    Log.i(TAG, "doInBackground: Thread Name = " + Thread.currentThread().getName());

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i++;
            }
            return "Counter  stopped at " + i+ " seconds";

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i(TAG, "onPreExecute: Thread Name = " + Thread.currentThread().getName());
        }

        @Override
        protected void onPostExecute(String  s) {
            super.onPostExecute(s);
            stopSelf();
            Log.i(TAG, "onPostExecute: Thread Name = " + Thread.currentThread().getName());
            Intent i = new Intent("action.service.to.activity").putExtra(getString(R.string.started_service_results), s);
            sendBroadcast(i);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Toast.makeText(MyStartedService.this, "Count: " + values[0], Toast.LENGTH_SHORT).show();
            Log.i(TAG, "onProgressUpdate: Thread Name = " + Thread.currentThread().getName() + "Count: " + values[0]);
        }
    }

}
