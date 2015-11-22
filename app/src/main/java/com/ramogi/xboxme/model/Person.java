package com.ramogi.xboxme.model;

import android.graphics.drawable.Drawable;

import com.google.android.gms.maps.model.LatLng;
import com.google.api.client.util.DateTime;
import com.ramogi.xboxme.clustering.ClusterItem;


public class Person implements ClusterItem {
    public final String name;
    public final Drawable profilePhoto;
    private final LatLng mPosition;
    public final DateTime updateTime;

    public Person(LatLng position, String name, Drawable d, DateTime updateTime ) {

        this.name = name;
        profilePhoto = d;
        mPosition = position;
        this.updateTime = updateTime;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

}
