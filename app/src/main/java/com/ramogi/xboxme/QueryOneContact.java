package com.ramogi.xboxme;

import android.os.AsyncTask;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.ramogi.xbox.backend.teacherApi.TeacherApi;
import com.ramogi.xbox.backend.teacherApi.model.Teacher;
import com.ramogi.xbox.backend.contactplusApi.model.Contactplus;
import com.ramogi.xbox.backend.contactplusApi.ContactplusApi;

import java.io.IOException;

/**
 * Created by ROchola on 11/22/2015.
 */
public class QueryOneContact extends AsyncTask<Void, Void, Contactplus> {

    private GoogleAccountCredential credential;
    private QueryOneContactCallback queryOneContactCallback ;

    private static ContactplusApi myApiService = null;
    private String email;

    public QueryOneContact(String email, QueryOneContactCallback queryOneContactCallback) {

        //this.credential = credential;
        this.queryOneContactCallback = queryOneContactCallback;
        this.email = email;
    }

    protected Contactplus doInBackground(Void... unused) {
        Contactplus contactplus = null;

        try {
            if (myApiService == null) { // Only do this once
                ContactplusApi.Builder builder = new ContactplusApi.Builder(
                        AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
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

            contactplus = myApiService.get(email).execute();

            }catch(IOException e){
                //Log.v("builder/do background", e.toString());
            }

            return contactplus;
        }
    protected void onPostExecute(Contactplus contactplus) {


        queryOneContactCallback.querycomplete(contactplus);
    }
}
