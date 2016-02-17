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
        name = "feesthreeApi",
        version = "v1",
        resource = "feesthree",
        namespace = @ApiNamespace(
                ownerDomain = "backend.xbox.ramogi.com",
                ownerName = "backend.xbox.ramogi.com",
                packagePath = ""
        )
)
public class feesthreeEndpoint {

    private static final Logger logger = Logger.getLogger(feesthreeEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;

    static {
        // Typically you would register this inside an OfyServive wrapper. See: https://code.google.com/p/objectify-appengine/wiki/BestPractices
        ObjectifyService.register(feesthree.class);
    }

    /**
     * Returns the {@link feesthree} with the corresponding ID.
     *
     * @param regno the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code feesthree} with the provided ID.
     */
    @ApiMethod(
            name = "get",
            path = "feesthree/{regno}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public feesthree get(@Named("regno") String regno) throws NotFoundException {
        logger.info("Getting feesthree with ID: " + regno);
        feesthree feesthree = ofy().load().type(feesthree.class).id(regno).now();
        if (feesthree == null) {
            throw new NotFoundException("Could not find feesthree with ID: " + regno);
        }
        return feesthree;
    }

    /**
     * Inserts a new {@code feesthree}.
     */
    @ApiMethod(
            name = "insert",
            path = "feesthree",
            httpMethod = ApiMethod.HttpMethod.POST)
    public feesthree insert(feesthree feesthree) {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that feesthree.regno has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        ofy().save().entity(feesthree).now();
        logger.info("Created feesthree with ID: " + feesthree.getRegno());

        return ofy().load().entity(feesthree).now();
    }

    /**
     * Updates an existing {@code feesthree}.
     *
     * @param regno     the ID of the entity to be updated
     * @param feesthree the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code regno} does not correspond to an existing
     *                           {@code feesthree}
     */
    @ApiMethod(
            name = "update",
            path = "feesthree/{regno}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public feesthree update(@Named("regno") String regno, feesthree feesthree) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        checkExists(regno);
        ofy().save().entity(feesthree).now();
        logger.info("Updated feesthree: " + feesthree);
        return ofy().load().entity(feesthree).now();
    }

    /**
     * Deletes the specified {@code feesthree}.
     *
     * @param regno the ID of the entity to delete
     * @throws NotFoundException if the {@code regno} does not correspond to an existing
     *                           {@code feesthree}
     */
    @ApiMethod(
            name = "remove",
            path = "feesthree/{regno}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("regno") String regno) throws NotFoundException {
        checkExists(regno);
        ofy().delete().type(feesthree.class).id(regno).now();
        logger.info("Deleted feesthree with ID: " + regno);
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
            path = "feesthree",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<feesthree> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<feesthree> query = ofy().load().type(feesthree.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<feesthree> queryIterator = query.iterator();
        List<feesthree> feesthreeList = new ArrayList<feesthree>(limit);
        while (queryIterator.hasNext()) {
            feesthreeList.add(queryIterator.next());
        }
        return CollectionResponse.<feesthree>builder().setItems(feesthreeList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }

    private void checkExists(String regno) throws NotFoundException {
        try {
            ofy().load().type(feesthree.class).id(regno).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find feesthree with ID: " + regno);
        }
    }
}