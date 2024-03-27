package com.example.appdocbao.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.appdocbao.Adapter.RecyclerArticleAdapter;
import com.example.appdocbao.Model.Article;
import com.example.appdocbao.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class RecentlyReadActivity extends AppCompatActivity {
    private RecyclerView rcvRecentlyRead;
    private RecyclerArticleAdapter articleAdapter;
    private ArrayList<Article> listArticle;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recently_read);
        rcvRecentlyRead = findViewById(R.id.rcvRecentlyRead);
//        progressIndicator = findViewById(R.id.progress_bar);
        setupRecycleView();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
//        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        listArticle = new ArrayList<>();
        articleAdapter= new RecyclerArticleAdapter(this,listArticle);
        rcvRecentlyRead.setAdapter(articleAdapter);

        FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();
        String id_User = mAuth.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference("users/"+id_User+"/recently_read");
//        dialog.show();
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listArticle.clear();
                for(DataSnapshot itemSnapShot : snapshot.getChildren()){
                    Article article = itemSnapShot.getValue(Article.class);
                    listArticle.add(article);
                }
                Collections.reverse(listArticle);
                articleAdapter.notifyDataSetChanged();
//                changeInProgress(false);
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });
    }
    void setupRecycleView(){
        rcvRecentlyRead.setLayoutManager(new LinearLayoutManager(this));
        rcvRecentlyRead.setHasFixedSize(true);
        articleAdapter = new RecyclerArticleAdapter(this,listArticle);
        rcvRecentlyRead.setAdapter(articleAdapter);
    }
}