package com.example.newsgateway;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private NewsReceiver newsReceiver;
    static final String NEWS_BROADCAST_FROM_SERVICE = "NEWS_BROADCAST_FROM_SERVICE";
    static final String MESSAGE_BROADCAST_FROM_SERVICE = "MESSAGE_BROADCAST_FROM_SERVICE";
    static final String ACTION_MSG_TO_SERVICE = "ACTION_MSG_TO_SERVICE";
    static final String ACTION_MSG_TO_MAIN_ACTIVITY = "ACTION_MSG_TO_MAIN_ACTIVITY";
    static final String ACTION_NEWS_STORY = "ACTION_NEWS_STORY";
    static final String STORIES = "STORIES";
    static final String NEWS_DATA = "NEWS_DATA";
    static final String COUNT_DATA = "COUNT_DATA";
    static final String MESSAGE_DATA = "MESSAGE_DATA";
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private Menu opt_menu;
    private ActionBarDrawerToggle mDrawerToggle;
    private ArrayList<Source> sourceList = new ArrayList<>();
    private ArrayList<Story> storyList = new ArrayList<>();
    private HashMap<String, ArrayList<Source>> sourceData = new HashMap<>();
    private List<Fragment> fragments;
    private MyPageAdapter pageAdapter;
    private ViewPager pager;
    private String sSource;
    private String sCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerList = findViewById(R.id.drawer_list);

        Intent intent = new Intent(MainActivity.this, NewsService.class);
        intent.putExtra("ACTION_MSG_TO_SERVICE", NewsService.ACTION_MSG_TO_SERVICE);
        startService(intent);

        newsReceiver = new NewsReceiver();
        IntentFilter filter1 = new IntentFilter(ACTION_MSG_TO_MAIN_ACTIVITY);
        registerReceiver(newsReceiver, filter1);

        // Set up the drawer item click callback method
        mDrawerList.setOnItemClickListener(
                new ListView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        pager.setBackground(null);
                        Source s = sourceList.get(position);
                        sSource = s.getName();
                        Log.d(TAG, "onItemClick: " + s.getID());
                        sendRequest(s.getID());
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

        fragments = new ArrayList<>();


        pageAdapter = new MyPageAdapter(getSupportFragmentManager());
        pager = findViewById(R.id.container);
        pager.setAdapter(pageAdapter);

        // Load the data
        if (sourceData.isEmpty())
            Log.d(TAG, "onCreate: source list size is " + sourceList.size());
            new AsyncNewsSourceLoader(this).execute();


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: ");
        outState.putString("CURRENTCATEGORY", sCategory);
        outState.putParcelableArrayList("SOURCELIST", sourceList);
        Log.d(TAG, "onSaveInstanceState: " + sourceList.size());

        // Call super last
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // Call super first
        super.onRestoreInstanceState(savedInstanceState);
        sCategory = savedInstanceState.getString("CURRENTCATEGORY");
        setTitle(sCategory);

        if(sCategory == null){
            setTitle("News Gateway");
        }

        sourceList = savedInstanceState.getParcelableArrayList("SOURCELIST");
        Log.d(TAG, "onRestoreInstanceState: " + sourceList.size());
//        sourceList.addAll(listIn);
        mDrawerList.setAdapter(null);

        mDrawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_item, sourceList));

        ((ArrayAdapter) mDrawerList.getAdapter()).notifyDataSetChanged();
//
//        textDegreesTop.setText(savedInstanceState.getString("TEXTDEGREESTOP"));
//        textDegreesBottom.setText(savedInstanceState.getString("TEXTDEGREESBOTTOM"));
//        textResult.setText(savedInstanceState.getString("TEXTRESULT"));
//        textConversionHistory.setText(savedInstanceState.getString("TEXTHISTORY"));
//
//        conversionHistory = savedInstanceState.getStringArrayList("ARRAYHISTORY");
//
//        radioValue = savedInstanceState.getInt("RADIOVALUE");
    }

    public void setStories(ArrayList<Story> storyList) {

        Log.d(TAG, "setStories: " + sSource);

        setTitle(sSource);


        for (int i = 0; i < pageAdapter.getCount(); i++)
            pageAdapter.notifyChangeInPosition(i);

        fragments.clear();

        int iStoryListSize = storyList.size();
        if(iStoryListSize > 10){
            iStoryListSize = 10;
        }

        for (int i = 0; i < iStoryListSize; i++) {
            fragments.add(
                    StoryFragment.newInstance(storyList.get(i), i+1, iStoryListSize));
            //pageAdapter.notifyChangeInPosition(i);
        }

        pageAdapter.notifyDataSetChanged();
        pager.setCurrentItem(0);
    }

    @Override
    protected void onResume() {
        newsReceiver = new NewsReceiver();
        IntentFilter filter1 = new IntentFilter(ACTION_MSG_TO_MAIN_ACTIVITY);
        registerReceiver(newsReceiver, filter1);

        Intent intent = new Intent(MainActivity.this, NewsService.class);
        intent.putExtra("ACTION_MSG_TO_SERVICE", NewsService.ACTION_MSG_TO_SERVICE);
        startService(intent);
        super.onResume();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(newsReceiver);
        super.onStop();
    }

    private void sendRequest(String sId) {
        Intent intent = new Intent();
        intent.setAction(NewsService.ACTION_MSG_TO_SERVICE);
        intent.putExtra(NewsService.SOURCE_ID, sId);
        Log.d(TAG, "sendRequest: here");
        sendBroadcast(intent);
    }

    public void updateData(ArrayList<Source> listIn) {

        for (Source source : listIn) {

            if (!sourceData.containsKey(source.getCategory())) {
                sourceData.put(source.getCategory(), new ArrayList<Source>());
            }
            ArrayList<Source> sourceList2 = sourceData.get(source.getCategory());
            if (sourceList2 != null) {
                sourceList2.add(source);
            }
        }
//        Log.d(TAG, "updateData: " + sourceData.get("business"));
//        ArrayList<Source> foo = sourceData.get("business");
//        Source fooSource = foo.get(0);
//        Log.d(TAG, "updateData: " + fooSource.getName());

        sourceData.put("All", listIn);

        ArrayList<String> tempList = new ArrayList<>(sourceData.keySet());
        Collections.sort(tempList);
        for (String s : tempList)
            opt_menu.add(s);

        sourceList.clear();
        sourceList.addAll(listIn);
        mDrawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_item, sourceList));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

    }
    // You need the below to open the drawer when the toggle is clicked
    // Same method is called when an options menu item is selected.

    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            Log.d(TAG, "onOptionsItemSelected: mDrawerToggle " + item);
            return true;
        }

        sCategory = item.getTitle().toString();

        setTitle(sCategory);

        sourceList.clear();
        ArrayList<Source> tempList = sourceData.get(item.getTitle().toString());
        if (tempList != null) {
            sourceList.addAll(tempList);
        }

        ((ArrayAdapter) mDrawerList.getAdapter()).notifyDataSetChanged();
        return super.onOptionsItemSelected(item);

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
            Log.d(TAG, "scoopy: doopy");
            String action = intent.getAction();
            Log.d(TAG, "onReceive action5: " + action);
            if (action == null)
                return;
            switch (action) {
                case ACTION_MSG_TO_MAIN_ACTIVITY:
                    if (intent.hasExtra(STORIES)) {
                        Log.d(TAG, "onReceive stories: ");
//                        storyList = intent.getStringArrayListExtra(STORIES);
                        storyList = intent.getParcelableArrayListExtra(STORIES);

                        setStories(storyList);
                        Log.d(TAG, "onReceiverr: " + storyList.size());
//                        Story story = storyList.get(0);
//                        Log.d(TAG, "onReceive story description: " + story.getDescription());

                        //todo call reDoFragments passing the list of articles (page 5 of documentation)
                        storyList.clear();
                    }
                    break;

                default:
                    Log.d(TAG, "onReceive: Unknown broadcast received");
            }
        }
    }

    //////////////////////////////////////

    private class MyPageAdapter extends FragmentPagerAdapter {
        private long baseId = 0;


        MyPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public long getItemId(int position) {
            // give an ID different from position when position has been changed
            return baseId + position;
        }

        /**
         * Notify that the position of a fragment has been changed.
         * Create a new ID for each position to force recreation of the fragment
         * @param n number of items which have been changed
         */
        void notifyChangeInPosition(int n) {
            // shift the ID returned by getItemId outside the range of all previous fragments
            baseId += getCount() + n;
        }

    }
}

