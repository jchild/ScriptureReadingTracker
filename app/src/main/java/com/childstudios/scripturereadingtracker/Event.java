package com.childstudios.scripturereadingtracker;


import java.sql.Time;
import java.util.Date;
import java.util.HashSet;

/**
 * Created by Jonathan on 2/9/2015.
 */
public class Event {

    int month;
    int day;
    int year;
    int id;
    int hour;
    int min;
    int script;
    int book;
    int chapt;


    public Event(){
        id = 0;
        hour = 0;
        min = 0;
        script = 0;
        book = 0;
        chapt = 0;

    }

    public Event(int id, int day, int month, int year, int hour, int min, int script, int book, int chapt){
        this.id = id;
        this.month = month;
        this.day = day;
        this.year = year;
        this.hour = hour;
        this.min = min;
        this.script = script;
        this.book = book;
        this.chapt = chapt;
    }

    public int getID(){
        return id;
    }

    public Date getDate() {

        Date date = new Date();

        date.setDate(day);
        date.setMonth(month);
        date.setYear(year);

        return date;
    }
    public void setDate(Date date){
        day = date.getDate();
        month = date.getMonth();
        year = date.getYear();
    }


    public void setTime(int hour, int min){
        this.hour = hour;
        this.min = min;
    }

    public int getHour(){return hour;}
    public int getMin(){return min;}
    public int getDay(){return day;}
    public int getYear(){return year;}
    public int getMonth(){
        return month;
    }

    public void setScript(int script){this.script = script;}
    public void setBook(int book){this.book = book;}
    public void setChapt(int chapt){this.chapt = chapt;}

    public int getScript(){return script;}
    public int getBook(){return book;}
    public int getChapt(){return chapt;}
}
