package com.example.servicetest;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public MyService.DownloadBinder downloadBinder ;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            downloadBinder = ( MyService.DownloadBinder ) iBinder ;
            downloadBinder.startDownload();
            downloadBinder.getProgress() ;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    } ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button start = ( Button ) findViewById( R.id.start_service ) ;
        Button stop = ( Button ) findViewById( R.id.stop_service ) ;
        start.setOnClickListener( this ) ;
        stop.setOnClickListener( this ) ;
        Button bind = ( Button ) findViewById( R.id.bind ) ;
        Button unbind = ( Button ) findViewById( R.id.unbind ) ;
        bind.setOnClickListener( this ) ;
        unbind.setOnClickListener( this );
    }

    @Override
    public void onClick ( View v ) {
        switch ( v.getId() ) {
            case R.id.start_service :
                Intent startIntent = new Intent ( MainActivity.this , MyService.class ) ;
                startService( startIntent ) ;
                break;
            case R.id.stop_service :
                Intent stopIntent = new Intent ( MainActivity.this , MyService.class ) ;
                stopService ( stopIntent  ) ;
                break;
            case R.id.bind :
                Intent bindIntent = new Intent ( this , MyService.class ) ;
                bindService( bindIntent , connection , BIND_AUTO_CREATE ) ;
                break;
            case R.id.unbind :
                unbindService( connection ) ;
            default:
                break ;
            }
        }


}
