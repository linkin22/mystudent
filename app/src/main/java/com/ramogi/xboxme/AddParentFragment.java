package com.ramogi.xboxme;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.ramogi.xbox.backend.roleApi.model.Role;
import com.ramogi.xbox.backend.studentApi.model.Student;
import com.ramogi.xbox.backend.teacherApi.model.Teacher;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

/**
 * Created by ROchola on 1/15/2016.
 */
public class AddParentFragment extends Fragment {



    List<Student> students1 = new List<Student>() {
        @Override
        public void add(int location, Student object) {

        }

        @Override
        public boolean add(Student object) {
            return false;
        }

        @Override
        public boolean addAll(int location, Collection<? extends Student> collection) {
            return false;
        }

        @Override
        public boolean addAll(Collection<? extends Student> collection) {
            return false;
        }

        @Override
        public void clear() {

        }

        @Override
        public boolean contains(Object object) {
            return false;
        }

        @Override
        public boolean containsAll(Collection<?> collection) {
            return false;
        }

        @Override
        public Student get(int location) {
            return null;
        }

        @Override
        public int indexOf(Object object) {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @NonNull
        @Override
        public Iterator<Student> iterator() {
            return null;
        }

        @Override
        public int lastIndexOf(Object object) {
            return 0;
        }

        @Override
        public ListIterator<Student> listIterator() {
            return null;
        }

        @NonNull
        @Override
        public ListIterator<Student> listIterator(int location) {
            return null;
        }

        @Override
        public Student remove(int location) {
            return null;
        }

        @Override
        public boolean remove(Object object) {
            return false;
        }

        @Override
        public boolean removeAll(Collection<?> collection) {
            return false;
        }

        @Override
        public boolean retainAll(Collection<?> collection) {
            return false;
        }

        @Override
        public Student set(int location, Student object) {
            return null;
        }

        @Override
        public int size() {
            return 0;
        }

        @NonNull
        @Override
        public List<Student> subList(int start, int end) {
            return null;
        }

        @NonNull
        @Override
        public Object[] toArray() {
            return new Object[0];
        }

        @NonNull
        @Override
        public <T> T[] toArray(T[] array) {
            return null;
        }
    };

    public AddParentFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_addparent, container, false);



        final AutoCompleteTextView searchStudentAuto =
                (AutoCompleteTextView)rootView.findViewById(R.id.searchStudentAutoComplete);

        //final Student studentioio = new Student();

        final Student studentAddParent = new Student();

        final InsertStudentCallback insertStudentCallback = new InsertStudentCallback() {
            @Override
            public void querycomplete(String result) {

                Toast.makeText(rootView.getContext(), result, Toast.LENGTH_LONG).show();

                EditText multiLineTextView = (EditText)rootView.findViewById(R.id.studentInfoMultiText);
                EditText parentemail = (EditText)rootView.findViewById(R.id.parentEmail);
                EditText parentphone = (EditText)rootView.findViewById(R.id.parentPhone);

                multiLineTextView.setText("");
                searchStudentAuto.setText("");
                parentemail.setText("");
                parentphone.setText("");



            }
        };



        //Set adapter to AutoCompleteTextView
        //textView.setAdapter(adapter);
        searchStudentAuto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Log.v("OnItemSelected ", " position " + position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        searchStudentAuto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.v("OnItemClick ", " position " + position);

                String selectedName = parent.getItemAtPosition(position).toString();

                Log.v("OnItemClick ", " value " + selectedName);

                String[] names = new String[students1.size()];

                for (int i = 0; i < students1.size(); i++) {

                    names[i] = students1.get(i).getStudentname();
                    Log.v(" student names ", " " + names[i]);

                    if(names[i].matches(selectedName)){

                        studentAddParent.setAdmno(students1.get(i).getAdmno());
                        studentAddParent.setGender(students1.get(i).getGender());
                        studentAddParent.setStudentname(selectedName);
                        studentAddParent.setSchoolname(students1.get(i).getSchoolname());

                        EditText multiLineTextView = (EditText)rootView.findViewById(R.id.studentInfoMultiText);
                        String studentInfo = studentAddParent.getAdmno() +" "+ studentAddParent.getStudentname() +
                                " "+studentAddParent.getGender().toLowerCase();

                        multiLineTextView.setText(studentInfo);


                    }
                    else{
                        Toast.makeText(rootView.getContext(),"Name not found",Toast.LENGTH_LONG).show();
                    }

                }


            }
        });

        rootView.findViewById(R.id.addParentBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.v("button clicked ", " before adding");

                //EditText studentname = (EditText)rootView.findViewById(R.id.addStudentName);
                //EditText schoolname = (EditText)rootView.findViewById(R.id.addStudentSchool);
                //EditText studentadmno = (EditText)rootView.findViewById(R.id.addStudentAdmno);
                EditText parentemail = (EditText)rootView.findViewById(R.id.parentEmail);
                EditText parentphone = (EditText)rootView.findViewById(R.id.parentPhone);

                int tmobile = 0;

                try {
                    tmobile = Integer.parseInt(parentphone.getText().toString().trim());
                    Log.v("parent phone "," inside try "+tmobile);
                } catch(NumberFormatException nfe) {
                    Log.v("parent phone "," inside catch "+tmobile);


                }
                Log.v("parent phone ", " after tryandcatch " + tmobile);

                studentAddParent.setCreatedby(Common.getPreferredEmail());
                //student.setStudentname(studentname.getText().toString().trim());
                //student.setSchoolname(schoolname.getText().toString().trim());
                //student.setAdmno(studentadmno.getText().toString().trim());
                studentAddParent.setParentemail(parentemail.getText().toString().trim());
                studentAddParent.setParentphone(tmobile);

                Role role = new Role();

                role.setRole("P");
                role.setCreatedby(Common.getPreferredEmail());

                InsertRole insertRole = new InsertRole(role);
                insertRole.execute();


                InsertStudent insertStudent = new InsertStudent(studentAddParent,insertStudentCallback);
                insertStudent.execute();



                Log.v("button clicked ", " After adding");



            }
        });


        QueryOneTeacherCallback qotc = new QueryOneTeacherCallback() {
            @Override
            public void querycomplete(Teacher teacher) {

                Log.v("queryteacher callback"," "+teacher.getTschool());

                QueryStudentsCallback qscb = new QueryStudentsCallback() {
                    @Override
                    public void querycomplete(List<Student> students) {

                        students1 = students;

                        if(students.size()<1){

                        }
                        else {

                            Log.v("querystudent callback ", " " + students.size());

                            String[] names = new String[students.size()];

                            for (int i = 0; i < students.size(); i++) {

                                names[i] = students.get(i).getStudentname();


                                Log.v(" student names ", " " + names[i]);

                            }

                            List list = Arrays.asList(names);
                            Set set = new HashSet(list);

                            String[] uniqueNames = Arrays.copyOf(set.toArray(), set.toArray().length, String[].class);



                            //String[] COUNTRIES = new String[] {
                            //"Belgium", "France", "Italy", "Germany", "Spain", "Ghana"
                            // };

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                    rootView.getContext(),
                                    android.R.layout.simple_list_item_1, uniqueNames);


                            searchStudentAuto.setThreshold(1);
                            searchStudentAuto.setAdapter(adapter);

                        }

                        //listview.setAdapter(adapter);

                    }
                };

                QueryStudents queryStudents = new QueryStudents(100,qscb,teacher.getTschool());
                queryStudents.execute();


            }
        };

        Log.v(" Teacher's email ", Common.getPreferredEmail());

        QueryOneTeacher queryOneTeacher = new QueryOneTeacher(Common.getPreferredEmail(),qotc);
        queryOneTeacher.execute();

        return rootView;
    }
}
