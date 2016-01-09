package com.ramogi.xboxme;

import com.ramogi.xbox.backend.teacherApi.model.Teacher;
import com.ramogi.xbox.backend.contactplusApi.model.Contactplus;

/**
 * Created by ROchola on 11/22/2015.
 */
public abstract class QueryOneContactCallback {

    public abstract void querycomplete(Contactplus contactplus);
}
