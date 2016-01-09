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

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.ramogi.xbox.backend.contactplusApi.model.Contactplus;
import com.ramogi.xboxme.InsertContact;
import com.ramogi.xboxme.InsertContactCallback;
import com.ramogi.xboxme.QueryOneContact;
import com.ramogi.xboxme.QueryOneContactCallback;
import com.ramogi.xboxme.RemoveContact;
import com.ramogi.xboxme.SendPlus;
import com.ramogi.xboxme.SendPlusCallBack;

import java.io.IOException;
import java.util.Random;


/**
 * Helper class used to communicate with the demo com.approx.messenger.com.approx.messenger.server.
 */
public final class ServerUtilities {
	
	private static final String TAG = "ServerUtilities";


    private static final int MAX_ATTEMPTS = 5;
    private static final int BACKOFF_MILLI_SECONDS = 2000;
    private static final Random random = new Random();

    /**
     * Register this account/device pair within the com.approx.messenger.com.approx.messenger.server.
     */
    public static void register(final String email, final String regId) {
        //Log.i(TAG, "registering device (regId = " + regId + ")");
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

        Contactplus contactplus = new Contactplus();
        contactplus.setEmail(email);
        contactplus.setRegId(regId);

        InsertContactCallback insertContactCallback = new InsertContactCallback() {
            @Override
            public void querycomplete(String result) {

                Log.v("Register plus ", result);
                //setResult(result);
            }
        };

        InsertContact insertContact = new InsertContact(contactplus, insertContactCallback);
        insertContact.execute();
    }

    /**
     * Unregister this account/device pair within the com.approx.messenger.com.approx.messenger.server.
     */
    public static void unregister(final String email,  final GoogleAccountCredential credential) {
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
        */

        InsertContactCallback insertContactCallback = new InsertContactCallback() {
            @Override
            public void querycomplete(String result) {

                Log.v("Unregister plus ", result);
            }
        };

        RemoveContact removeContact = new RemoveContact(email,insertContactCallback);
        removeContact.execute();
    }
    
    /**
     * Send a message.
     */
    public static void send(final String msg, String to) throws IOException {
        /*
        //Log.i(TAG, "sending message (msg = " + msg + ")");
        String serverUrl = Common.getServerUrl() + "/send";
        Map<String, String> params = new HashMap<String, String>();
        params.put(Common.MSG, msg);
        params.put(Common.FROM, Common.getPreferredEmail());
        params.put(Common.TO, to);        
        
        post(serverUrl, params, MAX_ATTEMPTS);
        */

        //final String regId;

        //final Contactplus contactplus1 = new Contactplus();

        QueryOneContactCallback queryOneContactCallback = new QueryOneContactCallback() {
            @Override
            public void querycomplete(Contactplus contactplus) {

                Log.v("query contact ", " "+contactplus.getRegId());
                Log.v("query contact ", " "+contactplus.getEmail());

                //contactplus1.setRegId(contactplus.getRegId());
                //contactplus1.setEmail(contactplus.getEmail());

                SendPlusCallBack sendPlusCallBack = new SendPlusCallBack() {
                    @Override
                    public void querycomplete(String result) {

                        Log.v("Server utilities ", " message sent "+result);

                    }
                };

                SendPlus sendPlus = new SendPlus(msg,contactplus.getRegId(),sendPlusCallBack);
                sendPlus.execute();

            }
        };

        QueryOneContact queryOneContact = new QueryOneContact(to,queryOneContactCallback);
        queryOneContact.execute();


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
