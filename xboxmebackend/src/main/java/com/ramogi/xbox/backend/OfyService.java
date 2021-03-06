package com.ramogi.xbox.backend;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;

/**
 * Objectify service wrapper so we can statically register our persistence classes
 * More on Objectify here : https://code.google.com/p/objectify-appengine/
 */
public class OfyService {

    static {
        ObjectifyService.register(RegistrationRecord.class);
        ObjectifyService.register(GamersLocation.class);
        ObjectifyService.register(Teacher.class);
        ObjectifyService.register(Role.class);
        ObjectifyService.register(Unknown.class);
        ObjectifyService.register(Student.class);
        ObjectifyService.register(Contactplus.class);
        ObjectifyService.register(classes.class);
        ObjectifyService.register(examdetails.class);
        ObjectifyService.register(examsummary.class);
        ObjectifyService.register(feesone.class);
        ObjectifyService.register(feestwo.class);
        ObjectifyService.register(feesthree.class);
        ObjectifyService.register(subjects.class);
        ObjectifyService.register(schoolreg.class);
        ObjectifyService.register(schooldetails.class);
    }

    public static Objectify ofy() {
        return ObjectifyService.ofy();
    }

    public static ObjectifyFactory factory() {
        return ObjectifyService.factory();
    }
}
