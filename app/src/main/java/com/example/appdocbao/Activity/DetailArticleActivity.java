package com.example.appdocbao.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.*;
import java.text.SimpleDateFormat;

import com.bumptech.glide.Glide;
import com.example.appdocbao.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailArticleActivity extends AppCompatActivity {

    ImageView backMainActivity;
    TextView detailTitle, detailContent, detailAuthor, detailDate;
    ImageView detailImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_article);
        backMainActivity = findViewById(R.id.imageView6);
        backMainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailArticleActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

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
        BottomNavigationView navigationView = findViewById(R.id.bottom_nav);
        navigationView.setOnItemReselectedListener(new NavigationBarView.OnItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.action_like) {
                    //============================= Event khi click vào nút Like =====================
//                    String id_Article = bundle.getString("Id");
//                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("articles").child(id_Article);
//                    databaseReference.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            int count_like = dataSnapshot.child("likes").getValue(Integer.class) + 1;
//                            databaseReference.child("likes").setValue(count_like)
//                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                        @Override
//                                        public void onSuccess(Void aVoid) {
//                                            // Đã cập nhật thành công lượt like vào Firebase
//                                            Toast.makeText(DetailArticleActivity.this, "Like", Toast.LENGTH_SHORT).show();
//                                        }
//                                    })
//                                    .addOnFailureListener(new OnFailureListener() {
//                                        @Override
//                                        public void onFailure(@NonNull Exception e) {
//                                            // Xảy ra lỗi khi cập nhật lượt like vào Firebase
//                                        }
//                                    });
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//                            //Xử lí lỗi
//                        }
//                    });
                } else if (itemId == R.id.action_dislike) {
                    Toast.makeText(DetailArticleActivity.this, "Dislike", Toast.LENGTH_SHORT).show();
                } else if (itemId == R.id.action_saved) {
                    Toast.makeText(DetailArticleActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                } else if (itemId == R.id.action_share) {
                    Toast.makeText(DetailArticleActivity.this, "Share", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
