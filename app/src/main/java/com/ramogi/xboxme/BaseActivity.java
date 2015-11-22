package com.ramogi.xboxme;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public abstract class BaseActivity extends FragmentActivity {
    private GoogleMap mMap;

    private static final String KEY_PEOPLE = "PLUSPEOPLE";
    private static final String KEY_LOCATION = "PLUSLOCATION";
    private static final String KEY_URL = "PLUSURL";

    public ArrayList<LatLng> plusLocations = new ArrayList<LatLng>();
    public ArrayList<String> plusUrl = new ArrayList<String>();
    public ArrayList<String> plusPeople = new ArrayList<String>();
    public boolean getValues = false;

/*

    public Bundle getSavedInstanceState() {
        return savedInstanceState;
    }

    public void setSavedInstanceState(Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;
    }

    private Bundle savedInstanceState;
    */

    protected int getLayoutId() {
        return R.layout.map;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        setUpMapIfNeeded();
        //setSavedInstanceState(savedInstanceState);
        //updateValuesFromBundle(savedInstanceState);
    }


    /**
     * Updates fields based on data stored in the bundle.
     *
     * @param //savedInstanceState The activity state saved in the Bundle.
     */
/*
    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {

            if((savedInstanceState.keySet().contains(KEY_PEOPLE))
                    &&(savedInstanceState.keySet().contains(KEY_LOCATION))
                    &&(savedInstanceState.keySet().contains(KEY_URL))){

                plusPeople = savedInstanceState.getStringArrayList(KEY_PEOPLE);
                plusLocations = savedInstanceState.getParcelableArrayList(KEY_LOCATION);
                plusUrl = savedInstanceState.getStringArrayList(KEY_URL);

                getValues = true;
            }
        }
    }
    */


    /**
     * Stores activity data in the Bundle.
     */
    /*
    @Override
    protected void onSaveInstanceState(Bundle outState) {

        if((plusLocations != null)&&(plusUrl != null)&&(plusPeople != null)) {
            outState.putStringArrayList(KEY_PEOPLE, plusPeople);
            outState.putStringArrayList(KEY_URL, plusUrl);
            outState.putParcelableArrayList(KEY_LOCATION, plusLocations);

            super.onSaveInstanceState(outState);
        }
    }
    */

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        if (mMap != null) {
            return;
        }
        mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        if (mMap != null) {
            startDemo();
        }
    }

    /**
     * Run the demo-specific code.
     */
    protected abstract void startDemo();

    protected GoogleMap getMap() {
        setUpMapIfNeeded();
        return mMap;
    }
}
