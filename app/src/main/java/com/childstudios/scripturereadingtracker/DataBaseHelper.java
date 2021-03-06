package com.childstudios.scripturereadingtracker;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by Jonathan on 7/10/2015.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    private final Context context;

    private static String DB_NAME = "ScripturesDB";
    private static String DB_PATH;

    private static final String S_TABLE = "Scriptures";
    private static final String B_TABLE = "Books";
    private static final String C_TABLE = "Chapters";

    private static final String S_ID = "s_id";
    private static final String B_ID = "b_id";
    private static final String C_ID = "c_id";

    private static final String B_NAME = "b_title";
    private static final String S_NAME = "s_name";
    private static final String C_NUMBER = "c_number";

    private SQLiteDatabase database;

    public DataBaseHelper(Context context) {
        super(context, DB_NAME , null, 1);
        DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        this.context = context;
    }

    public void createDataBase() throws IOException{
        boolean ifExist = checkDataBase();

        if(ifExist){

        }
        else {
            this.getReadableDatabase();
            try {
                copyDataBase();
            }
            catch (IOException e){
                throw new Error("Error copying Database");
            }
        }

    }

    private void copyDataBase() throws IOException{

        InputStream input = context.getAssets().open(DB_NAME);

        String outFileName = DB_PATH + DB_NAME;

        OutputStream output = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024];
        int length;
        while((length = input.read(buffer))>0){
            output.write(buffer,0,length);
        }
        output.flush();
        output.close();
        input.close();
    }

    private boolean checkDataBase() {

        SQLiteDatabase checkDB = null;

        try{
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        }
        catch (SQLiteException e){}

        if(checkDB != null)
            checkDB.close();

        return checkDB != null ? true : false;
    }

    public void openDataBase() throws SQLiteException{

        String path = DB_PATH + DB_NAME;
        database = SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READONLY);
    }
    @Override
    public synchronized void close(){
        if(database != null)
            database.close();
        super.close();

    }

    public ArrayList<String> getAllScriptureNames() {
        ArrayList<String> names = new ArrayList<>();
        String selectSQL = "SELECT "+ S_NAME +" FROM " + S_TABLE;
        try{
            openDataBase();
            Cursor cursor = database.rawQuery(selectSQL, null);
            if(cursor.moveToFirst()) {
                do {
                    names.add(cursor.getString(0));
                } while (cursor.moveToNext());
            }
            close();
        }catch(SQLiteException sqle) {

            throw sqle;
        }

        return names;
    }

    public ArrayList<String> getAllBookNames(int s_id){
        ArrayList<String> bNames = new ArrayList<>();
        String selectSQL = "SELECT " + B_NAME + " FROM " + B_TABLE + " WHERE " + S_ID + " = " + s_id;

        try {
            openDataBase();
            Cursor cursor = database.rawQuery(selectSQL,null);

            if(cursor.moveToFirst()){
                do {
                    bNames.add(cursor.getString(0));
                }while (cursor.moveToNext());
            }
        }catch (SQLiteException sqle){
            throw sqle;
        }


        return bNames;
    }

    public ArrayList<String> getAllChapterNo(int sID, int bID){
        ArrayList<String> cNames = new ArrayList<>();

        String selectSQL = "SELECT " + C_NUMBER + " FROM " + C_TABLE + " WHERE " + S_ID + " = " + sID + " AND " + B_ID + " = " + bID;
        try {
            openDataBase();
            Cursor cursor = database.rawQuery(selectSQL,null);

            if (cursor.moveToFirst()){
                do {
                    cNames.add(cursor.getString(0));
                }while (cursor.moveToNext());
            }
        }catch (SQLiteException sqle){
            throw sqle;
        }

        return  cNames;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
