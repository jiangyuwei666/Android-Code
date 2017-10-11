package com.example.administrator.lbstset;

import android.Manifest;
import android.app.PendingIntent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.MyLocationConfiguration;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public LocationClient mLocationClient ;
    private TextView positionText ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLocationClient = new LocationClient( getApplicationContext() ) ;
        mLocationClient.registerLocationListener( new MylocationListener() ) ;
        setContentView( R.layout.activity_main ) ;
        positionText = ( TextView ) findViewById( R.id.position_text ) ;
        List < String > permissionList = new ArrayList<>( ) ;
        if (ContextCompat.checkSelfPermission( MainActivity.this , Manifest.permission.WRITE_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED ) {
            permissionList.add( Manifest.permission.WRITE_EXTERNAL_STORAGE ) ;
        }
        if (ContextCompat.checkSelfPermission( MainActivity.this , Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            permissionList.add( Manifest.permission.ACCESS_FINE_LOCATION ) ;
        }
        if ( ContextCompat.checkSelfPermission( MainActivity.this , Manifest.permission.READ_PHONE_STATE ) != PackageManager.PERMISSION_GRANTED ) {
            permissionList.add( Manifest.permission.READ_PHONE_STATE ) ;
        }
        if ( !permissionList.isEmpty() ) {
            String [] permissions = permissionList.toArray( new String[permissionList.size() ] ) ;
            ActivityCompat.requestPermissions( MainActivity.this , permissions , 1 ) ;
        }
        else {
            requestLocation();
        }
    }

    private void requestLocation ( ) {
        mLocationClient.start();
    }

    @Override
    public void onRequestPermissionsResult ( int requestCode , String[] permissions , int [ ] grantResults ) {
        switch ( requestCode ) {
            case 1 :
                if ( grantResults.length > 0 ) {
                    for ( int result : grantResults ){
                        if ( result != PackageManager.PERMISSION_GRANTED ) {
                            Toast.makeText( this , "莫得权限的嘛" , Toast.LENGTH_SHORT ).show();
                            finish();
                            return ;
                        }
                    }
                    requestLocation();
                }
                else {
                    Toast.makeText( this , "??????" , Toast.LENGTH_SHORT ).show();
                    finish();
                }
                break;
            default:
                break;

        }
    }

    public class MylocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation ( BDLocation location ) {}

    }

}
