package com.ramogi.xboxme;

import android.os.AsyncTask;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.ramogi.xbox.backend.studentApi.StudentApi;
import com.ramogi.xbox.backend.studentApi.model.CollectionResponseStudent;
import com.ramogi.xbox.backend.studentApi.model.Student;

import java.io.IOException;
import java.util.List;


/**
 * Created by ROchola on 11/22/2015.
 */
public class QueryStudents extends AsyncTask<Void, Void, CollectionResponseStudent> {

    private GoogleAccountCredential credential;
    private QueryStudentsCallback queryStudentsCallback ;
    private int limit;
    private static StudentApi myApiService = null;
    private String schoolname;

    QueryStudents(int limit, QueryStudentsCallback queryStudentsCallback, GoogleAccountCredential credential, String schoolname) {

        this.credential = credential;
        this.limit = limit;
        this.queryStudentsCallback = queryStudentsCallback;
        this.schoolname = schoolname;
    }

    protected CollectionResponseStudent doInBackground(Void... unused) {
        CollectionResponseStudent student = null;

        try {
            if (myApiService == null) { // Only do this once
                StudentApi.Builder builder = new StudentApi.Builder(
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
            student = myApiService.list().execute();

            }catch(IOException e){
                //Log.v("builder/do background", e.toString());
            }

            return student;
        }
    protected void onPostExecute(CollectionResponseStudent student) {

        // Do something with the result.
        //ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        List<Student> _list = student.getItems();
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
        queryStudentsCallback.querycomplete(_list);
    }
}
