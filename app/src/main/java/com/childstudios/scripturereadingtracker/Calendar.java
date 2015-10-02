package com.childstudios.scripturereadingtracker;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class Calendar extends ActionBarActivity {
    ArrayList<Event> eventsArray;
    CalendarView cv;
    DatabaseHandler db;
    Event event;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        cv = ((CalendarView)findViewById(R.id.calendar_view));
        db = new DatabaseHandler(this);
        eventsArray = db.getAllEvents(cv.getCurrentDate());

        ImageView btnPrev = (ImageView)findViewById(R.id.calendar_prev_button);
        ImageView btnNext = (ImageView)findViewById(R.id.calendar_next_button);


        cv.updateCalendar();
        // assign event handler
        cv.setEventHandler(new CalendarView.EventHandler()
        {
            @Override
            public void onDayLongPress(Date date)
            {
                // show returned day
                DateFormat df = SimpleDateFormat.getDateInstance();
                Toast.makeText(Calendar.this, df.format(date), Toast.LENGTH_SHORT).show();
                newEvent(date);
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

        Event newEvent = new Event();
        newEvent.setDate(date);

        Toast.makeText(Calendar.this, ""+ date,Toast.LENGTH_LONG).show();
        eventsArray.add(newEvent);
        db.addEvent(newEvent);
        cv.updateCalendar();

    }

    @Override
    public void onResume(){
        super.onResume();
        eventsArray = db.getAllEvents(cv.getCurrentDate());
        cv.updateCalendar();

    }
}
