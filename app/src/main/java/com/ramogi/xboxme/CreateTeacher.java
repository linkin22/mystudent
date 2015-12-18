package com.ramogi.xboxme;

import android.app.Activity;
import android.os.Bundle;

public class CreateTeacher extends Activity {

    private String displayname;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_teacher);

        Bundle bundle = getIntent().getExtras();

        email = bundle.getString("emailadd");
        displayname = bundle.getString("name");
    }
}
