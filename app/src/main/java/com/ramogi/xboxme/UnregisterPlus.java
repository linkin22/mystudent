package com.ramogi.xboxme;

import android.util.Log;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

/**
 * Created by ROchola on 1/7/2016.
 */
public class UnregisterPlus {

    private String email;
    //private GoogleAccountCredential credential;

    public UnregisterPlus(String email){

        this.email = email;


        InsertContactCallback insertContactCallback = new InsertContactCallback() {
            @Override
            public void querycomplete(String result) {

                Log.v("Unregister plus ", result);
            }
        };

        RemoveContact removeContact = new RemoveContact(email,insertContactCallback);
        removeContact.execute();

    }
}
