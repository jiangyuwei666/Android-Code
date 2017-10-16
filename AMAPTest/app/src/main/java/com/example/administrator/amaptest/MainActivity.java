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
import android.util.Log;
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
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.TileProjection;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LocationSource {

    private MapView mapView ;
    private AMap aMap ; //AMap类是对整个地图进行各种操作，控制器
    private MyLocationStyle myLocationStyle ;
    private AMapLocationClient aMapLocationClient ;
    private AMapLocationListener aMapLocationListener = new MyAMapLocationListener() ;
    private AMapLocationClientOption aMapLocationClientOption = null ;
    protected LocationSource.OnLocationChangedListener mListener = null ;
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
            aMap.setLocationSource( this );//将MainActivity设置为定位监听
            aMapLocationClient = new AMapLocationClient( getApplicationContext() ) ;
            //aMapLocationClientOption = getOption( aMapLocationClientOption ) ; //官方文档写的是:在aMapLocationClient定位时需要这些参数,会不会是因为这个导致定位到非洲。。。试了之后，崩溃了
            aMapLocationClient.setLocationListener( aMapLocationListener ) ;//设置定位回掉监听
            aMapLocationClient.setLocationOption( aMapLocationClientOption ) ;
            aMapLocationClient.startLocation();
            UiSettings uiSettings = aMap.getUiSettings() ;//一个对地图上的控件管理的类
            uiSettings.setCompassEnabled( true ) ;//指南针
            uiSettings.setMyLocationButtonEnabled( true ) ;//定位按钮
            aMap.setMyLocationEnabled( true ) ;//点击重新定位
            uiSettings.setZoomControlsEnabled( true ) ;//地图缩放的+-号

        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener ;
    }

    @Override
    public void deactivate() {
        mListener = null ;
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
        markerOptions.position(new LatLng( aMapLocation.getLongitude() , aMapLocation.getLatitude() ));
        // 设置Marker点击之后显示的标题
        markerOptions.title("你在这");
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

    private AMapLocationClientOption getOption ( AMapLocationClientOption option ) {
        option.setInterval( 2000 ) ;//定位间隔
        option.setLocationMode( AMapLocationClientOption.AMapLocationMode.Hight_Accuracy ) ;//定位模式，高精度
        return option ;
    }

    private void navigateTo ( AMapLocation aMapLocation ) {
        if ( isFirstLocate ) {
            LatLng ll = new LatLng( aMapLocation.getLatitude() , aMapLocation.getLongitude() ) ;
            aMap.moveCamera( CameraUpdateFactory.changeLatLng( ll ) ) ;
            aMap.moveCamera( CameraUpdateFactory.zoomTo( 6f ) ) ;
            drawMarkers( aMapLocation ) ;
            isFirstLocate = false ;
            Log.d( "FUCKKKKKKKKKKKK" , "FUUUUUUUUUUUUUUUUUCCCCCCCCCCCCCCCKKKKKKKKKKKKKK" ) ;
        }
    }

    class MyAMapLocationListener implements AMapLocationListener {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            //定位完成后调用这个方法
            //这里加一个判定，isStarted方法用于检查本地定位服务是否已经启动，如果启动了就OjbK,但是跑了程序后并没有定位，所以本地定位服务并没有启动(1.权限问题2.服务问题)
            //在AndroidManifest中添加了<service>标签后，执行了这个方法，说明启动了定位
            //但是仍然定位在非洲，是不是因为 aMapLocation并没有传进来
            if ( aMapLocationClient.isStarted() ) {
                navigateTo( aMapLocation ) ;
                Log.d( "cao" + aMapLocation.getErrorCode() , aMapLocation.getErrorInfo()  ) ;
                //错误码12：缺少权限   定位权限被禁用,请授予应用定位权限#1201
                //？？？？？
            }

        }
    }

}
