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
        name = "classesApi",
        version = "v1",
        resource = "classes",
        namespace = @ApiNamespace(
                ownerDomain = "backend.xbox.ramogi.com",
                ownerName = "backend.xbox.ramogi.com",
                packagePath = ""
        )
)
public class classesEndpoint {

    private static final Logger logger = Logger.getLogger(classesEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;

    static {
        // Typically you would register this inside an OfyServive wrapper. See: https://code.google.com/p/objectify-appengine/wiki/BestPractices
        ObjectifyService.register(classes.class);
    }

    /**
     * Returns the {@link classes} with the corresponding ID.
     *
     * @param schoolname the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code classes} with the provided ID.
     */
    @ApiMethod(
            name = "get",
            path = "classes/{schoolname}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public classes get(@Named("schoolname") String schoolname) throws NotFoundException {
        logger.info("Getting classes with ID: " + schoolname);
        classes classes = ofy().load().type(classes.class).id(schoolname).now();
        if (classes == null) {
            throw new NotFoundException("Could not find classes with ID: " + schoolname);
        }
        return classes;
    }

    /**
     * Inserts a new {@code classes}.
     */
    @ApiMethod(
            name = "insert",
            path = "classes",
            httpMethod = ApiMethod.HttpMethod.POST)
    public classes insert(classes classes) {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that classes.schoolname has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        ofy().save().entity(classes).now();
        logger.info("Created classes.");

        return ofy().load().entity(classes).now();
    }

    /**
     * Updates an existing {@code classes}.
     *
     * @param schoolname the ID of the entity to be updated
     * @param classes    the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code schoolname} does not correspond to an existing
     *                           {@code classes}
     */
    @ApiMethod(
            name = "update",
            path = "classes/{schoolname}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public classes update(@Named("schoolname") String schoolname, classes classes) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        checkExists(schoolname);
        ofy().save().entity(classes).now();
        logger.info("Updated classes: " + classes);
        return ofy().load().entity(classes).now();
    }

    /**
     * Deletes the specified {@code classes}.
     *
     * @param schoolname the ID of the entity to delete
     * @throws NotFoundException if the {@code schoolname} does not correspond to an existing
     *                           {@code classes}
     */
    @ApiMethod(
            name = "remove",
            path = "classes/{schoolname}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("schoolname") String schoolname) throws NotFoundException {
        checkExists(schoolname);
        ofy().delete().type(classes.class).id(schoolname).now();
        logger.info("Deleted classes with ID: " + schoolname);
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
            path = "classes",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<classes> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<classes> query = ofy().load().type(classes.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<classes> queryIterator = query.iterator();
        List<classes> classesList = new ArrayList<classes>(limit);
        while (queryIterator.hasNext()) {
            classesList.add(queryIterator.next());
        }
        return CollectionResponse.<classes>builder().setItems(classesList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }

    private void checkExists(String schoolname) throws NotFoundException {
        try {
            ofy().load().type(classes.class).id(schoolname).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find classes with ID: " + schoolname);
        }
    }
}