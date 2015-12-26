package com.ramogi.xbox.backend;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.util.Date;

/**
 * Created by ROchola on 12/23/2015.
 */
@Entity
public class Student {

    @Id
    private String parentemail;
    private String admno;
    @Index
    private String schoolname;
    private Date dob;
    private String studentname;
    private String gender;
    private String classform;
    private String classteacher;
    private int parentphone;
    private String tcomments;
    private String createdby;
    private Date createdate;

    public String getTcomments() {
        return tcomments;
    }

    public void setTcomments(String tcomments) {
        this.tcomments = tcomments;
    }

    public int getParentphone() {
        return parentphone;
    }

    public void setParentphone(int parentphone) {
        this.parentphone = parentphone;
    }

    public String getClassteacher() {
        return classteacher;
    }

    public void setClassteacher(String classteacher) {
        this.classteacher = classteacher;
    }

    public String getClassform() {
        return classform;
    }

    public void setClassform(String classform) {
        this.classform = classform;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getStudentname() {
        return studentname;
    }

    public void setStudentname(String studentname) {
        this.studentname = studentname;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getSchoolname() {
        return schoolname;
    }

    public void setSchoolname(String schoolname) {
        this.schoolname = schoolname;
    }

    public String getAdmno() {
        return admno;
    }

    public void setAdmno(String admno) {
        this.admno = admno;
    }

    public String getParentemail() {
        return parentemail;
    }

    public void setParentemail(String parentemail) {
        this.parentemail = parentemail;
    }

    public Date getCreatedate() { return createdate;   }

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
