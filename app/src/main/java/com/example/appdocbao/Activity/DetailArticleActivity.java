package com.example.appdocbao.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;


import java.util.*;
import java.text.SimpleDateFormat;


public class DetailArticleActivity extends AppCompatActivity {

    ImageView backMainActivity;
    TextView detailTitle, detailContent, detailAuthor, detailDate, countLike;
    ImageView detailImage, likeArticle ,dislikeArticle, saveArticle, shareArticle;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    DatabaseReference likeReference = FirebaseDatabase.getInstance().getReference("likes");;
    boolean isLike = false, canDislike = true, isSaved = false; // ban đầu chưa active
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_article);
        backMainActivity = findViewById(R.id.imageView6);
        shareArticle = findViewById(R.id.shareArticle);
        likeArticle = findViewById(R.id.likeArticle);
        dislikeArticle = findViewById(R.id.dislikeArticle);
        saveArticle = findViewById(R.id.saveArticle);
        countLike = findViewById(R.id.countLike);
        // Check status like, dislike, save

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
            // Sử dụng Glide để tải hình ảnh
//            if (!isDestroyed()) {
            Glide.with(this)
                    .load(bundle.getString("Image"))
                    .into(detailImage);
//            }
            String id_Article = bundle.getString("Id");
            getLikebuttonStatus(id_Article);
            likeArticle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(checkLogined()){
                        isLike = true;
                        likeReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(isLike == true){
                                    if(snapshot.child(id_Article).hasChild(user.getUid())){
                                        likeReference.child(id_Article).removeValue();
                                        isLike = false;
                                    }else{
                                        likeReference.child(id_Article).child(user.getUid()).setValue(true);
                                        isLike = false;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }else{
                        AlertDialog(DetailArticleActivity.this);
                    }
                }
            });
        }
        backMainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        shareArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkLogined()){
                    Intent sendIntent = new Intent(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                }else{
                    AlertDialog(DetailArticleActivity.this);
                }
            }
        });


        dislikeArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkLogined()){
                    if (canDislike) {
//                        ChangeImageDisLike(canDislike);
//                        IncreaseDisLikeArticle();
//                        AddIdArticleToDisLikeUser();
//                        canDislike = false;
//                    } else {
//                        ChangeImageDisLike(canDislike);
//                        DecreaseDisLikeArticle();
//                        DeleteIdArticleToDisLikeUser();
//                        canDislike = true;
                    }
                }else{
                    AlertDialog(DetailArticleActivity.this);
                }
            }
        });
        saveArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSaved = !isSaved;
                if(checkLogined()){
//                    if (isSaved) {
//                        ChangeImageSave(isSaved);
//                        AddIdArticleToSaveUser();
//                    } else {
//                        ChangeImageSave(isSaved);
//                        DeleteIdArticleToSaveUser();
//                    }
                }else{
                    AlertDialog(DetailArticleActivity.this);
                }
            }
        });
    }

    private void getLikebuttonStatus(String id_Article){

        likeReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(checkLogined()){
                    if(snapshot.child(id_Article).hasChild(user.getUid())){ // User đã like
                        int likeCount = (int) snapshot.child(id_Article).getChildrenCount();
                        countLike.setText(""+likeCount);
                        likeArticle.setImageResource(R.drawable.like_active);
                    } else {    // User chưa like
                        int likeCount = (int) snapshot.child(id_Article).getChildrenCount();
                        countLike.setText(""+likeCount);
                        likeArticle.setImageResource(R.drawable.thumbs_up_line_icon);
                    }
                }else{ // Chưa đăng nhập
                    int likeCount = (int) snapshot.child(id_Article).getChildrenCount();
                    countLike.setText(""+likeCount);
                    likeArticle.setImageResource(R.drawable.thumbs_up_line_icon);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void AlertDialog(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Lưu ý");
        builder.setMessage("Bạn cần đăng nhập để có thể sử dụng đươc tính năng này. Bạn có muốn chuyển đến trang đăng nhập ngay bây giờ ?");
        builder.setPositiveButton("Đăng nhập", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(context, LoginActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public boolean checkLogined(){
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            return true;
        }else{
            return false;
        }
    }
}
