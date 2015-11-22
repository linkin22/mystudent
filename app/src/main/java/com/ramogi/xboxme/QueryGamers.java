package com.ramogi.xboxme;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.SimpleAdapter;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.ramogi.xbox.backend.gamersLocationApi.GamersLocationApi;
import com.ramogi.xbox.backend.gamersLocationApi.model.CollectionResponseGamersLocation;
import com.ramogi.xbox.backend.gamersLocationApi.model.GamersLocation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ROchola on 11/22/2015.
 */
public class QueryGamers extends AsyncTask<Void, Void, CollectionResponseGamersLocation> {

    private GoogleAccountCredential credential;
    private QueryPlusCallBack queryPlusCallBack ;
    private int limit;
    private static GamersLocationApi myApiService = null;

    QueryGamers( int limit, QueryPlusCallBack queryPlusCallBack, GoogleAccountCredential credential) {

        this.credential = credential;
        this.limit = limit;
        this.queryPlusCallBack = queryPlusCallBack;
    }

    protected CollectionResponseGamersLocation doInBackground(Void... unused) {
        CollectionResponseGamersLocation gamersLocations = null;

        try {
            if (myApiService == null) { // Only do this once
                GamersLocationApi.Builder builder = new GamersLocationApi.Builder(
                        AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), credential)
                        // options for running against local devappserver
                        // - 10.0.2.2 is localhost's IP address in Android emulator
                        // - turn off compression when running against local devappserver
                        .setRootUrl("https://" + Constants.PROJECT_ID + ".appspot.com/_ah/api/")
                        .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                            @Override
                            public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                                abstractGoogleClientRequest.setDisableGZipContent(true);
                            }
                        });
                // end options for devappserver
                myApiService = builder.build();
            }
                gamersLocations = myApiService.list().execute();

            }catch(IOException e){
                //Log.v("builder/do background", e.toString());
            }

            return gamersLocations;
        }
    protected void onPostExecute(CollectionResponseGamersLocation gamersLocations) {

        // Do something with the result.
        ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        List<GamersLocation> _list = gamersLocations.getItems();
        for (GamersLocation gamersLocation : _list) {
            HashMap<String, Object> item = new HashMap<String, Object>();
            item.put("email", gamersLocation.getEmail());
            item.put("displayname", gamersLocation.getDisplayname());
            item.put("gamertag", gamersLocation.getGamertag());
            item.put("latitude", gamersLocation.getLatx());
            item.put("longitude",gamersLocation.getLongy());
            item.put("time", gamersLocation.getTxntime());
            list.add(item);
        }
        queryPlusCallBack.querycomplete(list);
    }
}
