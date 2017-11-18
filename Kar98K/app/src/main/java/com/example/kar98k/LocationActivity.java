package com.example.kar98k;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLng;

public class LocationActivity extends AppCompatActivity implements View.OnClickListener {

    MapView mapView ;
    AMap aMap ;
    AMapLocationClient aMapLocationClient ;

    Button sendPosition ;
    Boolean isFirst = true ;
    Location location ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        mapView = ( MapView ) findViewById( R.id.map_location );
        sendPosition = ( Button ) findViewById( R.id.send_position ) ;
        sendPosition.setOnClickListener( this );
        mapView.onCreate(savedInstanceState);
        mapView.onSaveInstanceState(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        initMap();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    /**
     * 初始化地图
     */
    public void initMap () {
        if ( aMap == null ) {
            aMap = mapView.getMap() ;//设置地图的总控制器
            aMapLocationClient = new AMapLocationClient( getApplicationContext() ) ;
            aMapLocationClient.setLocationListener(new AMapLocationListener() {
                @Override
                public void onLocationChanged(AMapLocation aMapLocation) {
                    if ( aMapLocation.getErrorCode() == 0 ) {
                        if ( isFirst ) {
                            location  = new Location( new LatLng(aMapLocation.getLatitude() , aMapLocation.getLongitude()) ) ;
                            aMap.moveCamera( CameraUpdateFactory.newLatLngZoom( location.ll , 17 ) ) ;
                            SharedPreferences sharedPreferences = getSharedPreferences( "操" , Context.MODE_PRIVATE) ;
                            sharedPreferences.edit().putString("Latitude" , Double.toString( aMapLocation.getLatitude() ) ).commit();
                            sharedPreferences.edit().putString("Longitude" , Double.toString( aMapLocation.getLongitude())).commit();
                            Toast.makeText( LocationActivity.this , "定位成功" , Toast.LENGTH_SHORT).show() ;
                            isFirst = false ;
                        }
                    }
                }
            }) ; //设置定位监听器
            aMap.setMyLocationEnabled( true ) ;
            UiSettings uiSettings = aMap.getUiSettings() ; //对界面上的一些控件进行管理
            uiSettings.setMyLocationButtonEnabled( true ) ;//显示定位按钮
            aMapLocationClient.startLocation();
        }
    }

    @Override
    public void onClick(View v) {
        switch ( v.getId() ) {
            case R.id.send_position :
                Toast.makeText( LocationActivity.this , "发送成功" , Toast.LENGTH_SHORT).show();
                //onBackPressed();
                LocationActivity locationActivity = new LocationActivity() ;
                locationActivity.finish();
        }
    }
}
