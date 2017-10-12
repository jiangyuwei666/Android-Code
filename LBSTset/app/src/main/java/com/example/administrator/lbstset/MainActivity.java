package com.example.administrator.lbstset;

import android.Manifest;
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
import com.baidu.location.LocationClientOption;
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
        }//判断如果不是空就把集合转换成数组，再在后面的RequestPermissionsResult方法中设置权限
        else {
            requestLocation();
        }
    }

    private void requestLocation ( ) {
        initLocation() ;
        mLocationClient.start();
    }

    @Override
    protected void onDestroy () {
        super.onDestroy();
        mLocationClient.stop();
    }

    protected void initLocation () {
        LocationClientOption option = new LocationClientOption( ) ;
        option.setLocationMode( LocationClientOption.LocationMode.Device_Sensors ) ;
        option.setScanSpan( 3000 ) ;//时间为3000毫秒
        option.setIsNeedAddress( true ) ;//如果不调用这个方法并把参数设置成true，下面的国家等详细参数就全是null
        mLocationClient.setLocOption( option ) ;
    }//初始化，更新位置

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
    }//用一个循环加如权限

    public class MylocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation ( BDLocation location ) {
            StringBuilder currentPosition = new StringBuilder( ) ;
            currentPosition.append( "纬度：" ).append(location.getLatitude() ).append("\n") ;
            currentPosition.append( "经度：" ).append( location.getLongitude() ) .append("\n" ) ;
            currentPosition.append( "定位方式" );
            if ( location.getLocType() == BDLocation.TypeGpsLocation ) {
                currentPosition.append( "GPS" ).append( "\n" ) ;
            }
            if ( location.getLocType() == BDLocation.TypeNetWorkLocation ) {
                currentPosition.append( "网络" ).append( "\n" ) ;
            }
            currentPosition.append( "国家：" ).append( location.getCountry() ) .append( "\n" ) ;
            currentPosition.append( "省份：" ).append( location.getProvince() ).append( "\n" ) ;
            currentPosition.append( "城市：" ).append( location.getCity() ).append( "\n" ) ;
            currentPosition.append( "街道：" ).append( location.getStreet() ).append( location.getStreetNumber() ).append( "\n" ) ;
            positionText.setText( currentPosition ) ;

        }
    }

}
