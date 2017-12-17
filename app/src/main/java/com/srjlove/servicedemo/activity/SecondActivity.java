package com.srjlove.servicedemo.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.srjlove.servicedemo.R;
import com.srjlove.servicedemo.services.MyBoundService;

/**
 * Created by Suraj on 12/16/2017.
 */

public class SecondActivity extends AppCompatActivity {

    private boolean isBunded = false;

    private MyBoundService myBoundService;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            MyBoundService.MyLocalBinder myLocalBinder = (MyBoundService.MyLocalBinder) iBinder;
            myBoundService = myLocalBinder.getService();
            isBunded = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBunded = false;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindService(new Intent(this, MyBoundService.class), mConnection, BIND_AUTO_CREATE);
    }

    public void btnEvent(View view) {
        EditText etOne = findViewById(R.id.etOne);
        EditText etOTwo = findViewById(R.id.etTwo);

        int first = Integer.parseInt(etOne.getText().toString());
        int second = Integer.parseInt(etOTwo.getText().toString());

        String result = "";
        if (isBunded) {
            switch (view.getId()) {
                case R.id.btnAdd:
                    result = String.valueOf(myBoundService.add(first, second));
                    break;
                case R.id.btnDelete:
                    result = String.valueOf(myBoundService.delete(first, second));
                    break;
                case R.id.btnMultiply:
                    result = String.valueOf(myBoundService.multiply(first, second));
                    break;
                case R.id.btnDevide:
                    result = String.valueOf(myBoundService.divide(first, second));
                    break;
            }
        }
        TextView tv = findViewById(R.id.txtResult);
        tv.setText(result);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isBunded)
            unbindService(mConnection);
        isBunded= false;
    }
}
