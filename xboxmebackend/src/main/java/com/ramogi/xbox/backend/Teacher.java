package com.ramogi.xbox.backend;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.util.Date;
import java.net.URI;

/**
 * Created by ROchola on 12/16/2015.
 */
@Entity
public class Teacher {


    @Id
    private String email;
    private String tname;
    private String tschool;
    private String classes;
    private String subject;
    private String tmobile;
    private Date tcreated;
    private Date tdob;
    private String gender;
    private String createdby;


    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getTdob() {
        return tdob;
    }

    public void setTdob(Date tdob) {
        this.tdob = tdob;
    }

    public Date getTcreated() {
        return tcreated;
    }

    public void setTcreated(Date tcreated) {
        this.tcreated = tcreated;
    }

    public String getTmobile() {
        return tmobile;
    }

    public void setTmobile(String tmobile) {
        this.tmobile = tmobile;
    }

    public String getTschool() {
        return tschool;
    }

    public void setTschool(String tschool) {
        this.tschool = tschool;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getCreatedby() {
        return createdby;
    }

    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }


}
