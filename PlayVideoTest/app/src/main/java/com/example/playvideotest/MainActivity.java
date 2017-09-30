package com.example.playvideotest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.os.EnvironmentCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private VideoView videoView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button play = ( Button ) findViewById( R.id.play ) ;
        Button pause = ( Button ) findViewById( R.id.pause ) ;
        Button replay = ( Button ) findViewById( R.id.replay ) ;
        videoView = ( VideoView ) findViewById( R.id.video ) ;
        play.setOnClickListener( this ) ;
        pause.setOnClickListener( this ) ;
        replay.setOnClickListener( this ) ;
        if (ContextCompat.checkSelfPermission( MainActivity.this , Manifest.permission.WRITE_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions( MainActivity.this , new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE } , 1 ) ;
        }
        else {
            initVideoPath ( );
        }
    }

    private void initVideoPath () {
        File file = new File(Environment.getExternalStorageDirectory() , "the killer's saver.mp4" ) ;
        videoView.setVideoPath( file.getPath() ) ;
    }

    @Override
    public void onRequestPermissionsResult ( int requestCode , String[] permissions , int[] grantResults ) {
        switch ( requestCode ) {
            case 1 :

                if ( grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
                    initVideoPath();
                }else {
                    Toast.makeText( this , "u have no permission sb!" , Toast.LENGTH_SHORT ).show();
                    finish();
                }
        }
    }

    @Override
    public void onClick ( View v ) {
        switch ( v.getId() ) {
            case R.id.play :
                Toast.makeText( this , "u click play" , Toast.LENGTH_SHORT ).show();
                if ( !videoView.isPlaying() ) {
                    videoView.start();
                }
                break;
            case R.id.pause :
                if ( videoView.isPlaying() ) {
                    videoView.pause();
                }
                break;
            case R.id.replay :
                if ( videoView.isPlaying() ) {
                    videoView.resume();
                }
                break;
        }
    }

    @Override
    public void onDestroy ( ) {
        super.onDestroy();
        if ( videoView != null ) {
            videoView.suspend();
        }
    }


}
