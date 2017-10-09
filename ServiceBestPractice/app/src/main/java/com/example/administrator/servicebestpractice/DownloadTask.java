package com.example.administrator.servicebestpractice;

import android.os.AsyncTask;
import android.os.Environment;

import java.io.Externalizable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
            long contentLength = getContentLength( downloadUrl ) ;//这个要先写完getContentLength方法，不然会报错
            if ( contentLength == 0 ) {
                return TYPE_FAILED ;
            }
            else if ( contentLength == downloadedLength ) {
                return TYPE_SUCCESS ;//说明下载完成了，返回TYPE_SUCCESS
            }
            OkHttpClient client = new OkHttpClient() ;
            Request request = new Request.Builder()
                    .addHeader( "RANGE" , "bytes=" + downloadedLength + "-")
                    .url( downloadUrl )
                    .build() ;
            Response response = client.newCall( request ).execute() ;//有错？
            if ( response != null ) {
                is = response.body().byteStream() ;
                saveFile = new RandomAccessFile( file , "rw" ) ;//有错？
                saveFile.seek( downloadedLength ) ;//有错？上面三个地方有错是否是因为这些方法会抛出异常，但是还没有写catch或者exception语句
                byte[] b = new byte[ 2014 ] ;
                int total = 0 ;
                int len ;
                while ( ( len = is.read( b ) ) != -1 ) {
                    if ( isCanceled ) {
                        return TYPE_CANCELD ;
                    }
                    if ( isPaused ) {
                        return TYPE_PAUSED ;
                    }
                    else{
                        total += len ;
                        saveFile.write( b , 0 , len );
                        int progress = ( int ) ( ( total + downloadedLength ) * 100 / contentLength ) ;
                        publishProgress( progress ) ;//publishProgress这个方法是用来通知下载进度
                    }
                }
                response.body().close();
                return TYPE_SUCCESS ;
            }
        }
        catch ( Exception e ) {
            e.printStackTrace();
        }
        finally {
            try{
                if ( is != null ) {
                    is.close();
                }
                else if ( saveFile != null ) {
                    saveFile.close();
                }
                else if ( isCanceled && file != null ) {
                    file.delete() ;
                }
            }
            catch ( Exception e ) {
                e.printStackTrace();
            }
        }
        return TYPE_FAILED ;
    }//在后台执行下载

    @Override
    protected void onProgressUpdate( Integer ... value ) {
        int progress = value [ 0 ] ;
        if ( progress > lastProgress ) {
            listener.onProgress( progress ) ;
            lastProgress = progress ;
        }
    }//这个方法用于更新当前的下载进度

    @Override
    protected void onPostExecute ( Integer status ) {
        switch ( status ) {
            case TYPE_CANCELD:
                listener.onCanceled();
                break;
            case TYPE_FAILED:
                listener.onFailed();
                break;
            case TYPE_PAUSED:
                listener.onPaused();
                break;
            case TYPE_SUCCESS:
                listener.onSuccess();
                break;
            default:
                break;
        }
    }//通知最终的下载结果

    public void pauseDownload () {
        isPaused = true ;
    }

    public void cancelDownload ( ) {
        isCanceled = true ;
    }

    private long getContentLength ( String downloadUrl ) throws IOException {

    }
}
