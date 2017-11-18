package com.example.kar98k;

import com.amap.api.maps.model.LatLng;

/**
 * Created by Administrator on 2017/11/9.
 */

public class Location  {

    public LatLng ll ;
    public LatLng myLl ;
    public LatLng hisLl ;

    /**
     * 定位界面构造器
     * @param ll 定位位置信息
     */
    public Location ( LatLng ll ) {
        this.ll = ll;
    }

    /**
     * 导航界面的构造器
     * @param myLl 定位位置信息
     * @param hisLl 传入位置信息
     */
    public Location ( LatLng myLl , LatLng hisLl ) {
        this.myLl = myLl ;
        this.hisLl = hisLl ;
    }

    public LatLng getLocation () {
        return ll ;
    }



}
