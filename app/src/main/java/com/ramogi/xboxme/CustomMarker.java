package com.ramogi.xboxme;

/**
 * Created by ROchola on 11/22/2015.
 */

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
//import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.DateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;




import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.DateTime;
import com.ramogi.xboxme.clustering.Cluster;
import com.ramogi.xboxme.clustering.ClusterManager;
import com.ramogi.xboxme.clustering.view.DefaultClusterRenderer;
import com.ramogi.xboxme.ui.IconGenerator;
import com.ramogi.xboxme.model.Person;
import com.ramogi.xbox.backend.gamersLocationApi.model.GamersLocation;
//import com.igomar.studio.backend.locationPlusApi.model.LocationPlus;


/**
 * Demonstrates heavy customisation of the look of rendered clusters.
 */
public class CustomMarker extends BaseActivity
        implements ClusterManager.OnClusterClickListener<Person>,
        ClusterManager.OnClusterInfoWindowClickListener<Person>,
        ClusterManager.OnClusterItemClickListener<Person>,
        ClusterManager.OnClusterItemInfoWindowClickListener<Person> {

    private ClusterManager<Person> mClusterManager;
    private Random mRandom = new Random(1984);
    //public String[] locationmap;
    public String myaccount, mydisplayname,myuseremail,myphoto;
    //private ArrayList<String> peopleList;
    //private HashMap<String, String> hashMap;
    //private ArrayList<LatLng> plusLocations = new ArrayList<LatLng>();
    //private ArrayList<String> plusUrl = new ArrayList<String>();
    //private ArrayList<String> plusPeople = new ArrayList<String>();
    //private ArrayList<DateTime> plusTime = new ArrayList<DateTime>();
    public double mylat;
    public double mylong;
    //private boolean haslocation = false;
    private LatLng position;
    //private String urldisplay;

    //private static final String KEY_PEOPLE = "PLUSPEOPLE";
    //private static final String KEY_LOCATION = "PLUSLOCATION";
    //private static final String KEY_URL = "PLUSURL";
    private GoogleAccountCredential credential;
    private SharedPreferences settings;
   //private static final int REQUEST_ACCOUNT_PICKER = 7;
    //private  String accountname;



    /**
     * Draws profile photos inside markers (using IconGenerator).
     * When there are multiple people in the cluster, draw multiple photos (using MultiDrawable).
     */
    private class PersonRenderer extends DefaultClusterRenderer<Person> {
        private final IconGenerator mIconGenerator = new IconGenerator(getApplicationContext());
        private final IconGenerator mClusterIconGenerator = new IconGenerator(getApplicationContext());
        private final ImageView mImageView;
        private final ImageView mClusterImageView;
        private final int mDimension;




        public PersonRenderer() {
            super(getApplicationContext(), getMap(), mClusterManager);

            View multiProfile = getLayoutInflater().inflate(R.layout.multi_profile, null);
            mClusterIconGenerator.setContentView(multiProfile);
            mClusterImageView = (ImageView) multiProfile.findViewById(R.id.image);

            mImageView = new ImageView(getApplicationContext());
            mDimension = (int) getResources().getDimension(R.dimen.custom_profile_image);
            mImageView.setLayoutParams(new ViewGroup.LayoutParams(mDimension, mDimension));
            int padding = (int) getResources().getDimension(R.dimen.custom_profile_padding);
            mImageView.setPadding(padding, padding, padding, padding);
            mIconGenerator.setContentView(mImageView);
        }

        @Override
        protected void onBeforeClusterItemRendered(Person person, MarkerOptions markerOptions) {

            Utility u = new Utility();
            String timeFormat = u.formatTime(person.updateTime.toString());
            // Draw a single person.
            // Set the info window to show their name.
            mImageView.setImageDrawable(person.profilePhoto);
            //mImageView.setImageResource(person.profilePhoto);
            Bitmap icon = mIconGenerator.makeIcon();
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(person.name+" "+timeFormat);
        }

        @Override
        protected void onBeforeClusterRendered(Cluster<Person> cluster, MarkerOptions markerOptions) {
            // Draw multiple people.
            // Note: this method runs on the UI thread. Don't spend too much time in here (like in this example).
            List<Drawable> profilePhotos = new ArrayList<Drawable>(Math.min(4, cluster.getSize()));
            int width = mDimension;
            int height = mDimension;

            for (Person p : cluster.getItems()) {


                // Draw 4 at most.
                //if (profilePhotos.size() == 4) break;
                //Drawable drawable = getResources().getDrawable(p.profilePhoto);
                Drawable drawable = p.profilePhoto;
                drawable.setBounds(0, 0, width, height);
                profilePhotos.add(drawable);
            }
            MultiDrawable multiDrawable = new MultiDrawable(profilePhotos);
            multiDrawable.setBounds(0, 0, width, height);

            mClusterImageView.setImageDrawable(multiDrawable);
            Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster cluster) {
            // Always render clusters.
            return cluster.getSize() > 1;
        }
    }

    @Override
    public boolean onClusterClick(Cluster<Person> cluster) {
        // Show a toast with some info when the cluster is clicked.
        String firstName = cluster.getItems().iterator().next().name;
        //Toast.makeText(this, cluster.getSize() + " (including " + firstName + ")", Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, "one", Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public void onClusterInfoWindowClick(Cluster<Person> cluster) {
        // Does nothing, but you could go to a list of the users.
        //Toast.makeText(this, "two", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onClusterItemClick(Person item) {
        // Does nothing, but you could go into the user's profile page, for example.
        //Toast.makeText(this, "three", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onClusterItemInfoWindowClick(Person item) {
        // Does nothing, but you could go into the user's profile page, for example.
        //Toast.makeText(this, "four", Toast.LENGTH_SHORT).show();
    }




    @Override
    protected void startDemo() {

        //Account stuff


        LatLng mylocation= new LatLng(-1.2002, 36.7801);

        Bundle bundle = getIntent().getExtras();

/*

        if(bundle.getBoolean("haslocation")){
            mylat = bundle.getDouble("LATX");
            mylong = bundle.getDouble("LONGY");
            mylocation = new LatLng(mylat, mylong);

        }
        */

        mydisplayname = bundle.getString("userdisplayname");
        mylat = bundle.getDouble("mylat");
        mylong = bundle.getDouble("mylong");
        myuseremail = bundle.getString("usermail");
        myphoto = bundle.getString("urlphoto");

        Log.v("start demo ", "displayname " + mydisplayname + " email " + myuseremail +
                " location " + mylat +" " + mylong + " photo " + myphoto);

        mylocation = new LatLng(mylat, mylong);

        getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(mylocation, 10));

        mClusterManager = new ClusterManager<Person>(this, getMap());
        mClusterManager.setRenderer(new PersonRenderer());
        getMap().setOnCameraChangeListener(mClusterManager);
        getMap().setOnMarkerClickListener(mClusterManager);
        getMap().setOnInfoWindowClickListener(mClusterManager);
        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterInfoWindowClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.setOnClusterItemInfoWindowClickListener(this);

        //peopleList = bundle.getStringArrayList("THEDETAILS");
        //hashMap = (HashMap<String, String>)bundle.get("THEPEOPLE");
        //accountname = bundle.getString("loggeduser");



        settings = getSharedPreferences("xboxme", 0);
        credential =
                GoogleAccountCredential.usingAudience(this,"server:client_id:"+Constants.WEB_CLIENT_ID);

        setAccountName(settings.getString("ACCOUNT_NAME", null));
        if (credential.getSelectedAccountName() != null) {
            //detailIntent.putExtra("credential",accountCredentials);

            // Already signed in, begin app!
            //Toast.makeText(getBaseContext(), "Logged in with : " + credential.getSelectedAccountName(), Toast.LENGTH_SHORT).show();
            //Toast.makeText(getBaseContext(), GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext()),Toast.LENGTH_SHORT).show();
        } else {
            // Not signed in, show login window or request an account.
            //chooseAccount();
            setAccountName(myuseremail);
        }

        //Log.v("custom marker ", //credential.getSelectedAccountName());


        goQueryDatastore( mClusterManager);

    }

    private void goQueryDatastore(final ClusterManager<Person> mClusterManager){

        final ClusterManager<Person> mClusterManager1 = mClusterManager;

        //plusLocations.clear();
        //plusPeople.clear();
        //plusUrl.clear();
        //plusTime.clear();


        QueryPlusCallBack queryPlusCallBack = new QueryPlusCallBack() {
            @Override
            public void querycomplete(List<GamersLocation> gamersLocationList) {

                /*

                if(gamersLocationList.size() > 0) {

                    for(GamersLocation gamersLocation : gamersLocationList) {

                        String useremails = gamersLocation.getEmail();
                        String userdisplaynames = gamersLocation.getDisplayname();
                        String usergamertags = gamersLocation.getGamertag();
                        double userlat = gamersLocation.getLatx();
                        double userlong = gamersLocation.getLongy();
                        DateTime usertimes = gamersLocation.getTxntime();
                        String userphotos = gamersLocation.getPhotourl();

                        //mylatx = locationPlus.getLatx();
                        //mylongy = locationPlus.getLongy();
                        myaccount = locationPlus.getDisplayname();
                        urldisplay = hashMap.get(myaccount);

                        plusTime.add(usertimes);

                        //Log.v("readlocations try ", "Name " + myaccount + " Lat " + mylatx + " Lng " + mylongy + " image " + urldisplay);

                        position = new LatLng(userlat,userlong);



                        plusLocations.add(position);
                        plusPeople.add(myaccount);
                        if(urldisplay != null){
                            plusUrl.add(urldisplay);
                        }
                        else {
                            plusUrl.add("there is no image");
                        }

                    }
                    //Log.v("goquerystore ", "outside the for loop just before the mClustermanager.cluster()");
                    //mClusterManager1.cluster();
                }
                else{
                    // Toast.makeText(getApplicationContext(), "No location found", Toast.LENGTH_LONG).show();

                }
                */

                customizeMarkers(gamersLocationList,mClusterManager1);

            }


        };

        //Toast.makeText(getApplicationContext(), "This will take a few minutes.....", Toast.LENGTH_SHORT).show();

        QueryGamers queryGamers = new QueryGamers(10, queryPlusCallBack, credential);
        queryGamers.execute();

    }

    private void customizeMarkers(final List<GamersLocation> gamersLocationList,
                                  final ClusterManager<Person> mClusterManager){

        final ClusterManager<Person> mClusterManager2 = mClusterManager;

        CallBackImage callBackImage = new CallBackImage() {
            @Override
            public void querycomplete(ArrayList<Drawable> plusImages) {

                int i = 0;

               // TimeZone timeZone = TimeZone.getDefault();

                for(Drawable d : plusImages){

                    GamersLocation nowgamer = gamersLocationList.get(i);

                    double userlat = nowgamer.getLatx();
                    double userlong = nowgamer.getLongy();

                    position = new LatLng(userlat,userlong);

                    String usergamertags = nowgamer.getGamertag();
                    DateTime usertimes = nowgamer.getTxntime();


                    Person p = new Person(position, usergamertags, d, usertimes);
                    //Log.v("customize markers " ,plusTime.get(i).toString());

                    mClusterManager2.addItem(p);
                    i++;
                }
                mClusterManager2.cluster();
            }
        };

        ImagePlusDownload imagePlusDownload = new ImagePlusDownload(gamersLocationList, callBackImage, getApplicationContext());
        imagePlusDownload.execute();

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





/**
 * Stores activity data in the Bundle.
 */

/*

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);
        //outState.putInt(SAVED_PROGRESS, mSignInProgress);
        //outState.putParcelable(KEY_LOCATION, mLastLocation);
        Log.v("onsaveinstancestate ", "location "+ plusLocations.size() +" urls "+plusUrl.size()+" people "+plusPeople.size());
        if((plusLocations != null)&&(plusUrl != null)&&(plusPeople != null)) {
            outState.putStringArrayList(KEY_PEOPLE, plusPeople);
            outState.putStringArrayList(KEY_URL, plusUrl);
            outState.putParcelableArrayList(KEY_LOCATION, plusLocations);
            super.onSaveInstanceState(outState);
        }
    }
*/
}
