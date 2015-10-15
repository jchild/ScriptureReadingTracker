package com.childstudios.scripturereadingtracker;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Jonathan on 26/6/2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ScriptureTrackerDB";
    private static final String TABLE_CALENDAR = "Calendar";
    private static final String KEY_ID = "c_id";
    private static final String KEY_DAY = "c_day";
    private static final String KEY_MONTH = "c_month";
    private static final String KEY_YEAR = "c_year";

    private static final String KEY_SCRIPTURE = "c_scripture";
    private static final String KEY_BOOK = "c_book";
    private static final String KEY_CHAPTER = "c_chapter";
    private static final String KEY_HOUR = "c_hour";
    private static final String KEY_MIN = "c_min";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PLAYERS_TABLE = "CREATE TABLE " + TABLE_CALENDAR + " ( " + KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_DAY + " INTEGER, "+ KEY_MONTH + " INTEGER, "+ KEY_YEAR + " INTEGER, " + KEY_HOUR + " INTEGER, "
                + KEY_MIN + " INTEGER, " + KEY_SCRIPTURE + " INTEGER, " + KEY_BOOK + " INTEGER, "+ KEY_CHAPTER +" INTEGER" + " )";
        db.execSQL(CREATE_PLAYERS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CALENDAR);

        // Create tables again
        onCreate(db);
        db.close();
    }

    public void endGame(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CALENDAR);
        onCreate(db);
        db.close();
    }

    void addEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();

        int id = getMaxID();
        id = id +1;


        ContentValues values = new ContentValues();
        values.put(KEY_ID, id);
        values.put(KEY_DAY, event.getDay());
        values.put(KEY_MONTH, event.getMonth());
        values.put(KEY_YEAR, event.getYear());
        values.put(KEY_HOUR, event.getHour());
        values.put(KEY_MIN, event.getMin());
        values.put(KEY_SCRIPTURE, event.getScript());
        values.put(KEY_BOOK, event.getBook());
        values.put(KEY_CHAPTER, event.getChapt());

        // Inserting Row
        db.insert(TABLE_CALENDAR, null, values);
        db.close(); // Closing database connection
    }

    // Getting single contact
    Event getEvent(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CALENDAR, new String[]{KEY_ID, KEY_DAY, KEY_MONTH, KEY_YEAR, KEY_HOUR, KEY_MIN, KEY_SCRIPTURE, KEY_BOOK, KEY_CHAPTER}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Event event = new Event(Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(1)),Integer.parseInt(cursor.getString(2)),Integer.parseInt(cursor.getString(3)), Integer.parseInt(cursor.getString(4)), Integer.parseInt(cursor.getString(5)), Integer.parseInt(cursor.getString(6)), Integer.parseInt(cursor.getString(7)), Integer.parseInt(cursor.getString(8)));
        db.close();
        return event;
    }

    // Getting All Events
    public ArrayList<Event> getAllEvents() {
        ArrayList<Event> eventList = new ArrayList<Event>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CALENDAR;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Event event = new Event(Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(1)),Integer.parseInt(cursor.getString(2)),Integer.parseInt(cursor.getString(3)), Integer.parseInt(cursor.getString(4)), Integer.parseInt(cursor.getString(5)), Integer.parseInt(cursor.getString(6)), Integer.parseInt(cursor.getString(7)), Integer.parseInt(cursor.getString(8)));
                eventList.add(event);
            } while (cursor.moveToNext());
        }
        db.close();
        return eventList;
    }

    // Getting Events for a given month
    public ArrayList<Event> getMonthEvents(Date currentMonth) {
        ArrayList<Event> eventList = new ArrayList<Event>();
        // Select All Query

        int month = currentMonth.getMonth();
        int year = currentMonth.getYear();

        String selectQuery = "SELECT  * FROM " + TABLE_CALENDAR + " WHERE " + KEY_MONTH + " = " + month + " AND " + KEY_YEAR + " = " + year;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Event event = new Event(Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(1)),Integer.parseInt(cursor.getString(2)),Integer.parseInt(cursor.getString(3)), Integer.parseInt(cursor.getString(4)), Integer.parseInt(cursor.getString(5)), Integer.parseInt(cursor.getString(6)), Integer.parseInt(cursor.getString(7)), Integer.parseInt(cursor.getString(8)));
                eventList.add(event);
            } while (cursor.moveToNext());
        }
        db.close();
        return eventList;
    }

    public void updateEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DAY, String.valueOf(event.getDay()));
        values.put(KEY_MONTH, String.valueOf(event.getMonth()));
        values.put(KEY_YEAR, String.valueOf(event.getYear()));
        values.put(KEY_HOUR, String.valueOf(event.getHour()));
        values.put(KEY_MIN, String.valueOf(event.getMin()));
        values.put(KEY_SCRIPTURE, event.getScript());
        values.put(KEY_BOOK, event.getBook());
        values.put(KEY_CHAPTER, event.getChapt());

        // updating row
        db.update(TABLE_CALENDAR, values, KEY_ID + " = ?", new String[]{String.valueOf(event.getID())});
        db.close();
    }

    public void deleteEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CALENDAR, KEY_ID + " = ?", new String[]{String.valueOf(event.getID())});
        db.close();
    }


    public int getEntryCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CALENDAR;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int num = cursor.getCount();
        cursor.close();

        // return count
        return num;
    }

    public int getMaxID(){
        int id = 0;

        String query = "SELECT MAX("+ KEY_ID +") FROM " + TABLE_CALENDAR;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null)
            cursor.moveToFirst();
        id = cursor.getInt(0);

        return id;
    }
}