/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Backend with Google Cloud Messaging" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/GcmEndpoints
*/

package com.ramogi.xbox.backend;

import com.google.android.gcm.server.Constants;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Named;

import static com.ramogi.xbox.backend.OfyService.ofy;

/**
 * An endpoint to send messages to devices registered with the backend
 * <p/>
 * For more information, see
 * https://developers.google.com/appengine/docs/java/endpoints/
 * <p/>
 * NOTE: This endpoint does not use any form of authorization or
 * authentication! If this app is deployed, anyone can access this endpoint! If
 * you'd like to add authentication, take a look at the documentation.
 */
@Api(
        name = "messaging",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "backend.xbox.ramogi.com",
                ownerName = "backend.xbox.ramogi.com",
                packagePath = ""
        )
)
public class MessagingEndpoint {
    private static final Logger log = Logger.getLogger(MessagingEndpoint.class.getName());

    /**
     * Api Keys can be obtained from the google cloud console
     */
    private static final String API_KEY = System.getProperty("gcm.api.key");

    /**
     * Send to the first 10 devices (You can modify this to send to any number of devices or a specific device)
     *
     * @param message The message to send
     */
    public void sendMessage(@Named("message") String message) throws IOException {
        if (message == null || message.trim().length() == 0) {
            log.warning("Not sending message because it is empty");
            return;
        }
        // crop longer messages
        if (message.length() > 1000) {
            message = message.substring(0, 1000) + "[...]";
        }
        Sender sender = new Sender(API_KEY);
        Message msg = new Message.Builder().addData("message", message).build();
        List<RegistrationRecord> records = ofy().load().type(RegistrationRecord.class).limit(10).list();
        for (RegistrationRecord record : records) {
            Result result = sender.send(msg, record.getRegId(), 5);
            if (result.getMessageId() != null) {
                log.info("Message sent to " + record.getRegId());
                String canonicalRegId = result.getCanonicalRegistrationId();
                if (canonicalRegId != null) {
                    // if the regId changed, we have to update the datastore
                    log.info("Registration Id changed for " + record.getRegId() + " updating to " + canonicalRegId);
                    record.setRegId(canonicalRegId);
                    ofy().save().entity(record).now();
                }
            } else {
                String error = result.getErrorCodeName();
                if (error.equals(Constants.ERROR_NOT_REGISTERED)) {
                    log.warning("Registration Id " + record.getRegId() + " no longer registered with GCM, removing from datastore");
                    // if the device is no longer registered with Gcm, remove it from the datastore
                    ofy().delete().entity(record).now();
                } else {
                    log.warning("Error when sending message : " + error);
                }
            }
        }
    }

    /**
     *  @param regid The Google Cloud Messaging registration Id to add
     *  @param message The Google Cloud Messaging registration Id to add
     *  @param from The Google Cloud Messaging registration Id to add
     *  @param to The Google Cloud Messaging registration Id to add
     */
    @ApiMethod(
            name = "sendone",
            path = "sendone",
            httpMethod = ApiMethod.HttpMethod.POST)

    public void sendOneMessage(@Named("message") String message,@Named("regid")String regid,
                               @Named("from") String from, @Named("to") String to) throws IOException {

        //log.warning("sendOneMessage called");
        log.warning(" send() from " + from + " to " + to + " message " + message + " reg id " +regid);

        if (message == null || message.trim().length() == 0) {
            log.warning("Not sending message because it is empty");
            return;
        }
        // crop longer messages
        if (message.length() > 1000) {
            message = message.substring(0, 1000) + "[...]";
        }
        Sender sender = new Sender(API_KEY);
        Message msg = new Message.Builder()
//			.delayWhileIdle(true)
                .addData(com.ramogi.xbox.backend.Constants.TO, to)
                .addData(com.ramogi.xbox.backend.Constants.FROM, from)
                .addData(com.ramogi.xbox.backend.Constants.MSG, message)
                .build();

        try{

        Result result = sender.send(msg,regid,5);

            Contactplus contactplus = new Contactplus();
            contactplus.setRegId(regid);
            contactplus.setEmail(from);
        if (result.getMessageId() != null) {
            log.info("Message sent to " + regid);
            String canonicalRegId = result.getCanonicalRegistrationId();
            if (canonicalRegId != null) {
                // if the regId changed, we have to update the datastore
                //log.info("Registration Id changed for " + regid + " updating to " + canonicalRegId);
                //record.setRegId(canonicalRegId);
               // ofy().save().entity(record).now();
                contactplus.setRegId(canonicalRegId);
                ofy().save().entity(contactplus).now();

            }
        } else {
            String error = result.getErrorCodeName();
            if (error.equals(Constants.ERROR_NOT_REGISTERED)) {
                log.warning("Registration Id " + regid + " no longer registered with GCM, removing from datastore");
               //if the device is no longer registered with Gcm, remove it from the datastore
                //ofy().delete().entity(record).now();
                ofy().delete().entity(contactplus).now();
            } else {
                log.warning("Error when sending message : " + error);
            }
        }

            log.info("Result: " + result.toString());
        } catch (IOException e) {
            log.warning( e.getMessage());
        }

    }
}
