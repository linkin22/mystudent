package com.ramogi.xboxme.model;

import com.google.android.gms.maps.model.LatLng;
import com.ramogi.xboxme.clustering.ClusterItem;


public class MyItem implements ClusterItem {
    private final LatLng mPosition;

    public MyItem(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }
}
