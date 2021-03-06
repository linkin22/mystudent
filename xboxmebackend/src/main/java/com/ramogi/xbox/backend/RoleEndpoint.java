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
        name = "roleApi",
        version = "v1",
        resource = "role",
        namespace = @ApiNamespace(
                ownerDomain = "backend.xbox.ramogi.com",
                ownerName = "backend.xbox.ramogi.com",
                packagePath = ""
        )
)
public class RoleEndpoint {

    private static final Logger logger = Logger.getLogger(RoleEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;

    static {
        // Typically you would register this inside an OfyServive wrapper. See: https://code.google.com/p/objectify-appengine/wiki/BestPractices
        ObjectifyService.register(Role.class);
    }

    /**
     * Returns the {@link Role} with the corresponding ID.
     *
     * @param email the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code Role} with the provided ID.
     */
    @ApiMethod(
            name = "get",
            path = "role/{email}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public Role get(@Named("email") String email) throws NotFoundException {
        logger.info("Getting Role with ID: " + email);
        Role role = ofy().load().type(Role.class).id(email).now();
        if (role == null) {
            throw new NotFoundException("Could not find Role with ID: " + email);
        }
        return role;
    }

    /**
     * Inserts a new {@code Role}.
     */
    @ApiMethod(
            name = "insert",
            path = "role",
            httpMethod = ApiMethod.HttpMethod.POST)
    public Role insert(Role role) {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that role.email has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        ofy().save().entity(role).now();
        logger.info("Created Role with ID: " + role.getEmail());

        return ofy().load().entity(role).now();
    }

    /**
     * Updates an existing {@code Role}.
     *
     * @param email the ID of the entity to be updated
     * @param role  the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code email} does not correspond to an existing
     *                           {@code Role}
     */
    @ApiMethod(
            name = "update",
            path = "role/{email}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public Role update(@Named("email") String email, Role role) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        checkExists(email);
        ofy().save().entity(role).now();
        logger.info("Updated Role: " + role);
        return ofy().load().entity(role).now();
    }

    /**
     * Deletes the specified {@code Role}.
     *
     * @param email the ID of the entity to delete
     * @throws NotFoundException if the {@code email} does not correspond to an existing
     *                           {@code Role}
     */
    @ApiMethod(
            name = "remove",
            path = "role/{email}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("email") String email) throws NotFoundException {
        checkExists(email);
        ofy().delete().type(Role.class).id(email).now();
        logger.info("Deleted Role with ID: " + email);
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
            path = "role",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<Role> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<Role> query = ofy().load().type(Role.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<Role> queryIterator = query.iterator();
        List<Role> roleList = new ArrayList<Role>(limit);
        while (queryIterator.hasNext()) {
            roleList.add(queryIterator.next());
        }
        return CollectionResponse.<Role>builder().setItems(roleList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }

    private void checkExists(String email) throws NotFoundException {
        try {
            ofy().load().type(Role.class).id(email).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find Role with ID: " + email);
        }
    }
}