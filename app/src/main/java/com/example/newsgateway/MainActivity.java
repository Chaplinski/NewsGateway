package com.example.newsgateway;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private NewsReceiver newsReceiver;
    static final String NEWS_BROADCAST_FROM_SERVICE = "NEWS_BROADCAST_FROM_SERVICE";
    static final String MESSAGE_BROADCAST_FROM_SERVICE = "MESSAGE_BROADCAST_FROM_SERVICE";
    static final String NEWS_DATA = "NEWS_DATA";
    static final String COUNT_DATA = "COUNT_DATA";
    static final String MESSAGE_DATA = "MESSAGE_DATA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ArrayList<Story> storyList = new ArrayList<>();

        Intent intent = new Intent(MainActivity.this, NewsService.class);
        startService(intent);

        newsReceiver = new NewsReceiver();

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
                    Fruit newFruit = null;
                    int count = 0;

                    if (intent.hasExtra(NEWS_DATA))
                        newFruit = (Fruit) intent.getSerializableExtra(NEWS_DATA);

                    if (intent.hasExtra(COUNT_DATA))
                        count = intent.getIntExtra(COUNT_DATA, 0);

                    if (newFruit != null) {
                        ((TextView) findViewById(R.id.textView)).setText(
                                String.format(Locale.getDefault(),
                                        "%d)  %s", count, newFruit.toString()));
                    }

                    break;
                case MESSAGE_BROADCAST_FROM_SERVICE:
                    String data = "";
                    if (intent.hasExtra(MESSAGE_DATA))
                        data = intent.getStringExtra(MESSAGE_DATA);
                    ((TextView) findViewById(R.id.textView)).setText(data);
                    break;
                default:
                    Log.d(TAG, "onReceive: Unknown broadcast received");
            }
        }
    }
}
