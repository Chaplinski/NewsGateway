package com.example.newsgateway;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class NewsService extends Service {

    private static final String TAG = "CountService";
    private boolean running = true;
    static final String SOURCE_REQUEST = "SOURCE_REQUEST";
    static final String SOURCE_ID = "SOURCE_ID";
    static final String ACTION_MSG_TO_SERVICE = "ACTION_MSG_TO_SERVICE";
//    private final ArrayList<Fruit> fruitList = new ArrayList<>();
    private int count = 1;
    private ServiceReceiver serviceReceiver;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

//        if (intent.hasExtra(ACTION_MSG_TO_SERVICE)) {
            //register a ServiceReceiver broadcast receiver object using the intent filter
            serviceReceiver = new ServiceReceiver();
            IntentFilter filter1 = new IntentFilter(ACTION_MSG_TO_SERVICE);
            registerReceiver(serviceReceiver, filter1);
//        }


        //Creating new thread for my service
        //ALWAYS write your long running tasks in a separate thread, to avoid ANR

        new Thread(new Runnable() {
            @Override
            public void run() {

                while (running) {

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // Get random index
//                    int index = (int) (Math.random() * fruitList.size());
//                    sendFruit(fruitList.get(index)); // Send fruit at index
                }

                sendMessage("Service Thread Stopped");


                Log.d(TAG, "run: Ending loop");
            }
        }).start();


        return Service.START_STICKY;
    }

    private void sendNews() {
//    private void sendNews(Fruit fruitToSend) {
        Intent intent = new Intent();
        intent.setAction(MainActivity.NEWS_BROADCAST_FROM_SERVICE);
//        intent.putExtra(MainActivity.NEWS_DATA, fruitToSend);
        intent.putExtra(MainActivity.COUNT_DATA, count++);

        sendBroadcast(intent);
    }

    private void sendMessage(String msg) {
        Intent intent = new Intent();
        intent.setAction(MainActivity.MESSAGE_BROADCAST_FROM_SERVICE);
        intent.putExtra(MainActivity.MESSAGE_DATA, msg);
        sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        sendMessage("Service Destroyed");
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
            switch (action) {
                case SOURCE_REQUEST:
                    String sId = "";
                    if (intent.hasExtra(SOURCE_ID)) {
                        sId = intent.getStringExtra(SOURCE_ID);
                    }
                    Log.d(TAG, "onReceive: " + sId);
//                    Fruit newFruit = null;
//                    int count = 0;
//

//
//                    if (intent.hasExtra(COUNT_DATA))
//                        count = intent.getIntExtra(COUNT_DATA, 0);
//
//                    if (newFruit != null) {
//                        ((TextView) findViewById(R.id.textView)).setText(
//                                String.format(Locale.getDefault(),
//                                        "%d)  %s", count, newFruit.toString()));
//                    }

                    break;
//                case MESSAGE_BROADCAST_FROM_SERVICE:
//                    String data = "";
//                    if (intent.hasExtra(MESSAGE_DATA))
//                        data = intent.getStringExtra(MESSAGE_DATA);
//                    ((TextView) findViewById(R.id.textView)).setText(data);
//                    break;
                default:
                    Log.d(TAG, "onReceive: Unknown broadcast received");
            }
        }
    }
}
