package com.example.administrator.amaptest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.LocationListener;
import android.os.PersistableBundle;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MapView mapView ;
    private AMap aMap ; //AMap类是对整个地图进行各种操作，控制器
    private MyLocationStyle myLocationStyle ;
    private AMapLocationClient aMapLocationClient ;
    private AMapLocationListener aMapLocationListener = new MyAMapLocationListener() ;
    private AMapLocationClientOption aMapLocationClientOption ;
    private boolean isFirstLocate = true ;//用于初次定位，定位完成后改成false
    /*
    保证地图与活动的生命周期一致
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapView = ( MapView ) findViewById( R.id.map ) ;
        mapView.onCreate( savedInstanceState ) ;//活动开始时，创建地图
        mapView.onSaveInstanceState( savedInstanceState );
        initPosition () ;
    }

    public void initPosition ( ) {
        if ( aMap == null ) {
            aMap = mapView.getMap() ;
            //aMap.setLocationSource(  );
            aMapLocationClient = new AMapLocationClient( getApplicationContext() ) ;
            aMapLocationClient.setLocationListener( aMapLocationListener ) ;
            aMapLocationClient.startLocation();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();//销毁活动时，销毁地图
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState ) ;
        mapView.onSaveInstanceState( outState );
    }

    private void drawMarkers( AMapLocation aMapLocation ) {

        MarkerOptions markerOptions = new MarkerOptions();
        // 设置Marker的坐标，为我们点击地图的经纬度坐标
        markerOptions.position(new LatLng( aMapLocation.getLatitude() , aMapLocation.getAltitude() ));
        // 设置Marker点击之后显示的标题
        markerOptions.title("八维");
        // 设置Marker的图标样式
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        // 设置Marker是否可以被拖拽，
        markerOptions.draggable(false);
        // 设置Marker的可见性
        markerOptions.visible(true);
        //将Marker添加到地图上去
        Marker marker = aMap.addMarker(markerOptions);
        marker.showInfoWindow();
    }

    private void navigateTo ( AMapLocation aMapLocation ) {
        if ( isFirstLocate ) {
            LatLng ll = new LatLng( aMapLocation.getLatitude() , aMapLocation.getAltitude() ) ;
            //CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition( new CameraPosition( new LatLng( aMapLocation.getLatitude() , aMapLocation.getAltitude() ) , 18 , 0 , 30 ) ) ;
            //aMap.moveCamera( cameraUpdate ) ;
            aMap.moveCamera( CameraUpdateFactory.changeLatLng( ll ) ) ;
            aMap.moveCamera( CameraUpdateFactory.zoomTo( 18f ) ) ;
            drawMarkers( aMapLocation ) ;
            isFirstLocate = false ;
            Toast.makeText(MainActivity.this , "执行了navigateTo方法的" , Toast.LENGTH_SHORT ).show();
        }
    }

    class MyAMapLocationListener implements AMapLocationListener {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            //定位完成后调用这个方法
            navigateTo( aMapLocation ) ;
            Toast.makeText( MainActivity.this , "草草草草草草草" , Toast.LENGTH_SHORT).show();
        }
    }

}
