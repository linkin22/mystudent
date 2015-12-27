package com.ramogi.xboxme;

import com.ramogi.xbox.backend.studentApi.model.Student;
import com.ramogi.xbox.backend.teacherApi.model.Teacher;

import java.util.List;

/**
 * Created by ROchola on 11/22/2015.
 */
public abstract class QueryStudentsCallback {

    public abstract void querycomplete(List<Student> students);
}
