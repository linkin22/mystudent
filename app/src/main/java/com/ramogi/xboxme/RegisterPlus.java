package com.ramogi.xboxme;

import android.util.Log;
import android.widget.Toast;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.ramogi.xbox.backend.contactplusApi.model.Contactplus;

/**
 * Created by ROchola on 1/7/2016.
 */
public class RegisterPlus {

    private GoogleAccountCredential credential;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    private String result;

    public  RegisterPlus(String email, String regid){

        Contactplus contactplus = new Contactplus();
        contactplus.setEmail(email);
        contactplus.setRegId(regid);
        //this.credential = credential;

        InsertContactCallback insertContactCallback = new InsertContactCallback() {
            @Override
            public void querycomplete(String result) {

                Log.v("Register plus ", result);
                //setResult(result);
            }
        };

        InsertContact insertContact = new InsertContact(contactplus, insertContactCallback);
        insertContact.execute();

    }
}
