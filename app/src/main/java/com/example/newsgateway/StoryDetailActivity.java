package com.example.newsgateway;

import android.content.Intent;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

public class StoryDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.story_layout);

        Intent intent = getIntent();
//        if (intent.hasExtra(Story.class.getName())) {
            Story s = (Story) intent.getSerializableExtra(Story.class.getName());

            TextView headline = findViewById(R.id.textViewHeadline);
            headline.setText(s.getHeadline());

            TextView date = findViewById(R.id.textViewDate);
            date.setText(s.getDate());

            TextView author = findViewById(R.id.textViewAuthor);
            author.setText(s.getAuthor());

            TextView story = findViewById(R.id.textViewStory);
            story.setText(s.getStory());

            ImageView image = findViewById(R.id.imageView);
            image.setImageBitmap(s.getImage());

//        }

    }
}
