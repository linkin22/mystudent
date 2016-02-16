package com.ramogi.xbox.backend;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.util.Date;

/**
 * Created by rochola on 16/02/2016.
 */
@Entity
public class schoolreg {

    @Id
    private String schoolname;
    private String admno;
    private String usedbystudent;
    private String usedbyteacher;
    private Date createdate;
    private Date useddate;


}
