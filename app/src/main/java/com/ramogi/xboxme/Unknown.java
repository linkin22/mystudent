package com.ramogi.xboxme;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;


import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.ramogi.xbox.backend.gamersLocationApi.model.GamersLocation;
import com.ramogi.xbox.backend.roleApi.model.Role;


public class Unknown extends AppCompatActivity implements
        View.OnClickListener {

    private String displayname;
    private String email;
    private GoogleAccountCredential credential;
    private SharedPreferences settings;
    private EditText schoolname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unknownuser);

        Bundle bundle = getIntent().getExtras();

        email = bundle.getString("emailadd");
        displayname = bundle.getString("name");

        settings = getSharedPreferences("xboxme", 0);
        credential =
                GoogleAccountCredential.usingAudience(this, "server:client_id:" + Constants.WEB_CLIENT_ID);

        setAccountName(settings.getString("ACCOUNT_NAME", null));
        if (credential.getSelectedAccountName() != null) {
            //detailIntent.putExtra("credential",accountCredentials);

            // Already signed in, begin app!
            //Toast.makeText(getBaseContext(), "Logged in with : " + credential.getSelectedAccountName(), Toast.LENGTH_SHORT).show();
            //Toast.makeText(getBaseContext(), GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext()),Toast.LENGTH_SHORT).show();
        } else {
            // Not signed in, show login window or request an account.
            //chooseAccount();
            setAccountName(email);
        }

        schoolname = (EditText) findViewById(R.id.uSchoolName);

        // Button listeners
        findViewById(R.id.unknownbtn).setOnClickListener(this);
    }

    private void insertUnknown(){

        com.ramogi.xbox.backend.unknownApi.model.Unknown unknown = new com.ramogi.xbox.backend.unknownApi.model.Unknown();
        unknown.setEmail(email);
        unknown.setDisplayname(displayname);
        unknown.setSchoolname(schoolname.getText().toString().trim());

        InsertUnknown insertUnknown = new InsertUnknown(unknown, getApplicationContext(),getCredential());
        insertUnknown.execute();

        Toast.makeText(getApplicationContext(), "Thank you", Toast.LENGTH_LONG).show();


        finish();
        endApplication();

    }

    private void endApplication() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.unknownbtn:
                insertUnknown();
                break;
            //signIn();

            case R.id.sign_out_button:
                // signOut();
                break;
            case R.id.disconnect_button:
                // revokeAccess();
                break;
            case R.id.locationbtn:
                //Toast.makeText(this,"updated", Toast.LENGTH_SHORT).show();
                // uploadLocation();
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    // setAccountName definition
    private void setAccountName(String accountName) {
        //accountCredentials.clear();
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("ACCOUNT_NAME", accountName);
        editor.commit();
        credential.setSelectedAccountName(accountName);
        //accountCredentials.add(credential);
        //this.accountName = accountName;
    }

    public GoogleAccountCredential getCredential() {
        return credential;
    }

}