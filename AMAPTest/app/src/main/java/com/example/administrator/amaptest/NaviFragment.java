package com.example.administrator.amaptest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewListener;

/**
 * Created by Administrator on 2017/10/18.
 */

public class NaviFragment extends Fragment implements AMapNaviViewListener {

    private AMapNaviView aMapNaviView ;//导航布局

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.navi_fragment , container , false ) ;//添加碎片
        aMapNaviView = ( AMapNaviView ) view.findViewById( R.id.navi_view ) ;
        aMapNaviView.onCreate( savedInstanceState ) ;
        aMapNaviView.setAMapNaviViewListener( this ) ;
        return view ;
    }

    //同样的使其的生命周期和他一致
    @Override
    public void onPause() {
        super.onPause();
        aMapNaviView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        aMapNaviView.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        aMapNaviView.onDestroy();
    }

    public NaviFragment() {
        super();
    }

    @Override
    public void onLockMap(boolean b) {

    }

    @Override
    public void onNaviSetting() {

    }

    @Override
    public void onNaviCancel() {
        this.getActivity().finish();
    }

    @Override
    public boolean onNaviBackClick() {
        return false;
    }

    @Override
    public void onNaviMapMode(int i) {

    }

    @Override
    public void onNaviTurnClick() {

    }

    @Override
    public void onNextRoadClick() {

    }

    @Override
    public void onScanViewButtonClick() {

    }

    @Override
    public void onNaviViewLoaded() {

    }
}
