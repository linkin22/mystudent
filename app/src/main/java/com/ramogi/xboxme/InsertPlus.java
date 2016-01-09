package com.ramogi.xboxme;


import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.google.api.client.util.DateTime;
import com.ramogi.xbox.backend.gamersLocationApi.model.GamersLocation;
import com.ramogi.xbox.backend.gamersLocationApi.GamersLocationApi;

/**
 * Created by ROchola on 11/19/2015.
 */


import java.io.IOException;



/**
 * Created by ROchola on 2/15/2015.
 */
public class InsertPlus extends AsyncTask<GamersLocation, Void, String> {

    private static GamersLocationApi myApiService = null;
    private Context context;
    private GoogleAccountCredential credential;
    public GamersLocation gamersLocation;

    public InsertPlus(GamersLocation gamersLocation, Context context, GoogleAccountCredential credential) {
        setGamersLocation(gamersLocation);
        this.context = context;
        this.credential = credential;
    }

    @Override
    protected String doInBackground(GamersLocation... locationPlus) {

        // Only do this once
        // options for running against local devappserver
        // - 10.0.2.2 is localhost's IP address in Android emulator
        // - turn off compression when running against local devappserver

        if (myApiService == null) {
            GamersLocationApi.Builder builder = new GamersLocationApi.Builder(
                    AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), credential)
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



        try {
            //myApiService.insert(getLocationPlus()).execute();
            //myApiService.insert(locationPlus[0]).execute();
            getGamersLocation().setTxntime(new com.google.api.client.util.DateTime(new java.util.Date()));
            myApiService.insert(getGamersLocation()).execute();
            //myApiService.update(locationPlus[0].getEmail(),locationPlus[0]).execute();

        } catch (IOException e) {
            //Log.v("builder/do background  ", e.toString());
            //Toast.makeText(context, "Insert plus exception thrown", Toast.LENGTH_LONG).show();
        }

        return "updated";
    }

    @Override
    protected void onPostExecute(String me) {
        //for (Quote q : result) {
        //Toast.makeText(context, me, Toast.LENGTH_LONG).show();
        // }

    }

    public void setGamersLocation(GamersLocation gamersLocation) {
        this.gamersLocation = gamersLocation;
    }

    public GamersLocation getGamersLocation() {
        return gamersLocation;
    }



}