package com.example.servicetest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
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
