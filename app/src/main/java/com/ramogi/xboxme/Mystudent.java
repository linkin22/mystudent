package com.ramogi.xboxme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.ramogi.xbox.backend.gamersLocationApi.model.GamersLocation;

//import android.net.Uri;


/**
 * Activity to demonstrate basic retrieval of the Google user's ID, email address, and basic
 * profile.
 */
public class Mystudent extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    private GoogleApiClient mGoogleApiClient;
   private TextView mStatusTextView, mDetailsTextView;
        //    mGamerTagTextView, mLatxTextView, mLongyTextView;
    private ProgressDialog mProgressDialog;
    private Location mLastLocation, mSavedLocation;
    private double mylat,mylong;
    private static final String KEY_LOCATION = "location";
    private String currentUserEmail;
    private String CurrentUserDisplayName;
    private String myphoto;
    private GoogleAccountCredential credential;
    //private InsertPlus insertPlus;
    private GamersLocation gamersLocation;
   // private EditText mGamerTagInput;
    private Intent detailIntent;
    //private Uri myPhotoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);

        // Views
        mStatusTextView = (TextView) findViewById(R.id.status);
        mDetailsTextView = (TextView) findViewById(R.id.detail);
        //mGamerTagTextView = (TextView) findViewById(R.id.gamertagTextView);
       // mLatxTextView = (TextView) findViewById(R.id.latxTextView);
        //mLongyTextView = (TextView) findViewById(R.id.longyTextView);
       // mGamerTagInput = (EditText) findViewById(R.id.gamerTagInput);


        // Button listeners
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);
        findViewById(R.id.disconnect_button).setOnClickListener(this);
       // findViewById(R.id.locationbtn).setOnClickListener(this);

        new GcmRegistrationAsyncTask(this).execute();

        gamersLocation = new GamersLocation();
        detailIntent = new Intent(getApplicationContext(), CustomMarker.class);

        // [START configure_signin]
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // [END configure_signin]

        // [START build_client]
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        // [END build_client]
        //.addApi(Plus.API, Plus.PlusOptions.builder().build())

        if (savedInstanceState != null) {

            // Update the value of mLastLocation from the Bundle and update the UI to show the
            // correct latitude and longitude.
            if (savedInstanceState.keySet().contains(KEY_LOCATION)) {
                // Since KEY_LOCATION was found in the Bundle, we can be sure that mLastLocation
                // is not null.
                mSavedLocation = savedInstanceState.getParcelable(KEY_LOCATION);
                mLastLocation = mSavedLocation;
            }
        }

        // [START customize_button]
        // Customize sign-in button. The sign-in button can be displayed in
        // multiple sizes and color schemes. It can also be contextually
        // rendered based on the requested scopes. For example. a red button may
        // be displayed when Google+ scopes are requested, but a white button
        // may be displayed when only basic profile is requested. Try adding the
        // Scopes.PLUS_LOGIN scope to the GoogleSignInOptions to see the
        // difference.
        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setScopes(gso.getScopeArray());
        // [END customize_button]
    }

    @Override
    public void onStart() {
        super.onStart();

        //mGoogleApiClient.connect();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            if(mLastLocation == null){
                Log.d(TAG, "mlastlocation is null");

            }
            if(mSavedLocation == null){
                Log.d(TAG, "mSavedlocation is null");

            }
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    // [START onActivityResult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }
    // [END onActivityResult]

    // [START handleSignInResult]
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {

            if(mGoogleApiClient.isConnected()){
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                        mGoogleApiClient);

            }
            else {
                mGoogleApiClient.connect();

                Log.d("handle signin result ", "google client connecting");

                if(mGoogleApiClient.isConnecting()){
                    Log.d("handle signin result ", "google client still connecting");

                }
                else {
                    Log.d("handle signin result ", "google client has connected");

                    mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                            mGoogleApiClient);

                }

            }

            if(mLastLocation == null){
                Log.d("handle signin result ", "mlastlocation is null");

            }
            if(mSavedLocation == null){
                Log.d("handle signin result ", "mSavedlocation is null");

            }

            if (mLastLocation != null) {
                mylat = mLastLocation.getLatitude();
                mylong = mLastLocation.getLongitude();

                //mLatxTextView.setText(""+mylat);
                //mLongyTextView.setText(""+mylong);
            }
            else {
                mLastLocation = mSavedLocation;

                if (mLastLocation != null) {
                    mylat = mLastLocation.getLatitude();
                    mylong = mLastLocation.getLongitude();

                    //mLatxTextView.setText(""+mylat);
                   // mLongyTextView.setText(""+mylong);
                }
            }

            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
            mDetailsTextView.setText(acct.getEmail());
            setCurrentUserEmail(acct.getEmail());
            setCurrentUserDisplayName(acct.getDisplayName());
            setMyPhoto(acct.getPhotoUrl().toString());
            //setMyPhotoUri(acct.getPhotoUrl());

            credential =
                    GoogleAccountCredential.usingAudience(this,"server:client_id:"+Constants.WEB_CLIENT_ID);
            setAccountName(acct.getEmail());
            //uploadLocation();
            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }
    // [END handleSignInResult]



    @Override
    public void onConnected(Bundle connectionHint) {
        // Reaching onConnected means we consider the user signed in.
        // Log.i("onConnected callback " ," onConnected");

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        if (mLastLocation != null) {
            mylat = mLastLocation.getLatitude();
            mylong = mLastLocation.getLongitude();

            //mLatxTextView.setText(""+mylat);
            //mLongyTextView.setText(""+mylong);
            //uploadLocation();
        }
        else {
            mLastLocation = mSavedLocation;

            if (mLastLocation != null) {
                mylat = mLastLocation.getLatitude();
                mylong = mLastLocation.getLongitude();

               // mLatxTextView.setText(""+mylat);
               // mLongyTextView.setText(""+mylong);
               // uploadLocation();
            }
        }
    }

    // [START signIn]
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signIn]

    // [START signOut]
    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END signOut]

    // [START revokeAccess]
    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END revokeAccess]

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private void updateUI(boolean signedIn) {
        if (signedIn) {

            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
//            findViewById(R.id.locationbtn).setEnabled(true);

            uploadLocation();

        } else {
            mStatusTextView.setText(R.string.signed_out);
            mDetailsTextView.setText(R.string.no_email);

           // mLatxTextView.setText("");
            //mLongyTextView.setText("");

            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
          //  findViewById(R.id.locationbtn).setEnabled(false);
        }
    }

    private void uploadLocation(){

        //Toast.makeText(this,"updatelocation()",Toast.LENGTH_LONG).show();

       // String myGamerTag = mGamerTagInput.getText().toString().trim();

        Log.i("upload location ", " " + getCurrentUserDisplayName() + " " + getCurrentUserEmail() +
                " " + mylat + " " + mylong + " " + getMyPhoto());

        gamersLocation.setEmail(getCurrentUserEmail());
        gamersLocation.setDisplayname(getCurrentUserDisplayName());
        gamersLocation.setLatx(mylat);
        gamersLocation.setLongy(mylong);
        gamersLocation.setPhotoPath(getMyPhoto());
            //gamersLocation.setPhotoURI(myPhotoURI);

        //if(!(myGamerTag.isEmpty())) {
            //gamersLocation.setGamertag(myGamerTag);
       /// }
       // else {
            gamersLocation.setGamertag("Anonymous");
       // }
        Log.v("upload location ", gamersLocation.getPhotoPath());

        InsertPlus insertPlus = new InsertPlus(gamersLocation,getApplicationContext(),getCredential());
        insertPlus.execute();

        //Toast.makeText(this,"updated",Toast.LENGTH_short).show();

        detailIntent.putExtra("userdisplayname", getCurrentUserDisplayName());
        detailIntent.putExtra("mylat",mylat);
        detailIntent.putExtra("mylong",mylong);
        detailIntent.putExtra("usermail", getCurrentUserEmail());
        detailIntent.putExtra("urlphoto", getMyPhoto());
        startActivity(detailIntent);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.sign_out_button:
                signOut();
                break;
            case R.id.disconnect_button:
                revokeAccess();
                break;
            case R.id.locationbtn:
                //Toast.makeText(this,"updated", Toast.LENGTH_SHORT).show();
                uploadLocation();
                break;
        }
    }

    /**
     * Stores activity data in the Bundle.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // super.onSaveInstanceState(outState);
        //outState.putInt(SAVED_PROGRESS, mSignInProgress);
        if(mLastLocation != null) {
            outState.putParcelable(KEY_LOCATION, mLastLocation);
        }

        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onStop() {
        super.onStop();

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }
    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason.
        // We call connect() to attempt to re-establish the connection or get a
        // ConnectionResult that we can attempt to resolve.
        mGoogleApiClient.connect();
    }

    // setAccountName definition
    private void setAccountName(String accountName) {

        credential.setSelectedAccountName(accountName);
        //accountCredentials.add(credential);

        setCredential(credential);

    }

    public String getCurrentUserDisplayName() {
        return CurrentUserDisplayName;
    }

    public void setCurrentUserDisplayName(String currentUserDisplayName) {
        CurrentUserDisplayName = currentUserDisplayName;
    }

    public String getCurrentUserEmail() {
        return currentUserEmail;
    }

    public void setCurrentUserEmail(String currentUserEmail) {
        this.currentUserEmail = currentUserEmail;
    }

    public GoogleAccountCredential getCredential() {
        return credential;
    }

    public void setCredential(GoogleAccountCredential credential) {
        this.credential = credential;
    }

    public String getMyPhoto() {
        return myphoto;
    }

    public void setMyPhoto(String myphoto) {
        this.myphoto = myphoto;
    }

    //public Uri getMyPhotoUri() { return myPhotoUri;  }

    //public void setMyPhotoUri(Uri myPhotoUri) { this.myPhotoUri = myPhotoUri;  }

}
