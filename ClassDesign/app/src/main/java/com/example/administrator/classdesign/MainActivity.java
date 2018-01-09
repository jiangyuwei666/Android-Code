package com.example.administrator.classdesign;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton btnChoseFile ;
    Button btnTranscode ;
    Button btnTransback ;
    FloatingActionButton btnCamera ;
    TextView tvTranscode ;
    TextView tvTransback ;
    String transcode ;
    String transback ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnCamera = ( FloatingActionButton ) findViewById( R.id.button_camera ) ;
        btnChoseFile = ( ImageButton ) findViewById( R.id.button_chose_file ) ;
        btnTranscode = ( Button ) findViewById( R.id.button_transcode ) ;
        btnTransback = ( Button ) findViewById( R.id.button_transback ) ;
        tvTranscode = ( TextView ) findViewById( R.id.text_view_transcode ) ;
        tvTransback = ( TextView ) findViewById( R.id.text_view_transback ) ;
        btnChoseFile.setOnClickListener( this );
        btnTranscode.setOnClickListener( this );
        btnTransback.setOnClickListener( this );
        btnCamera.setOnClickListener( this );
        request();
    }

    @Override
    public void onClick(View v) {
        switch ( v.getId() ) {
            case R.id.button_camera :
                Toast.makeText( this  , "sb赵笠志点击了相机" , Toast.LENGTH_SHORT ).show();
//                clickCamera();
                break;
            case R.id.button_chose_file :
                Toast.makeText( this , "sb赵笠志点击了文件" , Toast.LENGTH_SHORT ).show();
//                clickChoseFile();
                break;
            case R.id.button_transcode :
                Toast.makeText( this , "sb赵笠志点击了转码" , Toast.LENGTH_SHORT ).show();
//                tvTranscode.setText( transcode ) ;//transcode赋值为返回的编码
                break;
            case R.id.button_transback :
                Toast.makeText( this , "sb赵笠志点击了恢复转码" , Toast.LENGTH_SHORT ).show();
//                tvTransback.setText( transback ) ;//transback赋值为转回去的字符串
                break;
            default:
        }
    }

    /**
     * 点击相机调用的方法
     */
    public void clickCamera () {}

    /**
     * 点击选择文件的方法
     */
    public void clickChoseFile () {}

    public void request( ) {
        List<String> permissionList = new ArrayList<>() ;
        if ( ContextCompat.checkSelfPermission( MainActivity.this , Manifest.permission.WRITE_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED ) {
            permissionList.add( Manifest.permission.WRITE_EXTERNAL_STORAGE ) ;
        }
        if ( ContextCompat.checkSelfPermission( MainActivity.this , Manifest.permission.ACCESS_WIFI_STATE ) != PackageManager.PERMISSION_GRANTED ) {
            permissionList.add( Manifest.permission.ACCESS_WIFI_STATE );
        }
        if ( ContextCompat.checkSelfPermission( MainActivity.this , Manifest.permission.CAMERA ) != PackageManager.PERMISSION_GRANTED ) {
            permissionList.add( Manifest.permission.CAMERA );
        }
        if ( ContextCompat.checkSelfPermission( MainActivity.this , Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS ) != PackageManager.PERMISSION_GRANTED ) {
            permissionList.add( Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS );
        }
        if ( ContextCompat.checkSelfPermission( MainActivity.this , Manifest.permission.INTERNET ) != PackageManager.PERMISSION_GRANTED ) {
            permissionList.add( Manifest.permission.INTERNET );
        }
        if ( ContextCompat.checkSelfPermission( MainActivity.this , Manifest.permission.ACCESS_NETWORK_STATE ) != PackageManager.PERMISSION_GRANTED ) {
            permissionList.add( Manifest.permission.ACCESS_NETWORK_STATE );
        }
        if ( !permissionList.isEmpty() ) {
            String[] permissions = permissionList.toArray( new String[ permissionList.size() ] ) ;
            ActivityCompat.requestPermissions( MainActivity.this , permissions , 1 ) ;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch ( requestCode ) {
            case 1 :
                if( grantResults.length > 0 ) {
                    for ( int result : grantResults ) {
                        if ( result != PackageManager.PERMISSION_GRANTED ) {
                            Toast.makeText(MainActivity.this , "请允许申请必要的权限哟~" , Toast.LENGTH_SHORT ).show();
                            return;
                        }
                    }
                }
        }
    }

}
