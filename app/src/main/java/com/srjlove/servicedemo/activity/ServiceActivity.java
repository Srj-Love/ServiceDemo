package com.srjlove.servicedemo.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.srjlove.servicedemo.R;
import com.srjlove.servicedemo.services.MyIntentService;
import com.srjlove.servicedemo.services.MyStartedService;

public class ServiceActivity extends AppCompatActivity {

    private static final String TAG = "ServiceActivity";
    private TextView tvIntentResult;
    private TextView tvStartedResult;
    Handler mHandler = new Handler();

    // using Broadcast Receiver to communicate with Started Services
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //get the intent comming from started_service_result
            String result = intent.getStringExtra(getString(R.string.started_service_results));
            tvStartedResult.setText(result);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        tvStartedResult = findViewById(R.id.tvStartedServiceResult);
        tvIntentResult = findViewById(R.id.tvIntentServiceResult);
    }

    public void btnStartStartedServicce(View view) {

        startService(new Intent(this, MyStartedService.class).putExtra(getString(R.string.sleep), 10));
        // don't forget to add it in manifest
    }

    public void btnStopStartedServicce(View view) {
        //ResultReceiver mReceiver = new MyResultReciever(null);
        stopService(new Intent(this, MyStartedService.class));
        Log.i(TAG, "btnStopStartedServicce: service stopped");
    }

    public void startIntentService(View view) {
        Log.d(TAG, "startStartedService: button clicked");
        ResultReceiver mReceiver = new MyResultReciever(null);// mReceiver is a Parcelable Object
        startService(new Intent(this, MyIntentService.class).putExtra(getString(R.string.sleep), 10).putExtra(getString(R.string.sleep), 10).putExtra(getString(R.string.receiver), mReceiver));
    }

    @Override
    protected void onResume() {
        super.onResume();
        // registering Broadcast Receiver dynamically  with an activity
        IntentFilter mFilter = new IntentFilter("action.service.to.activity"); // user defined action
        registerReceiver(mBroadcastReceiver, mFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mBroadcastReceiver);// explicitly underestimation
    }

    public void gotoBoundService(View view) {
        startActivity(new Intent(this, SecondActivity.class));
    }

    public void gotoMessangerActivity(View view) {
        startActivity(new Intent(this, MyMessangerActivity.class));
    }


    private class MyResultReciever extends ResultReceiver {


        MyResultReciever(Handler handler) {
            super(handler);
        }

        /* onReceiveResult will run in a WORKER THREAD  dont' do any MAIN UI Operations over HERE instead, use Handler*/
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if (resultCode == 25 && resultData != null) {
                final String result = resultData.getString(getString(R.string.bundle_result));
                // Toast.makeText(ServiceActivity.this, "Result : "+ result, Toast.LENGTH_SHORT).show();
                // tvIntentResult.setText(result);// rememmber srj, can't acces through MAIN UI
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        tvIntentResult.setText(result); // can Access MAIN UI through HANDLER
                    }
                });
                Log.i(TAG, "onReceiveResult: Result:" + result);
            }
        }
    }

}
