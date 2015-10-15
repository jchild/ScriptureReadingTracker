package com.childstudios.scripturereadingtracker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;



import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class Calendar extends ActionBarActivity {
    ArrayList<Event> eventsArray;
    CalendarView cv;
    DatabaseHandler db;
    Event event;
    Date keepDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        cv = ((CalendarView)findViewById(R.id.calendar_view));
        db = new DatabaseHandler(this);
        eventsArray = db.getMonthEvents(cv.getCurrentDate());

        ImageView btnPrev = (ImageView)findViewById(R.id.calendar_prev_button);
        ImageView btnNext = (ImageView)findViewById(R.id.calendar_next_button);


        cv.updateCalendar();
        // assign event handler
        cv.setEventHandler(new CalendarView.EventHandler()
        {
            @Override
            public void onDayLongPress(Date date)
            {
               // DateFormat df = SimpleDateFormat.getDateInstance();
               // Toast.makeText(Calendar.this, df.format(date), Toast.LENGTH_SHORT).show();
                //newEvent(date);
                // show returned day
            }
            @Override
            public void onDayPress(Date date){
                DateFormat df = SimpleDateFormat.getDateInstance();
                boolean check = false;
                Intent i = new Intent(Calendar.this, ViewDay.class);
                i.putExtra("Date", date);
                if(eventsArray != null){
                    for(Event events : eventsArray){
                        if(events.getDay() == date.getDate() && events.getYear() == date.getYear() && events.getMonth() == date.getMonth()){
                            events.getMin();
                            i.putExtra("id", events.getID());
                            check = true;
                            break;
                        }

                    }
                }
                else{
                    Event blank = new Event();
                    i.putExtra("event", (Parcelable) blank);

                }

                i.putExtra("check", check);
                startActivity(i);
                finish();
            }
        });

        // add one month and refresh UI
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cv.addMonth();
                cv.updateCalendar();
            }
        });

        // subtract one month and refresh UI
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cv.subMonth();
                cv.updateCalendar();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calendar, menu);
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

    public void newEvent(Date date){

        keepDate = date;
        AlertDialog.Builder newEvent = new AlertDialog.Builder(this);
        newEvent.setTitle("Add new record");
        newEvent.setMessage("Please input the following information:");
        View layout = View.inflate(this,R.layout.dialog_new_event, null);



        newEvent.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Event newEvent = new Event();
                newEvent.setDate(keepDate);
                eventsArray.add(newEvent);
                db.addEvent(newEvent);
                cv.updateCalendar();
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
        dialog.show();

    }



    @Override
    public void onResume(){
        super.onResume();
        eventsArray = db.getMonthEvents(cv.getCurrentDate());
        cv.updateCalendar();

    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
        finish();
    }
}
