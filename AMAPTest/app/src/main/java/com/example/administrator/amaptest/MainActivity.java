package com.example.administrator.amaptest;

import android.Manifest;
import android.content.Intent;
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
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.navi.view.RouteOverLay;
import com.autonavi.tbt.TrafficFacilityInfo;

import org.w3c.dom.ls.LSInput;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AMap.OnMapLoadedListener , AMapNaviListener , View.OnClickListener {

    private MapView mapView ;
    private AMap mAMap ;
    private AMapNavi mAMapNavi ;

    private AMapLocationClient mLocationClient ;
    private AMapLocationClientOption mLocationClientOpion ;
    protected MyLocationListener mListener ;
    private Boolean isFirst = true ;

    private NaviLatLng endLalng ;
    private NaviLatLng startLng ;

    private List< NaviLatLng > startList = new ArrayList< NaviLatLng >( ) ;
    private List< NaviLatLng > wayList = new ArrayList< NaviLatLng >( ) ;//途经点坐标集合
    private List< NaviLatLng > endList = new ArrayList< NaviLatLng >( ) ;

    int strategyFlag = 0 ;
    private RouteOverLay mRouteOverlay ;
    private Button mStartNavi ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_main ) ;
        mStartNavi =  ( Button ) findViewById( R.id.start_navi ) ;
        mapView = ( MapView ) findViewById( R.id.map ) ;
        mapView.onCreate( savedInstanceState ) ;//保证地图的周期与活动的周期相同
        mapView.onSaveInstanceState( savedInstanceState ) ;//必须有，不然地图不会显示出来
        initMap() ;//初始化的方法
        setUpMapIfNeeded();//初始化
    }

    public void initMap() {
        request();
        /**
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
         */
    }

    //驾车路线规划
    private void calculateDriveRoute () {
        try {
            strategyFlag = mAMapNavi.strategyConvert( true , false , false , true , false ) ;
        }
        catch ( Exception e ) {
            e.printStackTrace() ;
        }
        mAMapNavi.calculateDriveRoute( startList , endList , wayList , strategyFlag ) ;
    }

    //添加地图哦
    private void setUpMapIfNeeded () {
        if ( mAMap == null ) {
            mAMap = ( (SupportMapFragment) getSupportFragmentManager().findFragmentById( R.id.map ) ).getMap() ;
            UiSettings uiSettings = mAMap.getUiSettings() ;
            if ( uiSettings != null ) {
                uiSettings.setRotateGesturesEnabled( false ) ;
            }
            mAMap.setOnMapLoadedListener( this ) ;
        }
    }

    //导航初始化
    private void initNavi () {
        startList.add( startLng ) ;
        endList.add( endLalng ) ;
        mAMapNavi = AMapNavi.getInstance( getApplicationContext() ) ;
        mAMapNavi.addAMapNaviListener( this ) ;
    }

    private void cleanRouteOverlay () {
        if ( mRouteOverlay != null ) {
            mRouteOverlay.removeFromMap();
            mRouteOverlay.destroy();
        }
    }

    private void drawRoutes ( AMapNaviPath path ) {
        mAMap.moveCamera( CameraUpdateFactory.changeTilt( 0 ) );
        mRouteOverlay = new RouteOverLay( mAMap , path , this ) ;
        mRouteOverlay.addToMap();
        mRouteOverlay.zoomToSpan();
    }

    @Override
    public void onClick(View v) {
        switch ( v.getId() ) {
            case R.id.start_navi :
                Intent gpsIntent = new Intent ( getApplicationContext() , RouteNaviActivity.class ) ;
                gpsIntent.putExtra( "gps" , false ) ; //添加参数，true为真实导航，false为模拟导航
                startActivity( gpsIntent ) ;
        }
    }

    //权限的动态申请
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
        Marker marker = mAMap.addMarker(markerOptions);//将Marker添加到地图上去
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
                        startLng = new NaviLatLng( aMapLocation.getLatitude() , aMapLocation.getLongitude() ) ;
                        endLalng = new NaviLatLng( aMapLocation.getLatitude() + 1.0 , aMapLocation.getLongitude() ) ;
                        mAMap.moveCamera(CameraUpdateFactory.zoomTo(17));
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


    @Override
    public void onMapLoaded() {
        calculateDriveRoute();
    }

    @Override
    public void onInitNaviFailure() {

    }

    @Override
    public void onInitNaviSuccess() {

    }

    @Override
    public void onStartNavi(int i) {

    }

    @Override
    public void onTrafficStatusUpdate() {

    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

    }

    @Override
    public void onGetNavigationText(int i, String s) {

    }

    @Override
    public void onGetNavigationText(String s) {

    }

    @Override
    public void onEndEmulatorNavi() {

    }

    @Override
    public void onArriveDestination() {

    }

    @Override
    public void onCalculateRouteFailure(int i) {

    }

    @Override
    public void onReCalculateRouteForYaw() {

    }

    @Override
    public void onReCalculateRouteForTrafficJam() {

    }

    @Override
    public void onArrivedWayPoint(int i) {

    }

    @Override
    public void onGpsOpenStatus(boolean b) {

    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {

    }

    @Override
    public void onNaviInfoUpdated(AMapNaviInfo aMapNaviInfo) {

    }

    @Override
    public void updateCameraInfo(AMapNaviCameraInfo[] aMapNaviCameraInfos) {

    }

    @Override
    public void onServiceAreaUpdate(AMapServiceAreaInfo[] aMapServiceAreaInfos) {

    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {

    }

    @Override
    public void hideCross() {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {

    }

    @Override
    public void hideLaneInfo() {

    }

    @Override
    public void onCalculateRouteSuccess(int[] ints) {
        cleanRouteOverlay();
        AMapNaviPath path = mAMapNavi.getNaviPath() ;
        if ( path != null ) {
            drawRoutes( path ) ;
        }
        mStartNavi.setVisibility( View.VISIBLE ) ;
    }

    @Override
    public void notifyParallelRoad(int i) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {

    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {

    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {

    }

    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {

    }

    @Override
    public void onPlayRing(int i) {

    }
}

