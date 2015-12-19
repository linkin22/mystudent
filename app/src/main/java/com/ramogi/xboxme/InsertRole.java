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
import com.ramogi.xbox.backend.roleApi.RoleApi;
import com.ramogi.xbox.backend.roleApi.model.Role;

import java.io.IOException;

/**
 * Created by ROchola on 11/19/2015.
 */


/**
 * Created by ROchola on 2/15/2015.
 */
public class InsertRole extends AsyncTask<Role, Void, String> {

    private static RoleApi myApiService = null;
    private Context context;
    private GoogleAccountCredential credential;
    public Role role;

    public InsertRole(Role role, Context context, GoogleAccountCredential credential) {
        setRole(role);
        this.context = context;
        this.credential = credential;
    }

    @Override
    protected String doInBackground(Role... locationPlus) {

        // Only do this once
        // options for running against local devappserver
        // - 10.0.2.2 is localhost's IP address in Android emulator
        // - turn off compression when running against local devappserver

        if (myApiService == null) {
            RoleApi.Builder builder = new RoleApi.Builder(
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
            getRole().setCreated(new DateTime(new java.util.Date()));
            myApiService.insert(getRole()).execute();
            //myApiService.update(locationPlus[0].getEmail(),locationPlus[0]).execute();

        } catch (IOException e) {
            //Log.v("builder/do background  ", e.toString());
            Toast.makeText(context, "Insert plus exception thrown", Toast.LENGTH_LONG).show();
        }

        return "Role Added";
    }

    @Override
    protected void onPostExecute(String me) {
        //for (Quote q : result) {
        Toast.makeText(context, me, Toast.LENGTH_LONG).show();
        // }

    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Role getRole() {
        return role;
    }



}