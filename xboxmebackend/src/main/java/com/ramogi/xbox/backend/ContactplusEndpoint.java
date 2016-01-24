package com.ramogi.xbox.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;

import com.google.android.gcm.server.Constants;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Nullable;
import javax.inject.Named;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * WARNING: This generated code is intended as a sample or starting point for using a
 * Google Cloud Endpoints RESTful API with an Objectify entity. It provides no data access
 * restrictions and no data validation.
 * <p/>
 * DO NOT deploy this code unchanged as part of a real application to real users.
 */
@Api(
        name = "contactplusApi",
        version = "v1",
        resource = "contactplus",
        namespace = @ApiNamespace(
                ownerDomain = "backend.xbox.ramogi.com",
                ownerName = "backend.xbox.ramogi.com",
                packagePath = ""
        )
)
public class ContactplusEndpoint {

    private static final Logger logger = Logger.getLogger(ContactplusEndpoint.class.getName());

    //private static final Logger log = Logger.getLogger(MessagingEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;

    private static final String API_KEY = System.getProperty("gcm.api.key");

    static {
        // Typically you would register this inside an OfyServive wrapper. See: https://code.google.com/p/objectify-appengine/wiki/BestPractices
        ObjectifyService.register(Contactplus.class);
    }

    /**
     * Returns the {@link Contactplus} with the corresponding ID.
     *
     * @param email the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code Contactplus} with the provided ID.
     */
    @ApiMethod(
            name = "get",
            path = "contactplus/{email}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public Contactplus get(@Named("email") String email) throws NotFoundException {
        logger.info("Getting Contactplus with ID: " + email);
        Contactplus contactplus = ofy().load().type(Contactplus.class).id(email).now();
        if (contactplus == null) {
            throw new NotFoundException("Could not find Contactplus with ID: " + email);
        }
        return contactplus;
    }

    /**
     * Inserts a new {@code Contactplus}.
     */
    @ApiMethod(
            name = "insert",
            path = "contactplus",
            httpMethod = ApiMethod.HttpMethod.POST)
    public Contactplus insert(Contactplus contactplus) {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that contactplus.email has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        ofy().save().entity(contactplus).now();
        logger.info("Created Contactplus with ID: " + contactplus.getEmail());

        return ofy().load().entity(contactplus).now();
    }

    /**
     * Updates an existing {@code Contactplus}.
     *
     * @param email       the ID of the entity to be updated
     * @param contactplus the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code email} does not correspond to an existing
     *                           {@code Contactplus}
     */
    @ApiMethod(
            name = "update",
            path = "contactplus/{email}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public Contactplus update(@Named("email") String email, Contactplus contactplus) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        checkExists(email);
        ofy().save().entity(contactplus).now();
        logger.info("Updated Contactplus: " + contactplus);
        return ofy().load().entity(contactplus).now();
    }

    /**
     * Deletes the specified {@code Contactplus}.
     *
     * @param email the ID of the entity to delete
     * @throws NotFoundException if the {@code email} does not correspond to an existing
     *                           {@code Contactplus}
     */
    @ApiMethod(
            name = "remove",
            path = "contactplus/{email}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("email") String email) throws NotFoundException {
        checkExists(email);
        ofy().delete().type(Contactplus.class).id(email).now();
        logger.info("Deleted Contactplus with ID: " + email);
    }

    /**
     * List all entities.
     *
     * @param cursor used for pagination to determine which page to return
     * @param limit  the maximum number of entries to return
     * @return a response that encapsulates the result list and the next page token/cursor
     */
    @ApiMethod(
            name = "list",
            path = "contactplus",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<Contactplus> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<Contactplus> query = ofy().load().type(Contactplus.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<Contactplus> queryIterator = query.iterator();
        List<Contactplus> contactplusList = new ArrayList<Contactplus>(limit);
        while (queryIterator.hasNext()) {
            contactplusList.add(queryIterator.next());
        }
        return CollectionResponse.<Contactplus>builder().setItems(contactplusList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }



    private void checkExists(String email) throws NotFoundException {
        try {
            ofy().load().type(Contactplus.class).id(email).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find Contactplus with ID: " + email);
        }
    }
}