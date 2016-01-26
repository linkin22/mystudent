package com.ramogi.xboxme;

import android.os.AsyncTask;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.ramogi.xbox.backend.messaging.Messaging;
import com.ramogi.xbox.backend.registration.Registration;

import java.io.IOException;

/**
 * Created by ROchola on 1/7/2016.
 */
public class SendPlus extends AsyncTask<Void, Void, String> {
    private String emailto;
    private String emailfrom;
    private String message;
    private String regid;
    private static Messaging msgService = null;
    private SendPlusCallBack sendPlusCallBack;
    private String from,to;

    public SendPlus( String from, String to, String message, String regid, SendPlusCallBack sendPlusCallBack){

        //this.emailfrom = emailfrom;
        //this.emailto = emailto;
        this.from = from;
        this.to = to;
        this.message = message;
        this.regid = regid;
        this.sendPlusCallBack = sendPlusCallBack;

    }

    @Override
    protected String doInBackground(Void... params) {
        String msg = "";


        if (msgService == null) {
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

            msgService = builder.build();

            try {
                msgService.sendone(message, regid, from, to);

                //msgService.messagingEndpoint().sendOneMessage(message, regid, from, to);
                msg = "message sent";
            }
            catch (IOException io){
                msg = "message not sent";

            }


        }
        return msg;
    }

    @Override
    protected void onPostExecute(String msg) {
        // Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        // Logger.getLogger("REGISTRATION").log(Level.INFO, msg);

        sendPlusCallBack.querycomplete(msg);
    }


}
