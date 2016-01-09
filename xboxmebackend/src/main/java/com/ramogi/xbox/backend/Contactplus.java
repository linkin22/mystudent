package com.ramogi.xbox.backend;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;


/**
 * Created by ROchola on 1/5/2016.
 */

@Entity
public class Contactplus {

    @Id
    private String email;
    private String regId;

    public Contactplus() {}

    public Contactplus(String email, String regId) {
        this.email = email;
        this.regId = regId;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getRegId() {
        return regId;
    }
    public void setRegId(String regId) {
        this.regId = regId;
    }

}
