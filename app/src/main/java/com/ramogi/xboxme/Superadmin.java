package com.ramogi.xboxme;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Toast;

public class Superadmin extends Activity implements
        View.OnClickListener{

    private String displayname;
    private String email;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_superadmin);

        Bundle bundle = getIntent().getExtras();

        email = bundle.getString("emailadd");
        displayname = bundle.getString("name");

        findViewById(R.id.addteacherbtn).setOnClickListener(this);
        findViewById(R.id.teacherbtn).setOnClickListener(this);
        findViewById(R.id.parentbtn).setOnClickListener(this);
        findViewById(R.id.chatbtn).setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addteacherbtn:

                Intent createTeacher = new Intent(getApplicationContext(), CreateTeacher.class);
                createTeacher.putExtra("emailadd", email );
                createTeacher.putExtra("name", displayname);
                startActivity(createTeacher);

                break;
            case R.id.parentbtn:
                Toast.makeText(this,"parent", Toast.LENGTH_SHORT).show();

                break;
            case R.id.teacherbtn:
                Intent teacherActivity = new Intent(getApplicationContext(), TeacherActivity.class);
                teacherActivity.putExtra("emailadd",email);
                teacherActivity.putExtra("name", displayname);
                startActivity(teacherActivity);

                break;
            case R.id.chatbtn:
                Intent chatActivity = new Intent(getApplicationContext(), MainActivity.class);
                chatActivity.putExtra("emailadd",email);
                chatActivity.putExtra("name", displayname);
                startActivity(chatActivity);
                break;
        }
    }

}
