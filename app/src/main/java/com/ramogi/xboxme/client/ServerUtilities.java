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
        /*
        String serverUrl = Common.getServerUrl() + "/register";
        Map<String, String> params = new HashMap<String, String>();
        params.put(Common.FROM, email);
        params.put(Common.REG_ID, regId);
        // Once GCM returns a registration id, we need to register it in the
        // demo com.approx.messenger.com.approx.messenger.server. As the com.approx.messenger.com.approx.messenger.server might be down, we will retry it a couple
        // times.
        try {
        	post(serverUrl, params, MAX_ATTEMPTS);
        } catch (IOException e) {
        }

        //RegisterPlus registerPlus = new RegisterPlus(email,regId,credential);
        */

        //Date registerDate = new Date();
       // registerDate.getTime();
        //getRole().setCreated(new DateTime(new java.util.Date()));


        Contactplus contactplus = new Contactplus();
        contactplus.setEmail(email);
        contactplus.setRegId(regId);
        contactplus.setRegisteredDate(new DateTime(new java.util.Date()));

        Log.v(" registertime ",contactplus.getRegisteredDate().toString());


        /*

        InsertContactCallback insertContactCallback = new InsertContactCallback() {
            @Override
            public void querycomplete(String result) {

                Log.v("Register plus ", result);
                //setResult(result);
            }
        };

        InsertContact insertContact = new InsertContact(contactplus, insertContactCallback);
        insertContact.execute();
        */

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
        //Log.i(TAG, "unregistering device (email = " + email + ")");
        /*
        String serverUrl = Common.getServerUrl() + "/unregister";
        Map<String, String> params = new HashMap<String, String>();
        params.put(Common.FROM, email);
        try {
            post(serverUrl, params, MAX_ATTEMPTS);
        } catch (IOException e) {
            // At this point the device is unregistered from GCM, but still
            // registered in the com.approx.messenger.com.approx.messenger.server.
            // We could try to unregister again, but it is not necessary:
            // if the com.approx.messenger.com.approx.messenger.server tries to send a message to the device, it will get
            // a "NotRegistered" error message and should unregister the device.
        }


        InsertContactCallback insertContactCallback = new InsertContactCallback() {
            @Override
            public void querycomplete(String result) {

                Log.v("Unregister plus ", result);
            }
        };

        RemoveContact removeContact = new RemoveContact(email,insertContactCallback);
        removeContact.execute();
        */

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
            Log.v(TAG,"email "+contactplus.getEmail()+" send reg id"+contactplus.getRegId());


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
                   // msgSendService.

                    msgSendService.messagingEndpoint().sendOneMessage(msg, contactplus.getRegId(), from, to);
                    msgSendService.messagingEndpoint().sendMessage(msg);
                    //msgSendService.MessagingEndpoint
                    //com.ramogi.xbox.backend.messaging.Messaging.MessagingEndpoint.SendOneMessage(msg, contactplus.getRegId(), from, to);.
                    //SendOneMessage(msg, contactplus.getRegId(), from, to);
                    //msg = "message sent";
                }
                catch (IOException io){
                    //msg = "message not sent";

                }




       /* }catch(IOException e){
            //Log.v("builder/do background", e.toString());
        }
*/



    }

    /**
     * Issue a POST request to the com.approx.messenger.com.approx.messenger.server.
     *
     * @param endpoint POST address.
     * @param params request parameters.
     *
     * @throws IOException propagated from POST.
     */

    /*
    private static void post(String endpoint, Map<String, String> params) throws IOException {
        URL url;
        try {
            url = new URL(endpoint);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("invalid url: " + endpoint);
        }
        StringBuilder bodyBuilder = new StringBuilder();
        Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
        // constructs the POST body using the parameters
        while (iterator.hasNext()) {
            Entry<String, String> param = iterator.next();
            bodyBuilder.append(param.getKey()).append('=').append(param.getValue());
            if (iterator.hasNext()) {
                bodyBuilder.append('&');
            }
        }
        String body = bodyBuilder.toString();
        //Log.v(TAG, "Posting '" + body + "' to " + url);
        byte[] bytes = body.getBytes();
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setFixedLengthStreamingMode(bytes.length);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            // post the request
            OutputStream out = conn.getOutputStream();
            out.write(bytes);
            out.close();
            // handle the response
            int status = conn.getResponseCode();
            if (status != 200) {
              throw new IOException("Post failed with error code " + status);
            }
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
      }

      */
    
    /** Issue a POST with exponential backoff */

    /*
    private static void post(String endpoint, Map<String, String> params, int maxAttempts) throws IOException {
    	long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
    	for (int i = 1; i <= maxAttempts; i++) {
    		//Log.d(TAG, "Attempt #" + i);
    		try {
    			post(endpoint, params);
    			return;
    		} catch (IOException e) {
    			//Log.e(TAG, "Failed on attempt " + i + ":" + e);
    			if (i == maxAttempts) {
    				throw e;
                }
                try {
                    Thread.sleep(backoff);
                } catch (InterruptedException e1) {
                    Thread.currentThread().interrupt();
                    return;
                }
                backoff *= 2;    			
    		} catch (IllegalArgumentException e) {
    			throw new IOException(e.getMessage(), e);
    		}
    	}
    }
    */
}
