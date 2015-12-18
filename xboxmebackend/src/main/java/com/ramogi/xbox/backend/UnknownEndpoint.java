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
        name = "unknownApi",
        version = "v1",
        resource = "unknown",
        namespace = @ApiNamespace(
                ownerDomain = "backend.xbox.ramogi.com",
                ownerName = "backend.xbox.ramogi.com",
                packagePath = ""
        )
)
public class UnknownEndpoint {

    private static final Logger logger = Logger.getLogger(UnknownEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;

    static {
        // Typically you would register this inside an OfyServive wrapper. See: https://code.google.com/p/objectify-appengine/wiki/BestPractices
        ObjectifyService.register(Unknown.class);
    }

    /**
     * Returns the {@link Unknown} with the corresponding ID.
     *
     * @param email the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code Unknown} with the provided ID.
     */
    @ApiMethod(
            name = "get",
            path = "unknown/{email}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public Unknown get(@Named("email") String email) throws NotFoundException {
        logger.info("Getting Unknown with ID: " + email);
        Unknown unknown = ofy().load().type(Unknown.class).id(email).now();
        if (unknown == null) {
            throw new NotFoundException("Could not find Unknown with ID: " + email);
        }
        return unknown;
    }

    /**
     * Inserts a new {@code Unknown}.
     */
    @ApiMethod(
            name = "insert",
            path = "unknown",
            httpMethod = ApiMethod.HttpMethod.POST)
    public Unknown insert(Unknown unknown) {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that unknown.email has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        ofy().save().entity(unknown).now();
        logger.info("Created Unknown with ID: " + unknown.getEmail());

        return ofy().load().entity(unknown).now();
    }

    /**
     * Updates an existing {@code Unknown}.
     *
     * @param email   the ID of the entity to be updated
     * @param unknown the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code email} does not correspond to an existing
     *                           {@code Unknown}
     */
    @ApiMethod(
            name = "update",
            path = "unknown/{email}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public Unknown update(@Named("email") String email, Unknown unknown) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        checkExists(email);
        ofy().save().entity(unknown).now();
        logger.info("Updated Unknown: " + unknown);
        return ofy().load().entity(unknown).now();
    }

    /**
     * Deletes the specified {@code Unknown}.
     *
     * @param email the ID of the entity to delete
     * @throws NotFoundException if the {@code email} does not correspond to an existing
     *                           {@code Unknown}
     */
    @ApiMethod(
            name = "remove",
            path = "unknown/{email}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("email") String email) throws NotFoundException {
        checkExists(email);
        ofy().delete().type(Unknown.class).id(email).now();
        logger.info("Deleted Unknown with ID: " + email);
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
            path = "unknown",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<Unknown> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<Unknown> query = ofy().load().type(Unknown.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<Unknown> queryIterator = query.iterator();
        List<Unknown> unknownList = new ArrayList<Unknown>(limit);
        while (queryIterator.hasNext()) {
            unknownList.add(queryIterator.next());
        }
        return CollectionResponse.<Unknown>builder().setItems(unknownList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }

    private void checkExists(String email) throws NotFoundException {
        try {
            ofy().load().type(Unknown.class).id(email).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find Unknown with ID: " + email);
        }
    }
}