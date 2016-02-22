package com.ramogi.xbox.backend;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.util.Date;

/**
 * Created by rochola on 16/02/2016.
 */
@Entity
public class schooldetails {

    @Id
    private String adminemail;
    @Index
    private String schoolname;
    private String schoolcomments;
    private double latx;
    private double laty;
    private String schoolemail;
    private Date createdate;
    private String createdby;

    public String getSchoolname() {
        return schoolname;
    }

    public void setSchoolname(String schoolname) {
        this.schoolname = schoolname;
    }

    public String getSchoolcomments() {
        return schoolcomments;
    }

    public void setSchoolcomments(String schoolcomments) {
        this.schoolcomments = schoolcomments;
    }

    public double getLatx() {
        return latx;
    }

    public void setLatx(double latx) {
        this.latx = latx;
    }

    public double getLaty() {
        return laty;
    }

    public void setLaty(double laty) {
        this.laty = laty;
    }

    public String getAdminemail() {
        return adminemail;
    }

    public void setAdminemail(String adminemail) {
        this.adminemail = adminemail;
    }

    public String getSchoolemail() {
        return schoolemail;
    }

    public void setSchoolemail(String schoolemail) {
        this.schoolemail = schoolemail;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public String getCreatedby() {
        return createdby;
    }

    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }
}
