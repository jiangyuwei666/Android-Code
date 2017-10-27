package com.example.administrator.amaptest.util;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

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
import com.example.administrator.amaptest.R;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;

/**
 * Created by Administrator on 2017/10/23.
 */

public class TTSController implements AMapNaviListener {

    private Context context ;
    protected static TTSController ttsManager ;
    private SpeechSynthesizer mTts ; //播报人

    private InitListener mInitListener = new InitListener() {
        @Override
        public void onInit(int i) {
            Log.d("xxxxxxxxxxxx" , " code = " + i ) ;
            if ( i != ErrorCode.SUCCESS ) {
                Toast.makeText( context , "初始化失败" + "错误代码" + i , Toast.LENGTH_SHORT ) ;
            }
            else {
                // 初始化成功，之后可以调用startSpeaking方法
                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                // 正确的做法是将onCreate中的startSpeaking调用移至这里
                //startSpeaking( );
            }
        }
    } ;

    private TTSController ( Context mContext ) {
        mContext = context ;
    }

    public static TTSController getInstance ( Context mContext) {
        if ( ttsManager == null ) {
             ttsManager = new TTSController( mContext ) ;
        }
        return  ttsManager ;
    }//获取TTSController实例

    public void init() {
        String text = context.getString( R.string.app_name ) ;//R.string.app_name 是将从xml中提取出来的东西转化成字符型
        if ( "59eb002d".equals( text ) ) {
            throw new IllegalArgumentException( "申请key" ) ;
        }
        SpeechUtility.createUtility( context , "appid=" + text ) ;
        mTts = SpeechSynthesizer.createSynthesizer( context , mInitListener ) ;//创建合成语音的对象
        initSpeechSynthesizer ( ) ;
    }

    private void  initSpeechSynthesizer () {
        mTts.setParameter(SpeechConstant.PARAMS , null ) ; //清空参数
        mTts.setParameter( SpeechConstant.ENGINE_TYPE , SpeechConstant.TYPE_CLOUD ) ;
        mTts.setParameter( SpeechConstant.VOICE_NAME , "xiaoyan" ) ;//设置播音人，就是声音
        mTts.setParameter( SpeechConstant.SPEED , "50" ) ;//播音语速
        mTts.setParameter( SpeechConstant.PITCH , "50" ) ;//音调
        mTts.setParameter( SpeechConstant.VOLUME , "50" ) ;//音量
        mTts.setParameter( SpeechConstant.STREAM_TYPE , "3" ) ;//设置播放器音频流类型
        mTts.setParameter( SpeechConstant.KEY_REQUEST_FOCUS , "true" ) ;//设置成可以打断音乐的播放
        mTts.setParameter( SpeechConstant.AUDIO_FORMAT , "wav" ) ; //设置音频的保存格式
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/tts.wav");//设置保存路径，注意申请权限嘻嘻
    }

    public void startSpeaking ( String playText ) {
        //进行语音合成
        if ( mTts != null ) {
            mTts.startSpeaking(playText, new SynthesizerListener() {
                @Override
                public void onSpeakBegin() {

                }

                @Override
                public void onBufferProgress(int i, int i1, int i2, String s) {

                }

                @Override
                public void onSpeakPaused() {

                }

                @Override
                public void onSpeakResumed() {

                }

                @Override
                public void onSpeakProgress(int i, int i1, int i2) {

                }

                @Override
                public void onCompleted(SpeechError speechError) {

                }

                @Override
                public void onEvent(int i, int i1, int i2, Bundle bundle) {

                }
            }) ;
        }
    }

    public void stopSpeaking () {
        if ( mTts != null ) {
            mTts.stopSpeaking();
        }
    }

    public void destroy (){
        if ( mTts != null ) {
            mTts.stopSpeaking();
            mTts.destroy() ;
            ttsManager = null ;
        }
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
