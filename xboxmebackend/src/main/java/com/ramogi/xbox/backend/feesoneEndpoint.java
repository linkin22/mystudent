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
        name = "feesoneApi",
        version = "v1",
        resource = "feesone",
        namespace = @ApiNamespace(
                ownerDomain = "backend.xbox.ramogi.com",
                ownerName = "backend.xbox.ramogi.com",
                packagePath = ""
        )
)
public class feesoneEndpoint {

    private static final Logger logger = Logger.getLogger(feesoneEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;

    static {
        // Typically you would register this inside an OfyServive wrapper. See: https://code.google.com/p/objectify-appengine/wiki/BestPractices
        ObjectifyService.register(feesone.class);
    }

    /**
     * Returns the {@link feesone} with the corresponding ID.
     *
     * @param regno the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code feesone} with the provided ID.
     */
    @ApiMethod(
            name = "get",
            path = "feesone/{regno}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public feesone get(@Named("regno") String regno) throws NotFoundException {
        logger.info("Getting feesone with ID: " + regno);
        feesone feesone = ofy().load().type(feesone.class).id(regno).now();
        if (feesone == null) {
            throw new NotFoundException("Could not find feesone with ID: " + regno);
        }
        return feesone;
    }

    /**
     * Inserts a new {@code feesone}.
     */
    @ApiMethod(
            name = "insert",
            path = "feesone",
            httpMethod = ApiMethod.HttpMethod.POST)
    public feesone insert(feesone feesone) {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that feesone.regno has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        ofy().save().entity(feesone).now();
        logger.info("Created feesone with ID: " + feesone.getRegno());

        return ofy().load().entity(feesone).now();
    }

    /**
     * Updates an existing {@code feesone}.
     *
     * @param regno   the ID of the entity to be updated
     * @param feesone the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code regno} does not correspond to an existing
     *                           {@code feesone}
     */
    @ApiMethod(
            name = "update",
            path = "feesone/{regno}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public feesone update(@Named("regno") String regno, feesone feesone) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        checkExists(regno);
        ofy().save().entity(feesone).now();
        logger.info("Updated feesone: " + feesone);
        return ofy().load().entity(feesone).now();
    }

    /**
     * Deletes the specified {@code feesone}.
     *
     * @param regno the ID of the entity to delete
     * @throws NotFoundException if the {@code regno} does not correspond to an existing
     *                           {@code feesone}
     */
    @ApiMethod(
            name = "remove",
            path = "feesone/{regno}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("regno") String regno) throws NotFoundException {
        checkExists(regno);
        ofy().delete().type(feesone.class).id(regno).now();
        logger.info("Deleted feesone with ID: " + regno);
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
            path = "feesone",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<feesone> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<feesone> query = ofy().load().type(feesone.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<feesone> queryIterator = query.iterator();
        List<feesone> feesoneList = new ArrayList<feesone>(limit);
        while (queryIterator.hasNext()) {
            feesoneList.add(queryIterator.next());
        }
        return CollectionResponse.<feesone>builder().setItems(feesoneList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }

    private void checkExists(String regno) throws NotFoundException {
        try {
            ofy().load().type(feesone.class).id(regno).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find feesone with ID: " + regno);
        }
    }
}