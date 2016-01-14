package com.ramogi.xboxme;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by ROchola on 1/14/2016.
 */
public class ExamSummaryFragment extends Fragment {


    public ExamSummaryFragment(){


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_examsummary, container, false);

        TextView tprofilename = (TextView)rootView.findViewById(R.id.studentname);
        tprofilename.setText("Exam Summary fragment");

        Button tab1 = (Button)rootView.findViewById(R.id.tab1);
        tab1.setEnabled(true);
        Button tab2 = (Button)rootView.findViewById(R.id.tab2);
        tab2.setEnabled(false);
        Button tab3 = (Button)rootView.findViewById(R.id.tab3);
        tab3.setEnabled(true);

        rootView.findViewById(R.id.tab1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ExamDetailFragment myProfileFragment = new ExamDetailFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, myProfileFragment).commit();

            }
        });

        rootView.findViewById(R.id.tab3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ExamCommentsFragment myProfileFragment = new ExamCommentsFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, myProfileFragment).commit();

            }
        });


        //getActivity().setTitle("Profile");

                //stopprogress();



        return rootView;


    }

}
