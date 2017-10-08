package com.example.administrator.myservice;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v7.app.NotificationCompat;
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
        Log.d("Myservice", "onCreate executed");
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        Notification notification = new NotificationCompat.Builder( this )
                .setContentTitle( "fuccccck send a message" )
                .setContentText( "nice to meet you")
                .setWhen( System.currentTimeMillis() )
                .setSmallIcon( R.mipmap.ic_launcher )
                .setLargeIcon(BitmapFactory.decodeResource( getResources() , R.mipmap.ic_launcher ) )
                .setContentIntent( pi )
                .build() ;
        startForeground( 1 , notification ) ;
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
