package com.ramogi.xbox.backend;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import java.util.Date;

/**
 * Created by ROchola on 2/17/2016.
 */
@Entity
public class feesthree {

    @Id
    private String regno;
    private String item;
    private String amount;
    private String amountpaid;
    private String balance;
    private String inputby;
    private Date inputwhen;
    private String schoolname;


    public String getRegno() {
        return regno;
    }

    public void setRegno(String regno) {
        this.regno = regno;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAmountpaid() {
        return amountpaid;
    }

    public void setAmountpaid(String amountpaid) {
        this.amountpaid = amountpaid;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getInputby() {
        return inputby;
    }

    public void setInputby(String inputby) {
        this.inputby = inputby;
    }

    public Date getInputwhen() {
        return inputwhen;
    }

    public void setInputwhen(Date inputwhen) {
        this.inputwhen = inputwhen;
    }
}
