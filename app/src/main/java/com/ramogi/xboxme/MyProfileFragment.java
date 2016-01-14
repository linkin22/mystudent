package com.ramogi.xboxme;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ramogi.xbox.backend.teacherApi.model.Teacher;

/**
 * Created by ROchola on 1/14/2016.
 */
public class MyProfileFragment extends Fragment {


    public MyProfileFragment(){


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_addstudent, container, false);

        QueryOneTeacherCallback qotc = new QueryOneTeacherCallback() {
            @Override
            public void querycomplete(Teacher teacher) {

                TextView tprofilename = (TextView)rootView.findViewById(R.id.studentname);
                tprofilename.setText(teacher.getTname());
                TextView tprofileschool = (TextView)rootView.findViewById(R.id.addstudentschool);
                tprofileschool.setText(teacher.getTschool());
                TextView tprofileemail = (TextView)rootView.findViewById(R.id.addparentemail);
                tprofileemail.setText(teacher.getEmail());
                TextView tprofilephone = (TextView)rootView.findViewById(R.id.addstudentphone);
                tprofilephone.setText("" + teacher.getTmobile());

                getActivity().setTitle("Profile");

                //stopprogress();


            }
        };

        QueryOneTeacher queryOneTeacher = new QueryOneTeacher("o.ramogi@gmail.com",qotc);
        queryOneTeacher.execute();

        return rootView;


    }

}
