package com.example.newsgateway;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoryFragment extends Fragment {


    public StoryFragment() {
        // Required empty public constructor
    }


    public static StoryFragment newInstance(Story story,
                                              int index, int max)
    {
        StoryFragment s = new StoryFragment();
        Bundle bdl = new Bundle(1);
        bdl.putSerializable("STORY_DATA", story);
        bdl.putSerializable("INDEX", index);
        bdl.putSerializable("TOTAL_COUNT", max);
        s.setArguments(bdl);
        return s;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragment_layout = inflater.inflate(R.layout.fragment_story, container, false);

        Bundle args = getArguments();
        if (args != null) {
            final Story currentStory = (Story) args.getSerializable("STORY_DATA");
            if (currentStory == null) {
                return null;
            }
            int index = args.getInt("INDEX");
            int total = args.getInt("TOTAL_COUNT");

            TextView textViewHeadline = fragment_layout.findViewById(R.id.textViewHeadline);
            textViewHeadline.setText(currentStory.getTitle());
            TextView textViewDate = fragment_layout.findViewById(R.id.textViewDate);
            textViewDate.setText(currentStory.getDate());

            TextView textViewAuthor = fragment_layout.findViewById(R.id.textViewAuthor);
            if(!(currentStory.getAuthor().equals(null) || currentStory.getAuthor().equals("null"))) {
                textViewAuthor.setText(currentStory.getAuthor());
            } else {
                textViewAuthor.setText("");
            }

            TextView textViewStory = fragment_layout.findViewById(R.id.textViewStory);
            if(!(currentStory.getDescription().equals(null) || currentStory.getDescription().equals("null"))) {
                textViewStory.setText(currentStory.getDescription());
            } else {
                textViewStory.setText("");
            }

            TextView textViewCount = fragment_layout.findViewById(R.id.textViewCount);
            textViewCount.setText(index + " of " + total);

            ImageView imageView = fragment_layout.findViewById(R.id.imageView);
            if(!currentStory.getUrlToImage().isEmpty()) {
                Picasso.get().load(currentStory.getUrlToImage()).fit().into(imageView);
            } else {
                imageView.setVisibility(View.GONE);
            }
//
//
//            ImageView imageView = fragment_layout.findViewById(R.id.imageView);
//            imageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

//            imageView.setImageDrawable(currentCountry.getDrawable());
//            imageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    clickFlag(currentCountry.getName());
//                }
//            });
            return fragment_layout;
        } else {
            return null;
        }
    }



//    public void clickFlag(String name) {
//
//        Uri mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(name));
//
//        Intent intent = new Intent(Intent.ACTION_VIEW, mapUri);
//        intent.setPackage("com.google.android.apps.maps");
//        startActivity(intent);
//
//    }


}
