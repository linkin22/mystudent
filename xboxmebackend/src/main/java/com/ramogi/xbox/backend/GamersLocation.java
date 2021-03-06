package com.ramogi.xbox.backend;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.util.Date;
import java.net.URI;

/**
 * Created by ROchola on 11/19/2015.
 */
@Entity
public class GamersLocation {

    @Id
    private String email;
    private String displayname;
    private Date txntime;
    private double latx;
    private double longy;
    private String gamertag;
    private String photopath;
    //private java.net.URI photoURI;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public Date getTxntime() {
        return txntime;
    }

    public void setTxntime(Date txntime) {
        this.txntime = txntime;
    }

    public double getLatx() {
        return latx;
    }

    public void setLatx(double latx) {
        this.latx = latx;
    }

    public double getLongy() {
        return longy;
    }

    public void setLongy(double longy) {
        this.longy = longy;
    }

    public String getGamertag() {
        return gamertag;
    }

    public void setGamertag(String gamertag) {
        this.gamertag = gamertag;
    }

    public String getPhotoPath() {
        return photopath;
    }

    public void setPhotoPath(String photopath) {
        this.photopath = photopath;
    }

    //public java.net.URI getPhotoURI() { return photoURI; }

    //public void setPhotoURI(java.net.URI photoURI) { this.photoURI = photoURI;   }
}
