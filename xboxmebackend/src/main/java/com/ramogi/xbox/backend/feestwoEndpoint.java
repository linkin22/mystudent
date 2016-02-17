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
        name = "feestwoApi",
        version = "v1",
        resource = "feestwo",
        namespace = @ApiNamespace(
                ownerDomain = "backend.xbox.ramogi.com",
                ownerName = "backend.xbox.ramogi.com",
                packagePath = ""
        )
)
public class feestwoEndpoint {

    private static final Logger logger = Logger.getLogger(feestwoEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;

    static {
        // Typically you would register this inside an OfyServive wrapper. See: https://code.google.com/p/objectify-appengine/wiki/BestPractices
        ObjectifyService.register(feestwo.class);
    }

    /**
     * Returns the {@link feestwo} with the corresponding ID.
     *
     * @param regno the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code feestwo} with the provided ID.
     */
    @ApiMethod(
            name = "get",
            path = "feestwo/{regno}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public feestwo get(@Named("regno") String regno) throws NotFoundException {
        logger.info("Getting feestwo with ID: " + regno);
        feestwo feestwo = ofy().load().type(feestwo.class).id(regno).now();
        if (feestwo == null) {
            throw new NotFoundException("Could not find feestwo with ID: " + regno);
        }
        return feestwo;
    }

    /**
     * Inserts a new {@code feestwo}.
     */
    @ApiMethod(
            name = "insert",
            path = "feestwo",
            httpMethod = ApiMethod.HttpMethod.POST)
    public feestwo insert(feestwo feestwo) {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that feestwo.regno has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        ofy().save().entity(feestwo).now();
        logger.info("Created feestwo with ID: " + feestwo.getRegno());

        return ofy().load().entity(feestwo).now();
    }

    /**
     * Updates an existing {@code feestwo}.
     *
     * @param regno   the ID of the entity to be updated
     * @param feestwo the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code regno} does not correspond to an existing
     *                           {@code feestwo}
     */
    @ApiMethod(
            name = "update",
            path = "feestwo/{regno}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public feestwo update(@Named("regno") String regno, feestwo feestwo) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        checkExists(regno);
        ofy().save().entity(feestwo).now();
        logger.info("Updated feestwo: " + feestwo);
        return ofy().load().entity(feestwo).now();
    }

    /**
     * Deletes the specified {@code feestwo}.
     *
     * @param regno the ID of the entity to delete
     * @throws NotFoundException if the {@code regno} does not correspond to an existing
     *                           {@code feestwo}
     */
    @ApiMethod(
            name = "remove",
            path = "feestwo/{regno}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("regno") String regno) throws NotFoundException {
        checkExists(regno);
        ofy().delete().type(feestwo.class).id(regno).now();
        logger.info("Deleted feestwo with ID: " + regno);
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
            path = "feestwo",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<feestwo> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<feestwo> query = ofy().load().type(feestwo.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<feestwo> queryIterator = query.iterator();
        List<feestwo> feestwoList = new ArrayList<feestwo>(limit);
        while (queryIterator.hasNext()) {
            feestwoList.add(queryIterator.next());
        }
        return CollectionResponse.<feestwo>builder().setItems(feestwoList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }

    private void checkExists(String regno) throws NotFoundException {
        try {
            ofy().load().type(feestwo.class).id(regno).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find feestwo with ID: " + regno);
        }
    }
}