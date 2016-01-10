package com.ramogi.xboxme;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.ramogi.xbox.backend.roleApi.model.Role;
import com.ramogi.xbox.backend.teacherApi.model.Teacher;

public class CreateTeacher extends Activity implements
        View.OnClickListener{

    private String displayname;
    private String email;
    private GoogleAccountCredential credential;
    private SharedPreferences settings;
    private EditText fullname;
    private EditText schoolname;
    private EditText mobile;
    private EditText temail;
    private String gender;
    private String myrole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_teacher);

        Bundle bundle = getIntent().getExtras();

        email = bundle.getString("emailadd");
        displayname = bundle.getString("name");
        gender = "NA";

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

        findViewById(R.id.submitteacherbtn).setOnClickListener(this);
        findViewById(R.id.maleRadioBtn).setOnClickListener(this);
        findViewById(R.id.femaleRadioBtn).setOnClickListener(this);
    }


    private void queryRole(final String temail, final String tfullname){

        QueryRoleCallback qcb = new QueryRoleCallback() {
            @Override
            public void querycomplete(Role role) {

                afterQueryRole(role, tfullname, temail);

            }
        };

        QueryRole queryRole = new QueryRole(temail,qcb,getCredential());
        queryRole.execute();


    }

    private void afterQueryRole(Role role, String tfullname, String temail){

        String myrole = "";
        try {
            myrole = role.getRole();
            myrole.trim();


            switch (myrole){
                case "S":


                    break;
                case "T":
                    break;
                case "P":
                    myrole = "T"+myrole;
                    role.setRole(myrole);
                    role.setCreatedby(email);

                    InsertRole insertRole = new InsertRole(role,getCredential());
                    insertRole.execute();

                    break;
                case "TP":
                    break;
                case "PT":
                    break;
                default:

                    break;
            }
        }
        catch (java.lang.NullPointerException e){

            Role role1 = new Role();

            role1.setRole("T");
            role1.setFullname(tfullname);
            role1.setEmail(temail);
            role1.setCreatedby(email);

            InsertRole insertRole = new InsertRole(role1,getCredential());
            insertRole.execute();

        }


    }
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.submitteacherbtn:

                fullname = (EditText) findViewById(R.id.nameTeacher);
                mobile = (EditText)findViewById(R.id.phoneTeacher);
                temail = (EditText) findViewById(R.id.emailTeacher);
                schoolname = (EditText) findViewById(R.id.tschoolname);

                int tmobile = 0;

                try {
                    tmobile = Integer.parseInt(mobile.getText().toString().trim());
                } catch(NumberFormatException nfe) {


                }

                String tmail = temail.getText().toString().trim();
                String tfullname = fullname.getText().toString().trim();

                Teacher teacher = new Teacher();
                teacher.setEmail(tmail);
                teacher.setTname(tfullname);
                teacher.setTmobile(tmobile);
                teacher.setCreatedby(email);
                teacher.setTschool(schoolname.getText().toString());
                teacher.setGender(getGender());

                queryRole(tmail,tfullname);

                InsertTeacher insertTeacher = new InsertTeacher(teacher,getApplicationContext(),getCredential());
                insertTeacher.execute();

                break;
            //signIn();

            case R.id.maleRadioBtn:

                setGender("MALE");

                Log.v("onclick ", "male radio button");

                // signOut();
                break;
            case R.id.femaleRadioBtn:

                setGender("FEMALE");

                Log.v("onclick ", "male radio button");
                // revokeAccess();
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }



}
