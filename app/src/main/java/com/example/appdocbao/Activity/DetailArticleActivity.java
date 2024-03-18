package com.example.appdocbao.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.appdocbao.R;
import java.util.*;
import java.text.SimpleDateFormat;

public class DetailArticleActivity extends AppCompatActivity {

    TextView detailTitle, detailContent, detailAuthor, detailDate;
    ImageView detailImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_article);

        detailTitle = findViewById(R.id.detailTitle);
        detailContent = findViewById(R.id.detailContent);
        detailImage = findViewById(R.id.detailImage);
        detailAuthor = findViewById(R.id.detailAuthor);
        detailDate = findViewById(R.id.detailDate);


        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            detailTitle.setText(bundle.getString("Title"));
            detailContent.setText(bundle.getString("Content"));
            detailAuthor.setText(bundle.getString("Author"));

            long timestamp = bundle.getLong("Date");
            Date date = new Date(timestamp);
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy", Locale.getDefault());
            String formattedDateTime = dateFormat.format(date);
            detailDate.setText(formattedDateTime);

            Glide.with(this).load(bundle.getString("Image")).into(detailImage);
        }
    }
}