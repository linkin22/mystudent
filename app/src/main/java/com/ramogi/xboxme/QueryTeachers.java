package com.ramogi.xboxme;

import android.os.AsyncTask;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.ramogi.xbox.backend.teacherApi.model.CollectionResponseTeacher;
import com.ramogi.xbox.backend.teacherApi.model.Teacher;
import com.ramogi.xbox.backend.teacherApi.TeacherApi;
//import com.ramogi.xbox.backend.gamersLocationApi.GamersLocationApi;
//import com.ramogi.xbox.backend.gamersLocationApi.model.CollectionResponseGamersLocation;
//import com.ramogi.xbox.backend.gamersLocationApi.model.GamersLocation;

import java.io.IOException;
import java.util.List;

/**
 * Created by ROchola on 11/22/2015.
 */
public class QueryTeachers extends AsyncTask<Void, Void, CollectionResponseTeacher> {

    private GoogleAccountCredential credential;
    private QueryTeacherCallback queryTeacherCallback ;
    private int limit;
    private static TeacherApi myApiService = null;

    QueryTeachers(int limit, QueryTeacherCallback queryTeacherCallback, GoogleAccountCredential credential) {

        this.credential = credential;
        this.limit = limit;
        this.queryTeacherCallback = queryTeacherCallback;
    }

    protected CollectionResponseTeacher doInBackground(Void... unused) {
        CollectionResponseTeacher teacher = null;

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
            teacher = myApiService.list().execute();

            }catch(IOException e){
                //Log.v("builder/do background", e.toString());
            }

            return teacher;
        }
    protected void onPostExecute(CollectionResponseTeacher teacher) {

        // Do something with the result.
        //ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        List<Teacher> _list = teacher.getItems();
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
        queryTeacherCallback.querycomplete(_list);
    }
}
