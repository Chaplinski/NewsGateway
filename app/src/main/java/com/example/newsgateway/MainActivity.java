package com.example.newsgateway;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private NewsReceiver newsReceiver;
    static final String NEWS_BROADCAST_FROM_SERVICE = "NEWS_BROADCAST_FROM_SERVICE";
    static final String MESSAGE_BROADCAST_FROM_SERVICE = "MESSAGE_BROADCAST_FROM_SERVICE";
    static final String NEWS_DATA = "NEWS_DATA";
    static final String COUNT_DATA = "COUNT_DATA";
    static final String MESSAGE_DATA = "MESSAGE_DATA";
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private Menu opt_menu;
    private ActionBarDrawerToggle mDrawerToggle;
    ArrayList<Story> storyList = new ArrayList<>();
    private HashMap<String, ArrayList<Story>> storyData = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerList = findViewById(R.id.drawer_list);

        Intent intent = new Intent(MainActivity.this, NewsService.class);
        startService(intent);

        newsReceiver = new NewsReceiver();

        // Set up the drawer item click callback method
        mDrawerList.setOnItemClickListener(
                new ListView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Story s = storyList.get(position);
                        Intent intent = new Intent(MainActivity.this, StoryDetailActivity.class);
                        intent.putExtra(Story.class.getName(), s);
                        startActivity(intent);
                        mDrawerLayout.closeDrawer(mDrawerList);
                    }
                }
        );

        // Create the drawer toggle
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        );

        // Load the data
        if (storyData.isEmpty())
            new AsyncNewsSourceLoader(this).execute();


    }

    public void updateData(ArrayList<Source> listIn) {

//        for (Country c : listIn) {
//            if (c.getSubRegion().isEmpty()) {
//                c.setSubRegion("Unspecified");
//            }
//            if (!countryData.containsKey(c.getSubRegion())) {
//                countryData.put(c.getSubRegion(), new ArrayList<Country>());
//            }
//            ArrayList<Country> cList = countryData.get(c.getSubRegion());
//            if (cList != null) {
//                cList.add(c);
//            }
//        }
//
//        countryData.put("All", listIn);
//
//        ArrayList<String> tempList = new ArrayList<>(countryData.keySet());
//        Collections.sort(tempList);
//        for (String s : tempList)
//            opt_menu.add(s);
//
//
//        countryList.addAll(listIn);
//        mDrawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_item, countryList));
//
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setHomeButtonEnabled(true);
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.opt_menu, menu);
        opt_menu = menu;
        return true;
    }

    // You need the 2 below to make the drawer-toggle work properly:

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    /////////////////////////////////////////////////////////////
    class NewsReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (action == null)
                return;
            switch (action) {
                case NEWS_BROADCAST_FROM_SERVICE:
                    Story newStory = null;
                    int count = 0;

                    if (intent.hasExtra(NEWS_DATA))
                        newStory = (Story) intent.getSerializableExtra(NEWS_DATA);

                    if (intent.hasExtra(COUNT_DATA))
                        count = intent.getIntExtra(COUNT_DATA, 0);

//                    if (newStory != null) {
//                        ((TextView) findViewById(R.id.textView)).setText(
//                                String.format(Locale.getDefault(),
//                                        "%d)  %s", count, newStory.toString()));
//                    }

                    break;
                case MESSAGE_BROADCAST_FROM_SERVICE:
                    String data = "";
                    if (intent.hasExtra(MESSAGE_DATA))
                        data = intent.getStringExtra(MESSAGE_DATA);
//                    ((TextView) findViewById(R.id.textView)).setText(data);
                    break;
                default:
                    Log.d(TAG, "onReceive: Unknown broadcast received");
            }
        }
    }
}
