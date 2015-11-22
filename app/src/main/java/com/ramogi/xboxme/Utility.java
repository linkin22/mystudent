package com.ramogi.xboxme;

import android.util.Log;

/**
 * Created by ROchola on 7/13/2015.
 */
public class Utility {
    private String time;
    private String one = "2015-07-13T06:27:55.051Z";
    private String two = "2015-07-13T18:05:33.515Z";

    public Utility(){

    }
    public String formatTime(String time){
        this.time = time;

        String month = convertMonth(time.substring(5, 7));
        String day = convertDay(time.substring(8, 10));
        String hhmm = time.substring(11, 16);

        Log.v("format time", month + " " + day + " " + hhmm);

        return month+" "+day+" "+hhmm;

    }
    public String convertMonth(String time){
        switch (time){
            case "01":
                return "Jan";
            case "02":
                return "Feb";
            case "03":
                return "Mar";
            case "04":
                return "Apr";
            case "05":
                return "May";
            case "06":
                return "Jun";
            case "07":
                return "Jul";
            case "08":
                return "Aug";
            case "09":
                return "Sep";
            case "10":
                return "Oct";
            case "11":
                return "Nov";
            case "12":
                return "Dec";
            default:
                return time;



        }
    }

    private String convertDay(String date){
        switch (date){
            case "01":
                return "1st";
            case "02":
                return "2nd";
            case "03":
                return "3rd";
            case "04":
                return "4th";
            case "05":
                return "5th";
            case "06":
                return "6th";
            case "07":
                return "7th";
            case "08":
                return "8th";
            case "09":
                return "9th";
            default:
                return date+"th";
        }
    }
}
