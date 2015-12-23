package com.ramogi.xboxme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class TPAdmin extends Activity implements
        View.OnClickListener{

    private String displayname;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tpadmin);

        Bundle bundle = getIntent().getExtras();

        email = bundle.getString("emailadd");
        displayname = bundle.getString("name");

        //findViewById(R.id.addteacherbtn).setOnClickListener(this);
        findViewById(R.id.teacherbtn).setOnClickListener(this);
        findViewById(R.id.parentbtn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.parentbtn:
                Toast.makeText(this,"parent", Toast.LENGTH_SHORT).show();

                break;
            case R.id.teacherbtn:
                Intent teacherActivity = new Intent(getApplicationContext(), TeacherActivity.class);
                teacherActivity.putExtra("emailadd",email);
                teacherActivity.putExtra("name", displayname);
                startActivity(teacherActivity);
                break;
            default:
                //Toast.makeText(this,"updated", Toast.LENGTH_SHORT).show();

                break;
        }
    }

}
