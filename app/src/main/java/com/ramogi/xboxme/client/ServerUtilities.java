/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ramogi.xboxme.client;

import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.google.api.client.util.DateTime;
import com.ramogi.xbox.backend.contactplusApi.ContactplusApi;
import com.ramogi.xbox.backend.contactplusApi.model.Contactplus;
import com.ramogi.xbox.backend.messaging.Messaging;
import com.ramogi.xbox.backend.messaging.Messaging.MessagingEndpoint;
import com.ramogi.xboxme.Constants;

import java.io.IOException;
import java.util.ArrayList;
//import java.util.Random;


/**
 * Helper class used to communicate with the demo com.approx.messenger.com.approx.messenger.server.
 */
public final class ServerUtilities {
	
	private static final String TAG = "ServerUtilities";


    //private static final int MAX_ATTEMPTS = 5;
    //private static final int BACKOFF_MILLI_SECONDS = 2000;
    //private static final Random random = new Random();

    //private String message;
    //private String regid;
    //private static MessagingEndpoint msgEndpoint = null;
    private static Messaging msgSendService = null;
    private static ContactplusApi myRegisterApiService = null;

    //private String from,to;

    /**
     * Register this account/device pair within the com.approx.messenger.com.approx.messenger.server.
     */
    public static void register(String email, String regId) {
        Log.i(TAG, "registering device (regId = " + regId + ")");

        Contactplus contactplus = new Contactplus();
        contactplus.setEmail(email);
        contactplus.setRegId(regId);
        contactplus.setRegisteredDate(new DateTime(new java.util.Date()));

        Log.v(" registertime ",contactplus.getRegisteredDate().toString());

        if (myRegisterApiService == null) {
            ContactplusApi.Builder builder = new ContactplusApi.Builder(
                    AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(),null)
                    .setRootUrl("https://" + Constants.PROJECT_ID + ".appspot.com/_ah/api/")
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });

            myRegisterApiService = builder.build();
        }

        try {

            myRegisterApiService.insert(contactplus).execute();

            //added = contactplus.getEmail()+" added";

        } catch (IOException e) {

            //added = contactplus.getEmail()+" not added";
        }
    }

    /**
     * Unregister this account/device pair within the com.approx.messenger.com.approx.messenger.server.
     */
    public static void unregister( String email) {

        if (myRegisterApiService == null) {
            ContactplusApi.Builder builder = new ContactplusApi.Builder(
                    AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl("https://" + Constants.PROJECT_ID + ".appspot.com/_ah/api/")
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });

            myRegisterApiService = builder.build();
        }

        try {

            myRegisterApiService.remove(email).execute();

            //added = email + " removed";

        } catch (IOException e) {

            //added = email = " not removed";
        }

    }
    
    /**
     * Send a message.
     */
    public static void send( String msg, String to,  String from ) throws IOException {

            if (myRegisterApiService == null) { // Only do this once
                ContactplusApi.Builder builder = new ContactplusApi.Builder(
                        AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                        // options for running against local devappserver
                        // - 10.0.2.2 is localhost's IP address in Android emulator
                        // - turn off compression when running against local devappserver
                        .setRootUrl("https://" + Constants.PROJECT_ID + ".appspot.com/_ah/api/")
                        .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                            @Override
                            public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                                abstractGoogleClientRequest.setDisableGZipContent(true);
                            }
                        });
                // end options for devappserver
                myRegisterApiService = builder.build();
            }

            Contactplus contactplus  = myRegisterApiService.get(to).execute();
            //Log.v(TAG,"email "+contactplus.getEmail()+" send reg id"+contactplus.getRegId());

        Log.v(TAG, " send() from " + from + " to " + to + " message " + msg + " reg id " + contactplus.getRegId());

        final ArrayList<String> params = new ArrayList<String>(4);

        params.add(0,msg);
        params.add(1,contactplus.getRegId());
        params.add(2,from);
        params.add(3,to);

        try{

            Log.v(TAG," inside try");
            Log.v(TAG, " send() from " + from + " to " + to + " message " + msg + " reg id " + contactplus.getRegId());
            Log.v(TAG, "printing params "+params.get(0)+" / "+params.get(1)+" / "+params.get(2)+" / "+params.get(3));

        //myRegisterApiService.sendone(msg, contactplus.getRegId(), from, to).execute();

            myRegisterApiService.sendone(params).execute();

        }
        catch (IOException io){
            //msg = "message not sent";
            Log.v(TAG, "i've been caught");

        }

        Log.v(TAG, "after try n catch");


        Log.v(TAG, " send() from " + from + " to " + to + " message " + msg + " reg id " + contactplus.getRegId());

/*
            if (msgSendService == null) {

                Messaging.Builder builder = new Messaging.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), null)
                        // Need setRootUrl and setGoogleClientRequestInitializer only for local testing,
                        // otherwise they can be skipped
                        .setRootUrl("https://" + Constants.PROJECT_ID + ".appspot.com/_ah/api/")
                        .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                            @Override
                            public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest)
                                    throws IOException {
                                abstractGoogleClientRequest.setDisableGZipContent(true);
                            }
                        });
                // end of optional local run code

                msgSendService = builder.build();

            }

                try {

                    //MessagingEndpoint msgEndpoint = new MessagingEndpoint();
                    //Messaging.MessagingEndpoint.sendOneMessage(msg, contactplus.getRegId(), from, to);
                    //msgSendService.MessagingEndpoint.SendOneMessage
                    //msgSendService.messagingEndpoint().sendOneMessage()



                    Log.v(TAG, " send() from " + from + " to " + to + " message " + msg + " reg id " + contactplus.getRegId());
                   //msgSendService.MessagingEndpoint.SendOneMessage(msg, contactplus.getRegId(), from, to);

                    msgSendService.sendone(msg, contactplus.getRegId(), from, to);
                   // msgSendService.

                    //msgSendService.messagingEndpoint().sendOneMessage(msg, contactplus.getRegId(), from, to);
                    //msgSendService.messagingEndpoint().sendMessage(msg);
                    //msgSendService.MessagingEndpoint
                    //com.ramogi.xbox.backend.messaging.Messaging.MessagingEndpoint.SendOneMessage(msg, contactplus.getRegId(), from, to);.
                    //SendOneMessage(msg, contactplus.getRegId(), from, to);
                    //msg = "message sent";
                }
                catch (IOException io){
                    //msg = "message not sent";

                }

                */




       /* }catch(IOException e){
            //Log.v("builder/do background", e.toString());
        }
*/



    }


}
