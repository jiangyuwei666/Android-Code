package com.example.servicetest;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {

    private DownloadBinder binder = new DownloadBinder() ;

    class DownloadBinder extends Binder {

        public void startDownload () {
            Log.d( "MyService" , "startDownload executed" ) ;
        }

        public int getProgress () {
            Log.d( "MyService" , "getProgress executed" ) ;
            return 0 ;
        }


    }

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return binder ;
    }

    @Override
    public void onCreate ( ) {
        super.onCreate();
        Log.d( "Myservice" , "onCreate executed" ) ;
    }

    @Override
    public int onStartCommand( Intent intent , int flags , int startId ) {
        Log.d( "Myservice" , "onStartCommand executed" ) ;
        return super.onStartCommand( intent , flags , startId ) ;
    }

    @Override
    public void onDestroy ( ) {
        Log.d( "Myservice" , "onDestroy executed" ) ;
        super.onDestroy();
    }

}
