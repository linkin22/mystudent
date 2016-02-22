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
        name = "schooldetailsApi",
        version = "v1",
        resource = "schooldetails",
        namespace = @ApiNamespace(
                ownerDomain = "backend.xbox.ramogi.com",
                ownerName = "backend.xbox.ramogi.com",
                packagePath = ""
        )
)
public class schooldetailsEndpointone {

    private static final Logger logger = Logger.getLogger(schooldetailsEndpointone.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;

    static {
        // Typically you would register this inside an OfyServive wrapper. See: https://code.google.com/p/objectify-appengine/wiki/BestPractices
        ObjectifyService.register(schooldetails.class);
    }

    /**
     * Returns the {@link schooldetails} with the corresponding ID.
     *
     * @param schoolname the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code schooldetails} with the provided ID.
     */
    @ApiMethod(
            name = "get",
            path = "schooldetails/{schoolname}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public schooldetails get(@Named("schoolname") String schoolname) throws NotFoundException {
        logger.info("Getting schooldetails with ID: " + schoolname);
        schooldetails schooldetails = ofy().load().type(schooldetails.class).id(schoolname).now();
        if (schooldetails == null) {
            throw new NotFoundException("Could not find schooldetails with ID: " + schoolname);
        }
        return schooldetails;
    }

    /**
     * Inserts a new {@code schooldetails}.
     */
    @ApiMethod(
            name = "insert",
            path = "schooldetails",
            httpMethod = ApiMethod.HttpMethod.POST)
    public schooldetails insert(schooldetails schooldetails) {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that schooldetails.schoolname has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        ofy().save().entity(schooldetails).now();
        logger.info("Created schooldetails with ID: " + schooldetails.getSchoolname());

        return ofy().load().entity(schooldetails).now();
    }

    /**
     * Updates an existing {@code schooldetails}.
     *
     * @param schoolname    the ID of the entity to be updated
     * @param schooldetails the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code schoolname} does not correspond to an existing
     *                           {@code schooldetails}
     */
    @ApiMethod(
            name = "update",
            path = "schooldetails/{schoolname}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public schooldetails update(@Named("schoolname") String schoolname, schooldetails schooldetails) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        checkExists(schoolname);
        ofy().save().entity(schooldetails).now();
        logger.info("Updated schooldetails: " + schooldetails);
        return ofy().load().entity(schooldetails).now();
    }

    /**
     * Deletes the specified {@code schooldetails}.
     *
     * @param schoolname the ID of the entity to delete
     * @throws NotFoundException if the {@code schoolname} does not correspond to an existing
     *                           {@code schooldetails}
     */
    @ApiMethod(
            name = "remove",
            path = "schooldetails/{schoolname}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("schoolname") String schoolname) throws NotFoundException {
        checkExists(schoolname);
        ofy().delete().type(schooldetails.class).id(schoolname).now();
        logger.info("Deleted schooldetails with ID: " + schoolname);
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
            path = "schooldetails",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<schooldetails> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<schooldetails> query = ofy().load().type(schooldetails.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<schooldetails> queryIterator = query.iterator();
        List<schooldetails> schooldetailsList = new ArrayList<schooldetails>(limit);
        while (queryIterator.hasNext()) {
            schooldetailsList.add(queryIterator.next());
        }
        return CollectionResponse.<schooldetails>builder().setItems(schooldetailsList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }

    private void checkExists(String schoolname) throws NotFoundException {
        try {
            ofy().load().type(schooldetails.class).id(schoolname).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find schooldetails with ID: " + schoolname);
        }
    }
}