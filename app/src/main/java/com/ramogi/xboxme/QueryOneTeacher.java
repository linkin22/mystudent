package com.ramogi.xboxme;

import android.os.AsyncTask;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.ramogi.xbox.backend.roleApi.RoleApi;
import com.ramogi.xbox.backend.roleApi.model.Role;
import com.ramogi.xbox.backend.teacherApi.TeacherApi;
import com.ramogi.xbox.backend.teacherApi.model.Teacher;

import java.io.IOException;

//import com.ramogi.xbox.backend.gamersLocationApi.GamersLocationApi;
//import com.ramogi.xbox.backend.gamersLocationApi.model.CollectionResponseGamersLocation;
//import com.ramogi.xbox.backend.gamersLocationApi.model.GamersLocation;

/**
 * Created by ROchola on 11/22/2015.
 */
public class QueryOneTeacher extends AsyncTask<Void, Void, Teacher> {

    private GoogleAccountCredential credential;
    private QueryOneTeacherCallback queryOneTeacherCallback ;

    private static TeacherApi myApiService = null;
    private String email;

    QueryOneTeacher(String email, QueryOneTeacherCallback queryOneTeacherCallback , GoogleAccountCredential credential) {

        this.credential = credential;

        this.queryOneTeacherCallback = queryOneTeacherCallback;
        this.email = email;
    }

    protected Teacher doInBackground(Void... unused) {
        Teacher teacher = null;

        try {
            if (myApiService == null) { // Only do this once
                TeacherApi.Builder builder = new TeacherApi.Builder(
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

            teacher = myApiService.get(email).execute();

            }catch(IOException e){
                //Log.v("builder/do background", e.toString());
            }

            return teacher;
        }
    protected void onPostExecute(Teacher teacher) {


        queryOneTeacherCallback.querycomplete(teacher);
    }
}
