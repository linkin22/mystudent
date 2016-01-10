/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ramogi.xboxme;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.ramogi.xbox.backend.roleApi.model.Role;
import com.ramogi.xbox.backend.studentApi.model.Student;
import com.ramogi.xbox.backend.teacherApi.model.Teacher;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Set;

/**
 * This example illustrates a common usage of the DrawerLayout widget
 * in the Android support library.
 * <p/>
 * <p>When a navigation (left) drawer is present, the host activity should detect presses of
 * the action bar's Up affordance as a signal to open and close the navigation drawer. The
 * ActionBarDrawerToggle facilitates this behavior.
 * Items within the drawer should fall into one of two categories:</p>
 * <p/>
 * <ul>
 * <li><strong>View switches</strong>. A view switch follows the same basic policies as
 * list or tab navigation in that a view switch does not create navigation history.
 * This pattern should only be used at the root activity of a task, leaving some form
 * of Up navigation active for activities further down the navigation hierarchy.</li>
 * <li><strong>Selective Up</strong>. The drawer allows the user to choose an alternate
 * parent for Up navigation. This allows a user to jump across an app's navigation
 * hierarchy at will. The application should treat this as it treats Up navigation from
 * a different task, replacing the current task stack using TaskStackBuilder or similar.
 * This is the only form of navigation drawer that should be used outside of the root
 * activity of a task.</li>
 * </ul>
 * <p/>
 * <p>Right side drawers should be used for actions, not navigation. This follows the pattern
 * established by the Action Bar that navigation should be to the left and actions to the right.
 * An action should be an operation performed on the current contents of the window,
 * for example enabling or disabling a data overlay on top of the current content.</p>
 */
public class TeacherActivity extends Activity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mPlanetTitles;
    private String displayname;
    private static String email;
    private static GoogleAccountCredential credential;
    private SharedPreferences settings;
    private View mProgressView;
    private View mLoginFormView;
    private static String gender = "NA";
    private EditText studentname;
    private EditText schoolname;
    private EditText parentmobile;
    private EditText parentemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_teacher);

        Bundle bundle = getIntent().getExtras();

        email = bundle.getString("emailadd");
        displayname = bundle.getString("name");

       // mLoginFormView = findViewById(R.id.query_form);
       // mProgressView = findViewById(R.id.query_progress);

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

        //setEmail(email);

        mTitle = mDrawerTitle = getTitle();
        mPlanetTitles = getResources().getStringArray(R.array.teacher_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mPlanetTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
                ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         // The action bar home/up action should open or close the drawer.
         // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch(item.getItemId()) {
        case R.id.action_websearch:
            // create intent to perform web search for this planet
            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
            // catch event that there's no activity to handle intent
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
            }
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {

        Fragment fragment = null;

        switch (position) {
            case 0:
                //showProgress(true);
                fragment = new TeacherProfileFragment();
                //showProgress(false);
                break;
            case 1:
                fragment = new AddStudentFragment();
                break;
            case 2:
                fragment = new AddParentFragment();
                break;
            case 3:
                //fragment = new ReadFragment();
                Toast.makeText(this,"Fourth",Toast.LENGTH_SHORT).show();
                break;
            case 4:
                Toast.makeText(this,"Fifth",Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(mPlanetTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);

        } else {
            Log.e("MainActivityOriginal", "Error in creating fragment");
        }
        // update the main content by replacing fragments

        /*
        Fragment fragment = new PlanetFragment();
        Bundle args = new Bundle();
        args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
        fragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mPlanetTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
        */
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * Fragment that appears in the "content_frame", shows a planet
     */
    public static class PlanetFragment extends Fragment {
        public static final String ARG_PLANET_NUMBER = "planet_number";

        public PlanetFragment() {
            // Empty constructor required for fragment subclasses
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_planet, container, false);
            int i = getArguments().getInt(ARG_PLANET_NUMBER);
            String planet = getResources().getStringArray(R.array.planets_array)[i];

            int imageId = getResources().getIdentifier(planet.toLowerCase(Locale.getDefault()),
                            "drawable", getActivity().getPackageName());


            ((ImageView) rootView.findViewById(R.id.image)).setImageResource(imageId);
            getActivity().setTitle(planet);
            return rootView;
        }
    }

    public static class TeacherProfileFragment extends Fragment{

        public TeacherProfileFragment(){


        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            final View rootView = inflater.inflate(R.layout.fragment_teacherprofile, container, false);

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

                    getActivity().setTitle(R.string.teacherfragmentprofile);

                    //stopprogress();


                }
            };

            QueryOneTeacher queryOneTeacher = new QueryOneTeacher(email,qotc,credential);
            queryOneTeacher.execute();

            return rootView;


        }

    }

    public static class AddParentFragment extends Fragment {
        public static final String ARG_PLANET_NUMBER = "planet_number";


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

                    studentAddParent.setCreatedby(email);
                    //student.setStudentname(studentname.getText().toString().trim());
                    //student.setSchoolname(schoolname.getText().toString().trim());
                    //student.setAdmno(studentadmno.getText().toString().trim());
                    studentAddParent.setParentemail(parentemail.getText().toString().trim());
                    studentAddParent.setParentphone(tmobile);

                    Role role = new Role();

                    role.setRole("P");
                    role.setCreatedby(email);

                    InsertRole insertRole = new InsertRole(role,credential);
                    insertRole.execute();


                    InsertStudent insertStudent = new InsertStudent(studentAddParent,insertStudentCallback, credential);
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

                    QueryStudents queryStudents = new QueryStudents(100,qscb,credential,teacher.getTschool());
                    queryStudents.execute();


                }
            };

            Log.v(" Teacher's email ", email);

            QueryOneTeacher queryOneTeacher = new QueryOneTeacher(email,qotc,credential);
            queryOneTeacher.execute();

            return rootView;
        }
    }

    public static class TeacherExamFragment extends Fragment{

        public TeacherExamFragment(){

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_teacherexam, container, false);

            TextView tschoolname = (TextView)rootView.findViewById(R.id.addstudentschool);
            tschoolname.setText("Teacher exam working");

            getActivity().setTitle(R.string.teacherfragmentprofile);

            return rootView;


        }
    }

    public static class AddStudentFragment extends Fragment{

        public AddStudentFragment(){

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
                    EditText parentemail = (EditText)rootView.findViewById(R.id.addParentEmail);
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
                    EditText parentemail = (EditText)rootView.findViewById(R.id.addParentEmail);
                    EditText parentphone = (EditText)rootView.findViewById(R.id.addParentPhone);

                    int tmobile = 0;

                    try {
                        tmobile = Integer.parseInt(parentphone.getText().toString().trim());
                        Log.v("parent phone "," inside try "+tmobile);
                    } catch(NumberFormatException nfe) {
                        Log.v("parent phone "," inside catch "+tmobile);


                    }
                    Log.v("parent phone "," after tryandcatch "+tmobile);

                    student.setCreatedby(email);
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
                    role.setCreatedby(email);

                    InsertRole insertRole = new InsertRole(role,credential);
                    insertRole.execute();

                    InsertStudent insertStudent = new InsertStudent(student,insertStudentCallback, credential);
                    insertStudent.execute();



                    Log.v("button clicked ", " After adding");
                }
            });


            return rootView;


        }

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
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

    public static  String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}