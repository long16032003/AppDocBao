package com.example.appdocbao.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.appdocbao.Adapter.RecentlyReadAdapter;
import com.example.appdocbao.Model.Article;
import com.example.appdocbao.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SavedArticlesActivity extends AppCompatActivity {

    private RecyclerView rcvRecentlyRead;
    private RecentlyReadAdapter recentlyReadAdapter;
    private ArrayList<Article> listArticle;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    ImageView deleteRecentlyRead;
    ImageView backMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_articles);
        rcvRecentlyRead = findViewById(R.id.rcvRecentlyRead);
        deleteRecentlyRead = findViewById(R.id.deleteRecentlyRead);
        backMain = findViewById(R.id.backMain);
//        progressIndicator = findViewById(R.id.progress_bar);
        setupRecycleView();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
//        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        listArticle = new ArrayList<>();
        recentlyReadAdapter= new RecentlyReadAdapter(this,listArticle);
        rcvRecentlyRead.setAdapter(recentlyReadAdapter);

        FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();
        String id_User = mAuth.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference("saved_articles/"+id_User);
//        dialog.show();
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listArticle.clear();
                for(DataSnapshot itemSnapShot : snapshot.getChildren()){
                    Article article = itemSnapShot.getValue(Article.class);
                    listArticle.add(article);
                }
                Collections.sort(listArticle, new Comparator<Article>() {
                    @Override
                    public int compare(Article article1, Article article2) {
                        // So sánh thời gian giữa hai bài báo
                        return Long.compare(article2.getTimestamp(), article1.getTimestamp());
                    }
                });
                recentlyReadAdapter.notifyDataSetChanged();
//                changeInProgress(false);
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });
        deleteRecentlyRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SavedArticlesActivity.this);
                builder.setTitle("Xóa tất cả bài báo đã lưu");
                builder.setMessage("Bạn có chắc chắn muốn xóa tất cả bài báo đã lưu không?");
                builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Xử lý logic xóa danh sách saved_article
                        FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();
                        String id_User = mAuth.getUid();

                        Task<Void> databaseReference =  FirebaseDatabase.getInstance().getReference("saved_articles/"+id_User)
                                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            // Xóa dữ liệu thành công
                                            Toast.makeText(SavedArticlesActivity.this, "Đã xóa tất cả tin đã lưu", Toast.LENGTH_SHORT).show();
                                        } else {
                                            // Xóa dữ liệu thất bại
                                            Toast.makeText(SavedArticlesActivity.this, "Lỗi khi xóa dữ liệu", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                        dialog.dismiss();
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
        });
        backMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    void setupRecycleView(){
        rcvRecentlyRead.setLayoutManager(new LinearLayoutManager(this));
        rcvRecentlyRead.setHasFixedSize(true);
        recentlyReadAdapter = new RecentlyReadAdapter(this,listArticle);
        rcvRecentlyRead.setAdapter(recentlyReadAdapter);
    }
}