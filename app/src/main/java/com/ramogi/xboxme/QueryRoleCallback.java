package com.ramogi.xboxme;

import com.ramogi.xbox.backend.roleApi.model.Role;

import java.util.List;

/**
 * Created by ROchola on 11/22/2015.
 */
public abstract class QueryRoleCallback {

    public abstract void querycomplete(List<Role> role);
}
