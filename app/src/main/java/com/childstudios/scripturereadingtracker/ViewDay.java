package com.childstudios.scripturereadingtracker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ViewDay extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Date date;
    DatabaseHandler db;
    int gethour, getmin, sID, bID, cID;
    Boolean check;
    Event thisEvent;
    DataBaseHelper dbHelper;
    Spinner scriptureSpinner, bookSpinner,chapterSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_day);
        db = new DatabaseHandler(this);
        date = (Date)getIntent().getExtras().getSerializable("Date");
        check = (boolean)getIntent().getExtras().getSerializable("check");
        dbHelper = new DataBaseHelper(this);
        try {
            dbHelper.createDataBase();
        } catch (IOException ioe) {

            throw new Error("Unable to create database");

        }

        ArrayList<String> sNames = dbHelper.getAllScriptureNames();
        sNames = changeNameArray(sNames);



        TextView dateEdit = (TextView) findViewById(R.id.DateEdit);
        DateFormat df = SimpleDateFormat.getDateInstance();
        dateEdit.setText(df.format(date));

        scriptureSpinner = (Spinner) findViewById(R.id.scriptureSpinner);
        bookSpinner = (Spinner) findViewById(R.id.bookSpinner);
        chapterSpinner = (Spinner) findViewById(R.id.chapterSpinner);

        bookSpinner.setVisibility(View.INVISIBLE);
        bookSpinner.setOnItemSelectedListener(this);

        chapterSpinner.setVisibility(View.INVISIBLE);
        chapterSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<String> names = new ArrayAdapter<String>(this,R.layout.spinner_layout,sNames);
        names.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        scriptureSpinner.setAdapter(names);
        scriptureSpinner.setOnItemSelectedListener(this);

        if(check){
            thisEvent = db.getEvent((int)getIntent().getExtras().getSerializable("id"));

            TextView time = (TextView) findViewById(R.id.Time);
            String shour = String.format("%02d", thisEvent.getHour());
            String smin = String.format("%02d",thisEvent.getMin());
            time.setText(shour + ":" + smin);
            setScript();
        }
        else{
            thisEvent = new Event();
        }
    }

    public void setScript(){

        if(thisEvent.getScript() != 0){
            scriptureSpinner.setSelection(thisEvent.getScript());
            ArrayList<String>bNames = dbHelper.getAllBookNames(thisEvent.getScript());
            bookSpinner.setVisibility(View.VISIBLE);
            bNames = changeNameArray(bNames);
            ArrayAdapter<String> names = new ArrayAdapter<String>(this,R.layout.spinner_layout,bNames);
            names.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            bookSpinner.setAdapter(names);
        }

        if(thisEvent.getBook()!= 0) {
            bookSpinner.setSelection(thisEvent.getBook());

            ArrayList<String> cNames = dbHelper.getAllChapterNo(thisEvent.getScript(), thisEvent.getBook());
            cNames = changeNameArray(cNames);
            ArrayAdapter<String> names = new ArrayAdapter<String>(this,R.layout.spinner_layout, cNames);
            names.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            chapterSpinner.setAdapter(names);
        }

        if(thisEvent.getChapt()!= 0){
            chapterSpinner.setSelection(thisEvent.getChapt());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_day, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public ArrayList<String> changeNameArray(ArrayList<String> sNames){
        ArrayList<String> newArray = new ArrayList<>();

        newArray.add("Choose One");

        for (int i = 0; i<sNames.size();i++){
            newArray.add(sNames.get(i));
        }

        return newArray;
    }

    public void pickTime(View view){

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.dialog_time_picker, null, false);

        final TimePicker timePicker = (TimePicker) v.findViewById(R.id.myTimePicker);
        timePicker.setIs24HourView(true);
        timePicker.setCurrentHour(0);
        timePicker.setCurrentMinute(0);

        new AlertDialog.Builder(this).setView(v)
        .setTitle("Enter Time")
        .setMessage("Please input the time you have read:")
        .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                gethour = timePicker.getCurrentHour();
                getmin = timePicker.getCurrentMinute();

                thisEvent.setTime(gethour,getmin);

                TextView time = (TextView) findViewById(R.id.Time);
                String shour = String.format("%02d", timePicker.getCurrentHour());
                String smin = String.format("%02d",timePicker.getCurrentMinute());
                time.setText(shour + ":" + smin);
            }
        })
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).show();
    }

    public void pickDate(View view){/*
        View v = getLayoutInflater().inflate(R.layout.dialog_date_picker, null);
        AlertDialog.Builder newEvent = new AlertDialog.Builder(this);
        newEvent.setTitle("Select Date");
        newEvent.setMessage("Please select the date you want to record:");
        View layout = View.inflate(this,R.layout.dialog_date_picker, null);
        final DatePicker datePicker = (DatePicker) v.findViewById(R.id.datePicker);
        datePicker.init(date.getYear()+2,date.getMonth(),date.getDate(),null);
        newEvent.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Date newDate = new Date();
                DateFormat df = SimpleDateFormat.getDateInstance();

                newDate.setDate(datePicker.getDayOfMonth());
                newDate.setYear(datePicker.getYear());
                newDate.setMonth(datePicker.getMonth());
                date = newDate;
                Log.v("Calendar to ViewDay",datePicker.getDayOfMonth() + "/" + datePicker.getDayOfMonth() + "/" + datePicker.getYear());

                Log.v("ViewDay change date", df.format(date));
                Log.v("ViewDay change date", df.format(newDate));

                TextView dateView = (TextView) findViewById(R.id.DateEdit);

                dateView.setText(df.format(newDate));
            }
        });
        newEvent.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        newEvent.setView(layout);
        AlertDialog dialog = newEvent.create();
        dialog.show();*/
    }

    public void cancelEvent(View view){
        Intent i = new Intent(this,Calendar.class);
        startActivity(i);
        finish();
    }
    public void submitEvent(View view){

        if(check){
            thisEvent.setScript(sID);
            thisEvent.setBook(bID);
            thisEvent.setChapt(cID);
            Log.v("Update Event","Date: " + thisEvent.getMonth() + "/" + thisEvent.getDay() + "/" + thisEvent.getYear() +
                    " Time: " + thisEvent.getHour() + ":" + thisEvent.getMin());
            db.updateEvent(thisEvent);
            Intent i = new Intent(this,Calendar.class);
            startActivity(i);
            finish();
        }
        else{
            thisEvent.setScript(sID);
            thisEvent.setBook(bID);
            thisEvent.setChapt(cID);
            thisEvent.setDate(date);
            Log.v("Add Event","Date: " + thisEvent.getMonth() + "/" + thisEvent.getDay() + "/" + thisEvent.getYear() +
                    " Time: " + thisEvent.getHour() + ":" + thisEvent.getMin());

            db.addEvent(thisEvent);

            Intent i = new Intent(this,Calendar.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent(this,Calendar.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.scriptureSpinner:
                if (position != 0){
                    bookSpinner.setVisibility(View.VISIBLE);
                    ArrayList<String> bNames = dbHelper.getAllBookNames(position);
                    bNames = changeNameArray(bNames);
                    sID=position;
                    bookSpinner.setSelection(thisEvent.getBook());
                    ArrayAdapter<String> names = new ArrayAdapter<String>(this,R.layout.spinner_layout,bNames);
                    names.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    bookSpinner.setAdapter(names);
                }else {
                    scriptureSpinner.setSelection(thisEvent.getScript());
                    ArrayList<String>bNames = dbHelper.getAllBookNames(thisEvent.getScript());
                    bNames = changeNameArray(bNames);
                    ArrayAdapter<String> names = new ArrayAdapter<String>(this,R.layout.spinner_layout,bNames);
                    names.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    bookSpinner.setAdapter(names);
                }
                break;
            case R.id.bookSpinner:
                if(position != 0){
                    chapterSpinner.setVisibility(View.VISIBLE);
                    bID = position;
                    ArrayList<String> cNames = dbHelper.getAllChapterNo(sID,bID);
                    cNames = changeNameArray(cNames);
                    chapterSpinner.setSelection(thisEvent.getChapt());
                    ArrayAdapter<String> names = new ArrayAdapter<String>(this,R.layout.spinner_layout,cNames);
                    names.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    chapterSpinner.setAdapter(names);
                    thisEvent.setBook(0);

                }else{
                    bookSpinner.setSelection(thisEvent.getBook());
                    ArrayList<String> cNames = dbHelper.getAllChapterNo(thisEvent.getScript(), thisEvent.getBook());
                    cNames = changeNameArray(cNames);
                    ArrayAdapter<String> names = new ArrayAdapter<String>(this, R.layout.spinner_layout, cNames);
                    names.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    chapterSpinner.setAdapter(names);
                }
                break;
            case R.id.chapterSpinner:
                if(position !=0){
                    cID = position;
                    thisEvent.setChapt(0);
                }else{
                    chapterSpinner.setSelection(thisEvent.getChapt());
                }
                break;
            default:
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
