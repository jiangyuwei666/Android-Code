package com.example.administrator.amaptest;

import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.speech.tts.SynthesisCallback;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.amap.api.location.AMapLocationListener;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.example.administrator.amaptest.util.TTSController;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.speech.SynthesizerListener;

/**
 * Created by Administrator on 2017/10/21.
 */

public class RouteNaviActivity extends FragmentActivity implements AMapNaviListener {

    AMapNavi mAMapNavi ;
    TTSController mTsManager ;
    Fragment mNaviFragment ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_basic_navi ) ;
        mTsManager = TTSController.getInstance( getApplicationContext() ) ;
        mTsManager.init();

        mAMapNavi = AMapNavi.getInstance( getApplicationContext() ) ;
        mAMapNavi.addAMapNaviListener( this ) ;
        mAMapNavi.addAMapNaviListener( mTsManager ) ;//添加导航监听
        mAMapNavi.setEmulatorNaviSpeed( 60 ) ;
        boolean gps = getIntent().getBooleanExtra( "gps" , false ) ;
        if ( gps ) {
            mAMapNavi.startNavi( AMapNavi.GPSNaviMode ) ;
        }
        else {
            mAMapNavi.startNavi( AMapNavi.EmulatorNaviMode ) ;
        }
        setUpMapIfNeeded() ;
    }

    public void setUpMapIfNeeded () {
        if ( mNaviFragment == null ) {
            mNaviFragment = getSupportFragmentManager().findFragmentById( R.id.navi_fragment ) ;
        }
    }

    @Override
    protected void onResume() {
        setUpMapIfNeeded();
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mTsManager.stopSpeaking();
        mAMapNavi.stopNavi();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAMapNavi.stopNavi();
        mAMapNavi.removeAMapNaviListener( mTsManager ) ;
        mTsManager.destroy();
    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onGetNavigationText(int i, String s) {

    }

    @Override
    public void onNaviInfoUpdated(AMapNaviInfo aMapNaviInfo) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {

    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {

    }

    public RouteNaviActivity() {
        super();
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

    }

    @Override
    public void notifyParallelRoad(int i) {

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
}
