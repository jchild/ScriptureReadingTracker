package com.childstudios.scripturereadingtracker;


import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.util.ArrayList;
import java.util.Random;

import android.os.Handler;


public class MainActivity extends ActionBarActivity {

    CalendarView calendar;
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawerLayout;
    private ListView list;
    private ListViewAdapter adapter;
    private TextSwitcher rssFeed;

    Handler feedHandler = new Handler();

    String textToShow[]={"\"To the proud, the applause of the world rings in their ears; to the humble, the applause of heaven warms their hearts.\" —Ezra Taft Benson",
            "\"We will not accidentally come to believe in the Savior and His gospel.\" —L. Whitney Clayton",
            "\"Jesus Christ can help us fix anything that needs fixing in our lives.\" —M. Russell Ballard",
            "\"Many things are good, many are important, but only a few are essential.\" —D. Todd Christofferson",
            "\"Obedience is a choice.\" —L. Tom Perry",
            "\"Only God knows hearts, and so only He can say, in truth, “I know how you feel.”\" —Henry B. Eyring" +
            "\"His love never fails…Nor will the peace He promises ever leave us as we serve others for Him.\" —Henry B. Eyring"};
    int messageCount=textToShow.length;
    int currentIndex;
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DatabaseHandler(this);
        rssFeed = (TextSwitcher) findViewById(R.id.rss);
        Random r = new Random();
        currentIndex = r.nextInt(textToShow.length);

        rssFeed.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView feed = new TextView(MainActivity.this);
                feed.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
                feed.setTypeface(null, Typeface.ITALIC);
                feed.setTextColor(Color.GRAY);
                return feed;
            }
        });

        Animation in = AnimationUtils.loadAnimation(this,android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);

        rssFeed.setInAnimation(in);
        rssFeed.setOutAnimation(out);


        Runnable feedRunnable = new Runnable(){
            @Override
            public void run() {
                currentIndex++;
                if(currentIndex==messageCount)
                    currentIndex=0;
                rssFeed.setText(textToShow[currentIndex]);
                feedHandler.postDelayed(this, 10000);
            }
        };

        feedHandler.postDelayed(feedRunnable, 0);
        final ArrayList<String> activities = new ArrayList<>();

        activities.add("Calendar");
        activities.add("stats");

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        list = (ListView) findViewById(R.id.drawerListView);
        adapter = new ListViewAdapter(this,activities);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //if user clicks on an item, will take object and pass it to next activity
                menuSelect(activities.get(position));
            }
        });
        setupDrawer();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

        // Activate the navigation drawer toggle
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

/*Drawer stuff*/
    private void setupDrawer() {
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Menu");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(getTitle().toString());
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        toggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(toggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed(){
        if(this.drawerLayout.isDrawerOpen(GravityCompat.START)){
            this.drawerLayout.closeDrawer(GravityCompat.START);

        }else {
            super.onBackPressed();
        }
    }

    public void menuSelect(String activity){

        switch(activity){
            case "stats":
                Intent stat = new Intent(this, Stats.class);
                startActivity(stat);
                finish();
                break;
            case "Calendar":
                Intent cal = new Intent(this, Calendar.class);
                startActivity(cal);
                finish();
                break;
            default:
                break;
        }


    }

    public void deleteDB(View view){
        db.endGame();
    }

}
