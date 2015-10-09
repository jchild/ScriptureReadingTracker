package com.childstudios.scripturereadingtracker;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Jonathan on 7/10/2015.
 */
public class ScriptureDatabaseHandler extends SQLiteOpenHelper {

    private static final String DB_NAME = "ScriptureDB";
    private static final String S_TABLE = "Scriptures";
    private static final String B_TABLE = "Books";
    private static final String C_TABLE = "Chapters";

    private static final String S_ID = "s_id";
    private static final String B_ID = "b_id";
    private static final String C_ID = "c_id";

    private static final String B_NAME = "b_title";
    private static final String S_NAME = "s_name";
    private static final String C_NUMBER = "c_number";

    DataBaseHelper dbHelper;



    public ScriptureDatabaseHandler(Context context) throws SQLiteException {
        super(context, DB_NAME, null, 1);
        dbHelper = new DataBaseHelper(context);

    }

    public ArrayList<String> getAllScriptureNames(){
        ArrayList<String> names = new ArrayList<>();
        String selectSQL = "SELECT "+ S_NAME +" FROM " + S_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectSQL,null);
        if(cursor.moveToFirst()) {
            do {
                names.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        return names;
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        try {

            dbHelper.createDataBase();

        } catch (IOException ioe) {

            throw new Error("Unable to create database");

        }

        try {

            dbHelper.openDataBase();

        }catch(SQLiteException sqle){

            throw sqle;

        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
