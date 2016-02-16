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
        name = "schoolregApi",
        version = "v1",
        resource = "schoolreg",
        namespace = @ApiNamespace(
                ownerDomain = "backend.xbox.ramogi.com",
                ownerName = "backend.xbox.ramogi.com",
                packagePath = ""
        )
)
public class schoolregEndpoint {

    private static final Logger logger = Logger.getLogger(schoolregEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;

    static {
        // Typically you would register this inside an OfyServive wrapper. See: https://code.google.com/p/objectify-appengine/wiki/BestPractices
        ObjectifyService.register(schoolreg.class);
    }

    /**
     * Returns the {@link schoolreg} with the corresponding ID.
     *
     * @param schoolname the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code schoolreg} with the provided ID.
     */
    @ApiMethod(
            name = "get",
            path = "schoolreg/{schoolname}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public schoolreg get(@Named("schoolname") String schoolname) throws NotFoundException {
        logger.info("Getting schoolreg with ID: " + schoolname);
        schoolreg schoolreg = ofy().load().type(schoolreg.class).id(schoolname).now();
        if (schoolreg == null) {
            throw new NotFoundException("Could not find schoolreg with ID: " + schoolname);
        }
        return schoolreg;
    }

    /**
     * Inserts a new {@code schoolreg}.
     */
    @ApiMethod(
            name = "insert",
            path = "schoolreg",
            httpMethod = ApiMethod.HttpMethod.POST)
    public schoolreg insert(schoolreg schoolreg) {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that schoolreg.schoolname has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        ofy().save().entity(schoolreg).now();
        logger.info("Created schoolreg.");

        return ofy().load().entity(schoolreg).now();
    }

    /**
     * Updates an existing {@code schoolreg}.
     *
     * @param schoolname the ID of the entity to be updated
     * @param schoolreg  the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code schoolname} does not correspond to an existing
     *                           {@code schoolreg}
     */
    @ApiMethod(
            name = "update",
            path = "schoolreg/{schoolname}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public schoolreg update(@Named("schoolname") String schoolname, schoolreg schoolreg) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        checkExists(schoolname);
        ofy().save().entity(schoolreg).now();
        logger.info("Updated schoolreg: " + schoolreg);
        return ofy().load().entity(schoolreg).now();
    }

    /**
     * Deletes the specified {@code schoolreg}.
     *
     * @param schoolname the ID of the entity to delete
     * @throws NotFoundException if the {@code schoolname} does not correspond to an existing
     *                           {@code schoolreg}
     */
    @ApiMethod(
            name = "remove",
            path = "schoolreg/{schoolname}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("schoolname") String schoolname) throws NotFoundException {
        checkExists(schoolname);
        ofy().delete().type(schoolreg.class).id(schoolname).now();
        logger.info("Deleted schoolreg with ID: " + schoolname);
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
            path = "schoolreg",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<schoolreg> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<schoolreg> query = ofy().load().type(schoolreg.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<schoolreg> queryIterator = query.iterator();
        List<schoolreg> schoolregList = new ArrayList<schoolreg>(limit);
        while (queryIterator.hasNext()) {
            schoolregList.add(queryIterator.next());
        }
        return CollectionResponse.<schoolreg>builder().setItems(schoolregList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }

    private void checkExists(String schoolname) throws NotFoundException {
        try {
            ofy().load().type(schoolreg.class).id(schoolname).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find schoolreg with ID: " + schoolname);
        }
    }
}