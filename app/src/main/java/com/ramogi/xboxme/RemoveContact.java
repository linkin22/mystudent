package com.ramogi.xboxme;


import android.os.AsyncTask;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.ramogi.xbox.backend.contactplusApi.ContactplusApi;
import com.ramogi.xbox.backend.contactplusApi.model.Contactplus;
import com.ramogi.xbox.backend.studentApi.model.Student;

import java.io.IOException;

/**
 * Created by ROchola on 11/19/2015.
 */


/**
 * Created by ROchola on 2/15/2015.
 */
public class RemoveContact extends AsyncTask<Student, Void, String> {

    private static ContactplusApi myApiService = null;
    private InsertContactCallback insertContactCallback;
    //private GoogleAccountCredential credential;
    public String email;

    public RemoveContact(String email, InsertContactCallback insertContactCallback) {

        this.email = email;
        this.insertContactCallback = insertContactCallback;
        //this.credential = credential;
    }

    @Override
    protected String doInBackground(Student... locationPlus) {

        String added = null;

        if (myApiService == null) {
            ContactplusApi.Builder builder = new ContactplusApi.Builder(
                    AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl("https://" + Constants.PROJECT_ID + ".appspot.com/_ah/api/")
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });

            myApiService = builder.build();
        }

        try {

            myApiService.remove(email).execute();

            added = email + " removed";

        } catch (IOException e) {

            added = email = " not removed";
        }

        return added;
    }

    @Override
    protected void onPostExecute(String me) {

        insertContactCallback.querycomplete(me);

    }

}