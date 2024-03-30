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


import java.util.*;
import java.text.SimpleDateFormat;


public class DetailArticleActivity extends AppCompatActivity {

    ImageView backMainActivity;
    TextView detailTitle, detailContent, detailAuthor, detailDate;
    ImageView detailImage, likeArticle ,dislikeArticle, saveArticle, shareArticle;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    boolean canLike = true, canDislike = true, isSaved = false; // ban đầu chưa active
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_article);
        backMainActivity = findViewById(R.id.imageView6);
        shareArticle = findViewById(R.id.shareArticle);
        likeArticle = findViewById(R.id.likeArticle);
        dislikeArticle = findViewById(R.id.dislikeArticle);
        saveArticle = findViewById(R.id.saveArticle);
        // Check status like, dislike, save

        backMainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = getIntent().getExtras();
                Intent intent = null;
                if(bundle.getBoolean("NewsFragment")){
                    intent = new Intent(DetailArticleActivity.this,MainActivity.class);
                }else if(bundle.getBoolean("RecentlyRead")){
                    intent = new Intent(DetailArticleActivity.this,RecentlyReadActivity.class);
                }
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
            // Sử dụng Glide để tải hình ảnh
//            if (!isDestroyed()) {
                Glide.with(this)
                        .load(bundle.getString("Image"))
                        .into(detailImage);
//            }
        }
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

        likeArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkLogined()){
                    if (canLike) {
                        ChangeImageLike(canLike);
                        IncreaseLikeArticle();
                        AddIdArticleToLikeUser();
                        canLike = false;
                    } else {
                        ChangeImageLike(canLike);
                         DecreaseLikeArticle();
                         DeleteIdArticleToLikeUser();
                        canLike = true;
                    }
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
                        ChangeImageDisLike(canDislike);
                        IncreaseDisLikeArticle();
                        AddIdArticleToDisLikeUser();
                        canDislike = false;
                    } else {
                        ChangeImageDisLike(canDislike);
                        DecreaseDisLikeArticle();
                        DeleteIdArticleToDisLikeUser();
                        canDislike = true;
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
                    if (isSaved) {
                        ChangeImageSave(isSaved);
                        AddIdArticleToSaveUser();
                    } else {
                        ChangeImageSave(isSaved);
                        DeleteIdArticleToSaveUser();
                    }
                }else{
                    AlertDialog(DetailArticleActivity.this);
                }
            }
        });
    }



    private void ChangeImageLike(boolean canLike) {
        likeArticle.setImageResource(canLike ? R.drawable.like_active : R.drawable.thumbs_up_line_icon);
    }
    private void ChangeImageDisLike(boolean canDislike) {
        dislikeArticle.setImageResource(canDislike ? R.drawable.dislike_active : R.drawable.thumbs_down_line_icon);
    }
    private void ChangeImageSave(boolean isSaved) {
        saveArticle.setImageResource(isSaved ? R.drawable.saved_active : R.drawable.saved_bookmark_icon);
    }
    //Truy cập đến firebase của like
    private void IncreaseLikeArticle() {
        Bundle bundle = getIntent().getExtras();
        String id_Article = bundle.getString("Id");
        DatabaseReference likeRef = FirebaseDatabase.getInstance().getReference("articles/"+ id_Article);
        likeRef.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                // Lấy giá trị hiện tại của like
                Integer currentLikes = mutableData.child("like").getValue(Integer.class);

                if (currentLikes != null) {
                    // Tăng giá trị like lên 1
                    mutableData.child("like").setValue(currentLikes + 1);
                }

                // Trả về giá trị mới của like
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean committed, @Nullable DataSnapshot dataSnapshot) {
                if (committed) {
                    // Tăng giá trị like thành công
                    // Thực hiện các hành động phụ thuộc vào việc tăng like ở đây
                } else {
                    // Tăng giá trị like thất bại
                    // Xử lý lỗi nếu cần
                }
            }
        });

    }
    private void DecreaseLikeArticle() {
    }
    private void AddIdArticleToLikeUser() {
    }
    private void DeleteIdArticleToLikeUser(){
    }
    //Truy cập đến firebase của dislike
    private void IncreaseDisLikeArticle() {
//        Bundle bundle = getIntent().getExtras();
//        String id_Article = bundle.getString("Id");
//        DatabaseReference likeRef = FirebaseDatabase.getInstance().getReference("articles/"+ id_Article);
//        likeRef.runTransaction(new Transaction.Handler() {
//            @NonNull
//            @Override
//            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
//                // Lấy giá trị hiện tại của like
//                Integer currentLikes = mutableData.child("like").getValue(Integer.class);
//
//                if (currentLikes != null) {
//                    // Tăng giá trị like lên 1
//                    mutableData.child("like").setValue(currentLikes + 1);
//                }
//
//                // Trả về giá trị mới của like
//                return Transaction.success(mutableData);
//            }
//
//            @Override
//            public void onComplete(@Nullable DatabaseError databaseError, boolean committed, @Nullable DataSnapshot dataSnapshot) {
//                if (committed) {
//                    // Tăng giá trị like thành công
//                    // Thực hiện các hành động phụ thuộc vào việc tăng like ở đây
//                } else {
//                    // Tăng giá trị like thất bại
//                    // Xử lý lỗi nếu cần
//                }
//            }
//        });

    }
    private void DecreaseDisLikeArticle() {
    }
    private void AddIdArticleToDisLikeUser() {
    }
    private void DeleteIdArticleToDisLikeUser(){
    }
    // Truy cập đến firebase của save
    private void AddIdArticleToSaveUser() {
    }
    private void DeleteIdArticleToSaveUser(){
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
