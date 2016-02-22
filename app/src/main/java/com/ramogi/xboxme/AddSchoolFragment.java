package com.ramogi.xboxme;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.ramogi.xbox.backend.roleApi.model.Role;
import com.ramogi.xbox.backend.studentApi.model.Student;

/**
 * Created by ROchola on 1/15/2016.
 */
public class AddSchoolFragment extends Fragment {

    public AddSchoolFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_addstudent, container, false);

        final Student student = new Student();

        final InsertStudentCallback insertStudentCallback = new InsertStudentCallback() {
            @Override
            public void querycomplete(String result) {

                //Toast.makeText(getActivity().getApplication().getApplicationContext(), result, Toast.LENGTH_LONG).show();

                EditText studentname = (EditText)rootView.findViewById(R.id.addStudentName);
                EditText schoolname = (EditText)rootView.findViewById(R.id.addStudentSchool);
                EditText studentadmno = (EditText)rootView.findViewById(R.id.addStudentAdmno);
                EditText parentemail = (EditText)rootView.findViewById(R.id.addSchoolEmail);
                EditText parentphone = (EditText)rootView.findViewById(R.id.addParentPhone);
                RadioGroup radioGroup = (RadioGroup)rootView.findViewById(R.id.radioGroup);

                studentname.setText("");
                schoolname.setText("");
                studentadmno.setText("");
                parentemail.setText("");
                parentphone.setText("");
                radioGroup.clearCheck();



            }
        };



        rootView.findViewById(R.id.maleRadioBtn).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                student.setGender("MALE");
                Log.v("male radio button ", "male");
            }
        });

        rootView.findViewById(R.id.femaleRadioBtn).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                student.setGender("FEMALE");
                Log.v("female radio button ", "female");
            }
        });




        rootView.findViewById(R.id.addStudentBtn).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Log.v("button clicked ", " before adding");


                EditText studentname = (EditText)rootView.findViewById(R.id.addStudentName);
                EditText schoolname = (EditText)rootView.findViewById(R.id.addStudentSchool);
                EditText studentadmno = (EditText)rootView.findViewById(R.id.addStudentAdmno);
                EditText parentemail = (EditText)rootView.findViewById(R.id.addSchoolEmail);
                EditText parentphone = (EditText)rootView.findViewById(R.id.addParentPhone);

                int tmobile = 0;

                try {
                    tmobile = Integer.parseInt(parentphone.getText().toString().trim());
                    Log.v("parent phone "," inside try "+tmobile);
                } catch(NumberFormatException nfe) {
                    Log.v("parent phone "," inside catch "+tmobile);


                }
                Log.v("parent phone "," after tryandcatch "+tmobile);
                Log.v("parent phone "," inside catch "+Common.getPreferredEmail());

                student.setCreatedby(Common.getPreferredEmail());
                student.setStudentname(studentname.getText().toString().trim());
                student.setSchoolname(schoolname.getText().toString().trim());
                student.setAdmno(studentadmno.getText().toString().trim());
                student.setParentemail(parentemail.getText().toString().trim());
                student.setParentphone(tmobile);

                Log.v("button clicked ", student.getAdmno());
                Log.v("button clicked ", student.getCreatedby());
                Log.v("button clicked ", student.getParentemail());
                Log.v("button clicked ", student.getGender());
                Log.v("button clicked ", student.getSchoolname());
                Log.v("button clicked ", student.getStudentname());

                Role role = new Role();

                role.setRole("P");
                role.setCreatedby(Common.getPreferredEmail());

                InsertRole insertRole = new InsertRole(role);
                insertRole.execute();

                InsertStudent insertStudent = new InsertStudent(student,insertStudentCallback);
                insertStudent.execute();



                Log.v("button clicked ", " After adding");
            }
        });


        return rootView;


    }

}
