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
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ViewDay extends AppCompatActivity {
    Date date;
    DatabaseHandler db;
    int gethour, getmin;
    Boolean check;
    Event thisEvent;
    ScriptureDatabaseHandler sdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_day);
        db = new DatabaseHandler(this);
        date = (Date)getIntent().getExtras().getSerializable("Date");
        check = (boolean)getIntent().getExtras().getSerializable("check");
        sdb = new ScriptureDatabaseHandler(this);

        ArrayList<String> sNames = sdb.getAllScriptureNames();

        if(check){
            thisEvent = db.getEvent((int)getIntent().getExtras().getSerializable("id"));

            TextView time = (TextView) findViewById(R.id.Time);
            time.setText(thisEvent.getHour() + ":" + thisEvent.getMin());
        }
        else{
            thisEvent = new Event();
        }

        TextView dateEdit = (TextView) findViewById(R.id.DateEdit);
        DateFormat df = SimpleDateFormat.getDateInstance();
        dateEdit.setText(df.format(date));

        Spinner scriptureSpinner = (Spinner) findViewById(R.id.scriptureSpinner);
        ArrayAdapter<String> names = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,sNames);
        names.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        scriptureSpinner.setAdapter(names);

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
                time.setText(timePicker.getCurrentHour().toString() + ":" + timePicker.getCurrentMinute().toString());
            }
        })
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).show();
    }

    public void pickDate(View view){
       /* View v = getLayoutInflater().inflate(R.layout.dialog_date_picker, null);
        AlertDialog.Builder newEvent = new AlertDialog.Builder(this);
        newEvent.setTitle("Select Date");
        newEvent.setMessage("Please select the date you want to record:");
        View layout = View.inflate(this,R.layout.dialog_date_picker, null);
        final DatePicker datePicker = (DatePicker) v.findViewById(R.id.datePicker);
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
            Log.v("Update Event","Date: " + thisEvent.getMonth() + "/" + thisEvent.getDay() + "/" + thisEvent.getYear() +
                    " Time: " + thisEvent.getHour() + ":" + thisEvent.getMin());
            db.updateEvent(thisEvent);
            Intent i = new Intent(this,Calendar.class);
            startActivity(i);
            finish();
        }
        else{
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
}
