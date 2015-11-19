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
        name = "gamersLocationApi",
        version = "v1",
        resource = "gamersLocation",
        namespace = @ApiNamespace(
                ownerDomain = "backend.xbox.ramogi.com",
                ownerName = "backend.xbox.ramogi.com",
                packagePath = ""
        )
)
public class GamersLocationEndpoint {

    private static final Logger logger = Logger.getLogger(GamersLocationEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;

    static {
        // Typically you would register this inside an OfyServive wrapper. See: https://code.google.com/p/objectify-appengine/wiki/BestPractices
        ObjectifyService.register(GamersLocation.class);
    }

    /**
     * Returns the {@link GamersLocation} with the corresponding ID.
     *
     * @param email the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code GamersLocation} with the provided ID.
     */
    @ApiMethod(
            name = "get",
            path = "gamersLocation/{email}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public GamersLocation get(@Named("email") String email) throws NotFoundException {
        logger.info("Getting GamersLocation with ID: " + email);
        GamersLocation gamersLocation = ofy().load().type(GamersLocation.class).id(email).now();
        if (gamersLocation == null) {
            throw new NotFoundException("Could not find GamersLocation with ID: " + email);
        }
        return gamersLocation;
    }

    /**
     * Inserts a new {@code GamersLocation}.
     */
    @ApiMethod(
            name = "insert",
            path = "gamersLocation",
            httpMethod = ApiMethod.HttpMethod.POST)
    public GamersLocation insert(GamersLocation gamersLocation) {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that gamersLocation.email has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        ofy().save().entity(gamersLocation).now();
        logger.info("Created GamersLocation with ID: " + gamersLocation.getEmail());

        return ofy().load().entity(gamersLocation).now();
    }

    /**
     * Updates an existing {@code GamersLocation}.
     *
     * @param email          the ID of the entity to be updated
     * @param gamersLocation the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code email} does not correspond to an existing
     *                           {@code GamersLocation}
     */
    @ApiMethod(
            name = "update",
            path = "gamersLocation/{email}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public GamersLocation update(@Named("email") String email, GamersLocation gamersLocation) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        checkExists(email);
        ofy().save().entity(gamersLocation).now();
        logger.info("Updated GamersLocation: " + gamersLocation);
        return ofy().load().entity(gamersLocation).now();
    }

    /**
     * Deletes the specified {@code GamersLocation}.
     *
     * @param email the ID of the entity to delete
     * @throws NotFoundException if the {@code email} does not correspond to an existing
     *                           {@code GamersLocation}
     */
    @ApiMethod(
            name = "remove",
            path = "gamersLocation/{email}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("email") String email) throws NotFoundException {
        checkExists(email);
        ofy().delete().type(GamersLocation.class).id(email).now();
        logger.info("Deleted GamersLocation with ID: " + email);
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
            path = "gamersLocation",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<GamersLocation> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<GamersLocation> query = ofy().load().type(GamersLocation.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<GamersLocation> queryIterator = query.iterator();
        List<GamersLocation> gamersLocationList = new ArrayList<GamersLocation>(limit);
        while (queryIterator.hasNext()) {
            gamersLocationList.add(queryIterator.next());
        }
        return CollectionResponse.<GamersLocation>builder().setItems(gamersLocationList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }

    private void checkExists(String email) throws NotFoundException {
        try {
            ofy().load().type(GamersLocation.class).id(email).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find GamersLocation with ID: " + email);
        }
    }
}