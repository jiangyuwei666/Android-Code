package com.example.administrator.servicebestpractice;

import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

/**
 * Created by Administrator on 2017/10/9.
 */

public class DownloadTask extends AsyncTask < String , Integer , Integer > {
    public static final int TYPE_SUCCESS = 0 ;
    public static final int TYPE_FAILED = 1 ;
    public static final int TYPE_PAUSED = 2 ;
    public static final int TYPE_CANCELD = 3 ;

    private DownloadListener listener ;

    private boolean isCanceled = false ;
    private  boolean isPaused = false ;
    private int lastProgress ;

    public DownloadTask ( DownloadListener listener ) {
        this.listener = listener ;
    }

    @Override
    protected Integer doInbackground ( String ... params ) {
        InputStream is = null ;
        RandomAccessFile saveFile = null ;
        File file = null ;
        try{
            long downloadedLength = 0 ;
            String downloadUrl = params[ 0 ] ;
            String fileName = downloadUrl.substring( downloadUrl.lastIndexOf( "/" ) ) ;
            String directory = Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_DOWNLOADS ).getParent() ;
            file = new FIle ( directory + fileName ) ;
            if ( file.exists() ) {
                downloadedLength = file.length() ;
            }
            long contentLength = getContentLength( downloadUrl ) ;
            if ( file.exists() ) {
                downloadedLength = file.length() ;
            }
        }
    }//在后台执行下载

    @Override
    protected void onProgressUpdate( Integer ... value ) {}//这个方法用于更新当前的下载进度

    @Override
    protected void onPostExecute ( Integer status ) {}//通知最终的下载结果
    private long getContentLength ( String downloadUrl ) throws IOException {

    }
}
