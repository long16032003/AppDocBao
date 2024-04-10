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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.appdocbao.Adapter.RecentlyReadAdapter;
import com.example.appdocbao.Model.Article;
import com.example.appdocbao.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
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
import java.util.List;

public class SavedArticlesActivity extends AppCompatActivity {

    private RecyclerView rcvRecentlyRead;
    private RecentlyReadAdapter recentlyReadAdapter;
    private ArrayList<Article> listArticle;
    DatabaseReference savedArticleRef;
    ValueEventListener eventListener;
    ImageView deleteRecentlyRead;
    ImageView backMain;
    LinearLayout empty;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_articles);
        rcvRecentlyRead = findViewById(R.id.rcvRecentlyRead);
        deleteRecentlyRead = findViewById(R.id.deleteRecentlyRead);
        backMain = findViewById(R.id.backMain);
        empty = findViewById(R.id.empty);
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
        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);
        final String id_User = mAuth!=null ? mAuth.getUid() : (googleSignInAccount != null ? googleSignInAccount.getId() : "" );

        savedArticleRef = FirebaseDatabase.getInstance().getReference("saved_articles/"+id_User);
        eventListener = savedArticleRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> savedArticleIds = new ArrayList<>();

                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    String articleId = childSnapshot.getKey();
                    savedArticleIds.add(articleId);
                }
                // Truy cập vào nút "articles" và lấy thông tin của từng bài báo
                DatabaseReference articlesRef = FirebaseDatabase.getInstance().getReference("articles");
                articlesRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            String articleId = childSnapshot.getKey();
                            if (savedArticleIds.contains(articleId)) {
                                Article article = childSnapshot.getValue(Article.class);
                                if (article != null) {
                                    listArticle.add(article);
                                }
                            }
                        }
                        Collections.sort(listArticle, new Comparator<Article>() {
                            @Override
                            public int compare(Article article1, Article article2) {
                                // So sánh thời gian giữa hai bài báo
                                return Long.compare(article2.getTimestamp(), article1.getTimestamp());
                            }
                        });
                        recentlyReadAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Xử lý lỗi nếu có
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });
        //
        if(listArticle.isEmpty()){
            empty.setVisibility(View.VISIBLE);
        }{
            empty.setVisibility(View.GONE);
        }
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

                        Task<Void> savedArticleRef =  FirebaseDatabase.getInstance().getReference("saved_articles/"+id_User)
                                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            // Xóa dữ liệu thành công
                                            Toast.makeText(SavedArticlesActivity.this, "Đã xóa tất cả tin đã lưu", Toast.LENGTH_SHORT).show();
                                            recreate();
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