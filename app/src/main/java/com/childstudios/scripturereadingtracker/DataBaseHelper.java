package com.childstudios.scripturereadingtracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Jonathan on 7/10/2015.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    private final Context context;

    private static String DB_NAME = "ScripturesDB";
    private static String DB_PATH = "/data/data/com.childstudios.scripturereadingtracker/databases/";
    private SQLiteDatabase database;

    public DataBaseHelper(Context context) {
        super(context, DB_NAME , null, 1);
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

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
