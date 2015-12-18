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
import com.ramogi.xbox.backend.teacherApi.TeacherApi;
import com.ramogi.xbox.backend.teacherApi.model.Teacher;
import com.ramogi.xbox.backend.unknownApi.model.Unknown;
import com.ramogi.xbox.backend.unknownApi.UnknownApi;

import java.io.IOException;

/**
 * Created by ROchola on 11/19/2015.
 */


/**
 * Created by ROchola on 2/15/2015.
 */
public class InsertUnknown extends AsyncTask<Unknown, Void, String> {

    private static UnknownApi myApiService = null;
    private Context context;
    private GoogleAccountCredential credential;
    public Unknown unknown;

    public InsertUnknown(Unknown unknown, Context context, GoogleAccountCredential credential) {
        setUnknown(unknown);
        this.context = context;
        this.credential = credential;
    }

    @Override
    protected String doInBackground(Unknown... locationPlus) {

        // Only do this once
        // options for running against local devappserver
        // - 10.0.2.2 is localhost's IP address in Android emulator
        // - turn off compression when running against local devappserver

        if (myApiService == null) {
            UnknownApi.Builder builder = new UnknownApi.Builder(
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
            getUnknown().setCreated(new DateTime(new java.util.Date()));
            myApiService.insert(getUnknown()).execute();
            //myApiService.update(locationPlus[0].getEmail(),locationPlus[0]).execute();

        } catch (IOException e) {
            //Log.v("builder/do background  ", e.toString());
            Toast.makeText(context, "Insert plus exception thrown", Toast.LENGTH_LONG).show();
        }

        return "Thank you";
    }

    @Override
    protected void onPostExecute(String me) {
        //for (Quote q : result) {
        //Toast.makeText(context, "Thank you", Toast.LENGTH_LONG).show();
        // }

    }

    public void setUnknown(Unknown unknown) {
        this.unknown = unknown;
    }

    public Unknown getUnknown() {
        return unknown;
    }



}