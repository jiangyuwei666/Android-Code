package com.example.kar98k;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MapView mapView ;//地图显示
    private AMap aMap ;//地图控制类
    public AMapLocationClient aMapLocationClient ;
    private AMapLocationClientOption aMapLocationClientOption ; //设置定位的一些参数

    private Button startNavi ;
    private Button sendPosition ;

    private Boolean isFirst = true ;

    public LatLng ll ;
    public LatLng myLatLng ;
    public LatLng aimLatLng ;

    /*
    * 使地图的生命周期与活动的生命周期一致
    * */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startNavi = ( Button ) findViewById( R.id.start_navi ) ;
        sendPosition = ( Button ) findViewById( R.id.send_position ) ;
        mapView = ( MapView ) findViewById( R.id.map ) ;
        startNavi.setOnClickListener( this ) ;
        sendPosition.setOnClickListener( this ) ;
        mapView.onCreate( savedInstanceState ) ;
        mapView.onSaveInstanceState( savedInstanceState ) ;//必须得有，不然地图显示不出来
        initMap();
    }

    //初始化地图
    public void initMap () {
        request();
        if ( aMap == null ) {
            aMap = mapView.getMap() ;//设置地图的总控制器
            aMapLocationClient = new AMapLocationClient( getApplicationContext() ) ;
            aMapLocationClient.setLocationListener(new AMapLocationListener() {
                @Override
                public void onLocationChanged(AMapLocation aMapLocation) {
                    if ( aMapLocation.getErrorCode() == 0 ) {
                        if ( isFirst ) {
                            ll = new LatLng( aMapLocation.getLatitude() , aMapLocation.getLongitude() ) ;
                            aMap.moveCamera( CameraUpdateFactory.newLatLngZoom( ll , 17 ) ) ;
                            Location location = new Location( ll ) ;
                            drawMarkers( new LatLng( aMapLocation.getLatitude() + 0.001 , aMapLocation.getLongitude() + 0.001 ) ) ;
                            Toast.makeText( MainActivity.this , "cccc" , Toast.LENGTH_SHORT) .show() ;
                        }
                    }
                }
            }) ; //设置定位监听器
            aMap.setMyLocationEnabled( true ) ;
            UiSettings uiSettings = aMap.getUiSettings() ; //对界面上的一些控件进行管理
            uiSettings.setMyLocationButtonEnabled( false ) ;//显示定位按钮
            aMapLocationClient.startLocation();
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

    //用于定位时返回我的地址
    public LatLng getLocation( Location location ) {
        return location.latLng ;
    }

    @Override
    public void onClick(View v) {
        switch ( v.getId() ) {
            case R.id.start_navi :
                //添加逻辑
                startNaviFragment( new NaviFragment() ) ;
                Toast.makeText( MainActivity.this , "开始导航" , Toast.LENGTH_SHORT ).show();
                break;
            case R.id.send_position :
                getLocation( new Location( ll ) ) ;
                Toast.makeText( MainActivity.this , "发送成功" , Toast.LENGTH_SHORT ).show();
                //添加一个back的操作
                break;
        }
    }

    //添加标记
    public void drawMarkers ( LatLng latlng ) {
        MarkerOptions markerOptions = new MarkerOptions() ;
        markerOptions.position( latlng ) ;
        markerOptions.title( "他在这里" ) ;//标记上面显示的消息
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker( BitmapDescriptorFactory.HUE_AZURE ) ) ;//设置标记的样式
        markerOptions.visible( true ) ;//标记的可见性
        Marker marker = aMap.addMarker( markerOptions ) ;
        marker.showInfoWindow();
    }

    //添加导航fragment
    public void startNaviFragment( Fragment fragment ) {
        FragmentManager fragmentManager = getSupportFragmentManager() ;
        FragmentTransaction transaction = fragmentManager.beginTransaction() ;
        transaction.replace( R.id.navi_fragment , fragment ) ;
        transaction.addToBackStack( null ) ;//回到地图界面
        transaction.commit() ;
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

}
