package com.ramogi.xboxme;

import android.os.AsyncTask;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.ramogi.xbox.backend.roleApi.model.CollectionResponseRole;
import com.ramogi.xbox.backend.roleApi.model.Role;
import com.ramogi.xbox.backend.roleApi.RoleApi;

import java.io.IOException;
import java.util.List;

//import com.ramogi.xbox.backend.gamersLocationApi.GamersLocationApi;
//import com.ramogi.xbox.backend.gamersLocationApi.model.CollectionResponseGamersLocation;
//import com.ramogi.xbox.backend.gamersLocationApi.model.GamersLocation;

/**
 * Created by ROchola on 11/22/2015.
 */
public class QueryRole extends AsyncTask<Void, Void, Role> {

    private GoogleAccountCredential credential;
    private QueryRoleCallback queryRoleCallback ;

    private static RoleApi myApiService = null;
    private String email;

    QueryRole(String email, QueryRoleCallback queryRoleCallback, GoogleAccountCredential credential) {

        this.credential = credential;

        this.queryRoleCallback = queryRoleCallback;
        this.email = email;
    }

    protected Role doInBackground(Void... unused) {
        Role role = null;

        try {
            if (myApiService == null) { // Only do this once
                RoleApi.Builder builder = new RoleApi.Builder(
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

            role = myApiService.get(email).execute();

            }catch(IOException e){
                //Log.v("builder/do background", e.toString());
            }

            return role;
        }
    protected void onPostExecute(Role role) {



        // Do something with the result.
        //ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        //List<Role> _list = role.getItems();
        /*
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
        */
        queryRoleCallback.querycomplete(role);
    }
}
