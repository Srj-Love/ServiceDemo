package com.srjlove.servicedemo.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.srjlove.servicedemo.R;
import com.srjlove.servicedemo.services.MyMessangerService;

public class MyMessangerActivity extends AppCompatActivity {

    private static final String TAG = "MyMessangerActivity";
    private TextView tvResult;
    private boolean isBinded = false;
    private Messenger messenger = null;
    Messenger myIncomingMSG = new Messenger(new IncomingResponseHandler());


    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            messenger = new Messenger(service);//solved problem
            isBinded = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBinded = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messanger);

        tvResult = findViewById(R.id.tvMresult);
    }

    public void performAddOperation(View view) {

        EditText etOne = findViewById(R.id.etMone);
        EditText etTwo = findViewById(R.id.etMtwo);
        int one = Integer.parseInt(etOne.getText().toString());
        int two = Integer.parseInt(etTwo.getText().toString());

        Message msgToService = Message.obtain(null, 44);

        Bundle mBundle = new Bundle();
        mBundle.putInt(getString(R.string.et_one), one);
        mBundle.putInt(getString(R.string.et_two), two);

        msgToService.setData(mBundle);
        msgToService.replyTo = myIncomingMSG; // I want result back to this component/thread/process

        if (isBinded) {
            try {
                messenger.send(msgToService); // send data to a service class

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Bind first to add numbers", Toast.LENGTH_SHORT).show();
        }

    }

    public void bindService(View view) {

        bindService(new Intent(MyMessangerActivity.this, MyMessangerService.class), mConnection, BIND_AUTO_CREATE);
        Log.i(TAG, "onServiceConnected: service binded successfully");
        Toast.makeText(this, "service binded successfully", Toast.LENGTH_SHORT).show();
    }

    public void unbinddService(View view) {
        if (isBinded) {
            unbindService(mConnection);
            isBinded = false;
            Log.i(TAG, "unbinddService: service unbided successfully");
            Toast.makeText(this, "service unbinded successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Bind first to unbind services", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "unbinddService: service unbind failed");
        }

    }

    class IncomingResponseHandler extends Handler { // To receive data back from Service

        @Override
        public void handleMessage(Message msgFromService) {
            Log.i(TAG, "handleMessage: attempt to match");

            switch (msgFromService.what) {

                case 87:

                    Bundle bundle = msgFromService.getData();
                    int result = bundle.getInt("res", 0);

                    tvResult.setText("Result: " + result);
                    break;

                default:
                    super.handleMessage(msgFromService);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(mConnection);
    }
}
