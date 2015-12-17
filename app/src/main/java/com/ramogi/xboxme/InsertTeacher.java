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

import java.io.IOException;

/**
 * Created by ROchola on 11/19/2015.
 */


/**
 * Created by ROchola on 2/15/2015.
 */
public class InsertTeacher extends AsyncTask<Teacher, Void, String> {

    private static TeacherApi myApiService = null;
    private Context context;
    private GoogleAccountCredential credential;
    public Teacher teacher;

    public InsertTeacher(Teacher teacher, Context context, GoogleAccountCredential credential) {
        setTeacher(teacher);
        this.context = context;
        this.credential = credential;
    }

    @Override
    protected String doInBackground(Teacher... locationPlus) {

        // Only do this once
        // options for running against local devappserver
        // - 10.0.2.2 is localhost's IP address in Android emulator
        // - turn off compression when running against local devappserver

        if (myApiService == null) {
            TeacherApi.Builder builder = new TeacherApi.Builder(
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
            getTeacher().setTcreated(new DateTime(new java.util.Date()));
            myApiService.insert(getTeacher()).execute();
            //myApiService.update(locationPlus[0].getEmail(),locationPlus[0]).execute();

        } catch (IOException e) {
            //Log.v("builder/do background  ", e.toString());
            Toast.makeText(context, "Insert plus exception thrown", Toast.LENGTH_LONG).show();
        }

        return "updated";
    }

    @Override
    protected void onPostExecute(String me) {
        //for (Quote q : result) {
        //Toast.makeText(context, me, Toast.LENGTH_LONG).show();
        // }

    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Teacher getTeacher() {
        return teacher;
    }



}