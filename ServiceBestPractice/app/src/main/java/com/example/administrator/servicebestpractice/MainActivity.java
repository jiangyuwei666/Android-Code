package com.example.administrator.servicebestpractice;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DownloadService.DownloadBinder downloadBinder ;//先创建一个用来绑定服务的DownloadBinder类实例,因为DownloadBinder是DownloadService中的匿名内部类,所以要这样创建
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            downloadBinder = ( DownloadService.DownloadBinder ) service ;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button start = ( Button ) findViewById( R.id.start_download ) ;
        Button pause = ( Button ) findViewById( R.id.pause_download ) ;
        Button cancel = ( Button ) findViewById( R.id.cancel_download ) ;
        start.setOnClickListener( this ) ;
        pause.setOnClickListener( this ) ;
        cancel.setOnClickListener( this ) ;
        Intent intent = new Intent( this , DownloadService.class ) ;
        startService( intent ) ;
        bindService( intent , connection , BIND_AUTO_CREATE ) ;
        if (ContextCompat.checkSelfPermission( MainActivity.this , Manifest.permission.WRITE_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions( MainActivity.this , new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE } , 1 ) ;
        }
    }

    @Override
    public void onClick ( View v ) {
        if ( downloadBinder == null ) {
            return ;
        }
        switch ( v.getId() ) {
            case R.id.start_download :
                String url = "http://raw.githubusercontent.com/guolindev/eclipse/master/eclipse-inst-win64.exe" ;
                downloadBinder.startDownload( url ) ;
                break;
            case R.id.pause_download :
                downloadBinder.pauseDownload();
                break;
            case R.id.cancel_download :
                downloadBinder.cancelDownload();
                break;
            default:
                break;
        }
    }

    //因为要下载到本地的磁盘，所以必须要申请sd卡的权限
    @Override
    public void onRequestPermissionsResult( int requestCode , String [] permissions , int[] grantRequests ) {
        switch ( requestCode ) {
            case 1 :
                if ( grantRequests.length > 0 && grantRequests[0] != PackageManager.PERMISSION_GRANTED ) {
                    Toast.makeText( this , "你他吗别拒绝啊，拒绝了用nmh" , Toast.LENGTH_SHORT ).show() ;
                    finish() ;//关闭应用
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy () {
        super.onDestroy();
        unbindService( connection ) ;//解绑。解绑绑定都用的ServiceConnetion对象
    }
}
