package com.example.newsgateway;

import android.content.Intent;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class StoryDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_story);

        Intent intent = getIntent();
//        if (intent.hasExtra(Story.class.getName())) {
            Story s = (Story) intent.getSerializableExtra(Story.class.getName());

            TextView headline = findViewById(R.id.textViewHeadline);
            headline.setText(s.getTitle());

            TextView date = findViewById(R.id.textViewDate);
            date.setText(s.getDate());

            TextView author = findViewById(R.id.textViewAuthor);
            author.setText(s.getAuthor());

            TextView story = findViewById(R.id.textViewStory);
            story.setText(s.getDescription());

        TextView textViewCount = findViewById(R.id.textViewCount);
        textViewCount.setText(s.getDescription());

//            ImageView image = findViewById(R.id.imageView);
//            image.setImageBitmap(s.getImage());

//        }

    }
}
