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

import com.ramogi.xbox.backend.studentApi.StudentApi;
import com.ramogi.xbox.backend.studentApi.model.Student;


import java.io.IOException;

/**
 * Created by ROchola on 11/19/2015.
 */


/**
 * Created by ROchola on 2/15/2015.
 */
public class InsertStudent extends AsyncTask<Student, Void, String> {

    private static StudentApi myApiService = null;
    private Context context;
    private GoogleAccountCredential credential;
    public Student student;

    public InsertStudent(Student student, Context context, GoogleAccountCredential credential) {
        setStudent(student);
        this.context = context;
        this.credential = credential;
    }

    @Override
    protected String doInBackground(Student... locationPlus) {

        // Only do this once
        // options for running against local devappserver
        // - 10.0.2.2 is localhost's IP address in Android emulator
        // - turn off compression when running against local devappserver

        if (myApiService == null) {
            StudentApi.Builder builder = new StudentApi.Builder(
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
            getStudent().setCreatedate(new DateTime(new java.util.Date()));
            myApiService.insert(getStudent()).execute();
            //myApiService.update(locationPlus[0].getEmail(),locationPlus[0]).execute();

        } catch (IOException e) {
            //Log.v("builder/do background  ", e.toString());
            Toast.makeText(context, "Insert student error, kindly try again", Toast.LENGTH_LONG).show();
        }

        return "Student Added";
    }

    @Override
    protected void onPostExecute(String me) {
        //for (Quote q : result) {
        Toast.makeText(context, me, Toast.LENGTH_LONG).show();
        // }

    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Student getStudent() {
        return student;
    }



}