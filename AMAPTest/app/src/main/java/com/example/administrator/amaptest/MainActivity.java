package com.example.administrator.amaptest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.icu.text.SimpleDateFormat;
import android.location.LocationListener;
import android.os.PersistableBundle;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {

    private MapView mapView ;
    private AMap aMap ;

    private AMapLocationClient mLocationClient ;
    private AMapLocationClientOption mLocationClientOpion ;
    protected MyLocationListener mListener ;
    private Boolean isFirst = true ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_main ) ;
        Button mStartNavi =  ( Button ) findViewById( R.id.start_navi ) ;
        mapView = ( MapView ) findViewById( R.id.map ) ;
        mapView.onCreate( savedInstanceState ) ;//保证地图的周期与活动的周期相同
        mapView.onSaveInstanceState( savedInstanceState ) ;//必须有，不然地图不会显示出来
        initMap() ;//初始化的方法
    }

    public void initMap() {
        request();
        if ( aMap == null ) {
            aMap = mapView.getMap() ;
            aMap.setMyLocationEnabled( true ) ;//设置定位
            UiSettings uiSettings = aMap.getUiSettings() ;//UiSettings 是对界面上的一些控件进行管理
            uiSettings.setMyLocationButtonEnabled( true ) ;//显示定位的按钮
            mListener = new MyLocationListener() ;//初始化mListener,注意不能直接用AMapLocationListener
            mLocationClient = new AMapLocationClient( getApplicationContext() ) ;//初始化设置定位的发起端
            mLocationClientOpion = new AMapLocationClientOption() ;//这个类使用来个定位设置参数的
            mLocationClient.setLocationListener( mListener ) ;//给定位发起端设置监听器
            mLocationClientOpion.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy) ;//设置定位的模式为 高精准度(同时也是高耗电)
            mLocationClientOpion.setOnceLocation( true ) ;//单次定位
            mLocationClientOpion.setOnceLocationLatest( true ) ;
            //mLocationClientOpion.setInterval( 2000 ) ;//连续定位 设置定位的时间间隔
            mLocationClient.setLocationOption( mLocationClientOpion ) ;
            mLocationClient.startLocation();
        }
    }

    public void request () {
        List<String> permissionList = new ArrayList<>() ;
        if ( ContextCompat.checkSelfPermission( MainActivity.this , Manifest.permission.READ_PHONE_STATE ) != PackageManager.PERMISSION_GRANTED ) {
            permissionList.add( Manifest.permission.READ_PHONE_STATE ) ;
        }
        if ( ContextCompat.checkSelfPermission( MainActivity.this , Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            permissionList.add( Manifest.permission.ACCESS_FINE_LOCATION ) ;
        }
        if ( ContextCompat.checkSelfPermission( MainActivity.this , Manifest.permission.WRITE_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED ) {
            permissionList.add( Manifest.permission.WRITE_EXTERNAL_STORAGE ) ;
        }
        if ( ContextCompat.checkSelfPermission( MainActivity.this , Manifest.permission.ACCESS_WIFI_STATE ) != PackageManager.PERMISSION_GRANTED ) {
            permissionList.add( Manifest.permission.ACCESS_WIFI_STATE );
        }
        if ( !permissionList.isEmpty() ) {
            String [] permissions = permissionList.toArray( new String[permissionList.size() ] ) ;
            ActivityCompat.requestPermissions( MainActivity.this , permissions , 1 ) ;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch ( requestCode ) {
            case 1 :
                if ( grantResults.length > 0 ) {
                    for ( int result : grantResults ) {
                        if ( result != PackageManager.PERMISSION_GRANTED ) {
                            Toast.makeText(MainActivity.this , "必须要有权限" ,Toast.LENGTH_SHORT ).show();
                            finish();
                            return;
                        }
                    }
                }
        }
    }

    private void drawMarkers( AMapLocation aMapLocation ) {

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng( aMapLocation.getLongitude() , aMapLocation.getLatitude() ));// 设置Marker的坐标，为我们点击地图的经纬度坐标
        markerOptions.title("你在这");// 设置Marker点击之后显示的标题
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));// 设置Marker的图标样式
        markerOptions.draggable(true);// 设置Marker是否可以被拖拽
        markerOptions.visible(true);// 设置Marker的可见性
        Marker marker = aMap.addMarker(markerOptions);//将Marker添加到地图上去
        marker.showInfoWindow();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    class MyLocationListener implements AMapLocationListener {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if ( aMapLocation != null ) {
                if ( aMapLocation.getErrorCode() == 0 ) {
                    if ( isFirst ) {
                        aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
                        Toast.makeText(MainActivity.this , "AISJcijaiofihawkebjdfglzjxfngioherghiahdg" , Toast.LENGTH_SHORT).show();
                        isFirst = false ;
                        mLocationClient.stopLocation();
                    }
                }
                else{
                    Log.d( "错误码：" + aMapLocation.getErrorCode() , aMapLocation.getErrorInfo() ) ;
                }
            }
        }
    }
}

