package com.example.administrator.servicebestpractice;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

import java.io.File;

public class DownloadService extends Service {

    private DownloadTask downloadTask ;
    private String downloadUrl ;

    public DownloadService() {
    }//构造方法

    private DownloadListener listener = new DownloadListener() {
        @Override
        public void onProgress(int progress) {
            getNotificationManager().notify( 1 , getNotification( "Fucccck is downloading..." , progress )  );
        }

        @Override
        public void onSuccess() {
            downloadTask = null ;
            //下面的代码是下载成功时将前台服务通知关闭，并创建一个下载成功的通知
            stopForeground( true ) ;//关闭上一个前台服务
            getNotificationManager().notify( 1 , getNotification( "Download success" , -1 ) ) ;//调用notify()让通知显示出来
            Toast.makeText( DownloadService.this , "Download success" , Toast.LENGTH_SHORT ).show();
        }

        @Override
        public void onFailed() {
            downloadTask = null ;
            //下载失败后先关闭前台服务通知，再创建一个下载失败的通知
            stopForeground( true ) ;
            getNotificationManager( ).notify( 1 , getNotification( "Download failed" , -1 ) ) ;
            Toast.makeText( DownloadService.this , "Download failed" , Toast.LENGTH_SHORT ).show();
        }

        @Override
        public void onPaused() {
            downloadTask = null ;
            stopForeground( true ) ;
            getNotificationManager( ).notify( 1, getNotification( "Download paused " , -1 ) ) ;
            Toast.makeText( DownloadService.this , "Download paused " , Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCanceled() {
            downloadTask = null ;
            stopForeground( true ) ;
            getNotificationManager( ).notify( 1 , getNotification( "Download canceled" , -1 )  ) ;
            Toast.makeText( DownloadService.this , "Download canceled"  , Toast.LENGTH_SHORT  ).show();
        }
    } ;

    private DownloadBinder mBinder = new DownloadBinder() ;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mBinder ;
    }

    class DownloadBinder extends Binder {

        public void startDownload ( String url ) {
            if ( downloadTask == null ) {
                downloadUrl = url ;
                downloadTask = new DownloadTask( listener ) ;
                downloadTask.execute( downloadUrl ) ;//执行一个异步任务,开始下载
                startForeground( 1 , getNotification( "Downloading ... "  ,  0 ) );
                Toast.makeText( DownloadService.this , "Fuck is Downloading ... " , Toast.LENGTH_SHORT ).show();
            }
        }
        public void pauseDownload ( ) {
            if ( downloadTask != null ) {
                downloadTask.pauseDownload();
            }
        }
        public void cancelDownload () {
            if ( downloadTask != null ) {
                downloadTask.cancelDownload();
            }
            else if ( downloadUrl != null ) {
                String fileName = downloadUrl.substring( downloadUrl.lastIndexOf("/" ) ) ;
                //lastIndexOf()返回的是资格字符串到最后这里的字节数，返回的是一个int类型的；然后substring()的参数是一个int型，是从int那个位置开始往后读取，就从你选择的地方又继续往下面读取
                String directory = Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_DOWNLOADS ).getPath() ;
                //返回SD卡的目录，用于储存
                File file = new File( directory + fileName ) ;
                if ( file.exists() ) {
                    file.delete() ;
                }
                getNotificationManager().cancel( 1 ) ;//？
                stopForeground( true ) ;
                Toast.makeText( DownloadService.this , "Canceled" , Toast.LENGTH_SHORT  ) .show();
            }
        }
    }




    private NotificationManager getNotificationManager ( ) {
        return ( NotificationManager ) getSystemService( NOTIFICATION_SERVICE ) ;
    }

    private Notification getNotification ( String title , int progress ) {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setContentTitle(title);
        builder.setContentIntent(pi);
        if (progress > 0) {
            builder.setContentText(progress + "%");
            builder.setProgress(100, progress, false);//setProgress( 最大进度 , 已完成的进度 , 是否模糊进度条 )
        }
        return builder.build();
    }
}
