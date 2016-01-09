package com.ramogi.xboxme;


import android.os.AsyncTask;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.google.api.client.util.DateTime;
import com.ramogi.xbox.backend.contactplusApi.model.Contactplus;
import com.ramogi.xbox.backend.contactplusApi.ContactplusApi;
import com.ramogi.xbox.backend.studentApi.StudentApi;
import com.ramogi.xbox.backend.studentApi.model.Student;

import java.io.IOException;

/**
 * Created by ROchola on 11/19/2015.
 */


/**
 * Created by ROchola on 2/15/2015.
 */
public class InsertContact extends AsyncTask<Student, Void, String> {

    private static ContactplusApi myApiService = null;
    private InsertContactCallback insertContactCallback;
    //private GoogleAccountCredential credential;
    public Contactplus contactplus;

    public InsertContact(Contactplus contactplus, InsertContactCallback insertContactCallback) {

        this.contactplus = contactplus;
        this.insertContactCallback = insertContactCallback;
        //this.credential = credential;
    }

    @Override
    protected String doInBackground(Student... locationPlus) {

        String added = null;

        if (myApiService == null) {
            ContactplusApi.Builder builder = new ContactplusApi.Builder(
                    AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(),null)
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

            myApiService.insert(contactplus).execute();

            added = contactplus.getEmail()+" added";

        } catch (IOException e) {

            added = contactplus.getEmail()+" not added";
        }

        return added;
    }

    @Override
    protected void onPostExecute(String me) {

        insertContactCallback.querycomplete(me);

    }

}