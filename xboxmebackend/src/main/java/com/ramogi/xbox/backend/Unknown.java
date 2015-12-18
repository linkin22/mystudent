package com.ramogi.xbox.backend;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import java.util.Date;

/**
 * Created by ROchola on 12/17/2015.
 */

@Entity
public class Unknown {

    @Id
    private String email;
    private String schoolname;
    private String displayname;
    private Date created;
    private String status;
    private Date responded;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSchoolname() {
        return schoolname;
    }

    public void setSchoolname(String schoolname) {
        this.schoolname = schoolname;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getResponded() {
        return responded;
    }

    public void setResponded(Date responded) {
        this.responded = responded;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

}
