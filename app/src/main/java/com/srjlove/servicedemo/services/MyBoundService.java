package com.srjlove.servicedemo.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Suraj on 12/16/2017.
 *
 */


public class MyBoundService extends Service {

    /**
     * we can't use local service to communicate with different components process with IPC (Inter Process Communication).
     * If want to Instead use Messanger or AIDL (Android Interface Definition Language)
     */
    public MyLocalBinder myLocalBinder = new MyLocalBinder();


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myLocalBinder; //come first here
    }

     public class MyLocalBinder extends Binder {
        public MyBoundService getService(){
            return MyBoundService.this;
        }
    }

    public int add(int a, int b){
        return a+b;
    }

    public int delete(int a, int b){
        return a-b;
    }

    public int multiply(int a, int b){
        return a*b;
    }

    public float divide(int a, int b){
        return (float) a/b;
    }
}
