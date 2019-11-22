package com.example.newsgateway;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class NewsService extends Service {

    private static final String TAG = "CountService";
    private boolean running = true;
    static final String SOURCE_REQUEST = "SOURCE_REQUEST";
    static final String SOURCE_ID = "SOURCE_ID";
    static final String ACTION_MSG_TO_SERVICE = "ACTION_MSG_TO_SERVICE";
    private final ArrayList<Story> storyList = new ArrayList<>();
    private int count = 1;
    private ServiceReceiver serviceReceiver;
    private String sSourceId = "";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate(){
        serviceReceiver = new ServiceReceiver();
        IntentFilter filter1 = new IntentFilter(ACTION_MSG_TO_SERVICE);
        registerReceiver(serviceReceiver, filter1);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

//        if (intent.hasExtra(ACTION_MSG_TO_SERVICE)) {
            //register a ServiceReceiver broadcast receiver object using the intent filter
//            serviceReceiver = new ServiceReceiver();
//            IntentFilter filter1 = new IntentFilter(ACTION_MSG_TO_SERVICE);
//            registerReceiver(serviceReceiver, filter1);
        Log.d(TAG, "onStartCommand: receiver registered");
//        }


        //Creating new thread for my service
        //ALWAYS write your long running tasks in a separate thread, to avoid ANR

        new Thread(new Runnable() {
            @Override
            public void run() {

                while (running) {

                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(storyList.size() > 0){
                        Log.d(TAG, "run forrest: ");
                        //run async task retrieving

                        Intent intent = new Intent();
                        intent.setAction(MainActivity.ACTION_MSG_TO_MAIN_ACTIVITY);
                        intent.putParcelableArrayListExtra(MainActivity.STORIES, storyList);
//                        Log.d(TAG, "sendRequest: storyList " + MainActivity.ACTION_MSG_TO_MAIN_ACTIVITY);
                        sendBroadcast(intent);

                        storyList.clear();
                    }
                    // Get random index
//                    int index = (int) (Math.random() * fruitList.size());
//                    sendFruit(fruitList.get(index)); // Send fruit at index
                }

//                sendMessage("Service Thread Stopped");


                Log.d(TAG, "run: Ending loop");
            }
        }).start();


        return Service.START_STICKY;
    }

//    private void sendMessage(String msg) {
//        Intent intent = new Intent();
//        intent.setAction(MainActivity.MESSAGE_BROADCAST_FROM_SERVICE);
//        intent.putExtra(MainActivity.MESSAGE_DATA, msg);
//        sendBroadcast(intent);
//    }

    @Override
    public void onDestroy() {
        unregisterReceiver(serviceReceiver);
//        sendMessage("Service Destroyed");
        running = false;
        super.onDestroy();
    }

    /////////////////////////////////////////////////////////////
    class ServiceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive: received");
            String action = intent.getAction();
            if (action == null)
                return;
            Log.d(TAG, "onReceive action: " + action);
            switch (action) {
                case ACTION_MSG_TO_SERVICE:
                    if (intent.hasExtra(SOURCE_ID)) {
                        sSourceId = intent.getStringExtra(SOURCE_ID);
                    }
                    Log.d(TAG, "onReceive3: " + sSourceId);
                    //call async story task
                    new AsyncStoryLoader(this, sSourceId).execute();

                    break;
                default:
                    Log.d(TAG, "onReceive: Unknown broadcast received");
            }
        }

        public void updateData(ArrayList<Story> listIn) {

            Log.d(TAG, "updateData4: " + listIn.size());
            for(int i = 0; i < listIn.size(); i++){
                storyList.add(listIn.get(i));
            }

        }
    }
}
