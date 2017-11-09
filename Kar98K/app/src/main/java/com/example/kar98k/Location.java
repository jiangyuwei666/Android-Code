package com.example.kar98k;

import com.amap.api.maps.model.LatLng;

/**
 * Created by Administrator on 2017/11/9.
 */

public class Location  {

    public LatLng latLng ;

    public Location ( LatLng ll ) {
        latLng = ll ;
    }

    public LatLng getLocation () {
        return latLng ;
    }



}
