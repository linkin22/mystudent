package com.ramogi.xboxme;

import com.ramogi.xbox.backend.teacherApi.model.Teacher;

import java.util.List;

/**
 * Created by ROchola on 11/22/2015.
 */
public abstract class QueryTeacherCallback {

    public abstract void querycomplete(List<Teacher> teacher);
}
