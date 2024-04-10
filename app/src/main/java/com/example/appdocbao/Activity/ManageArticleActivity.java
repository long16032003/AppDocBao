package com.example.appdocbao.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.example.appdocbao.Adapter.ManageArticleAdapter;
import com.example.appdocbao.Model.Article;
import com.example.appdocbao.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class ManageArticleActivity extends AppCompatActivity {
    private RecyclerView rcvManageArticle;
    private ManageArticleAdapter manageArticleAdapter;
    private ArrayList<Article> listArticle;
    DatabaseReference articleReference, savedArticleReference;
    ValueEventListener eventListener;
    ImageView backMain;
    ScrollView mainView;
    LinearLayout empty;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_article);

        rcvManageArticle = findViewById(R.id.rcvManageArticle);
        backMain = findViewById(R.id.backMain);

        mainView = findViewById(R.id.mainView);
        empty = findViewById(R.id.empty);

        setupRecycleView();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
//        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        listArticle = new ArrayList<>();
        manageArticleAdapter= new ManageArticleAdapter(this,listArticle);
        rcvManageArticle.setAdapter(manageArticleAdapter);

        FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();
        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);
         String userId = "";
        if(mAuth != null){
            userId = mAuth.getUid();
        }
        else if(googleSignInAccount!=null){
            userId = googleSignInAccount.getId();
        }
        final String id_User = userId;

        articleReference = FirebaseDatabase.getInstance().getReference("articles");
//        dialog.show();
        eventListener = articleReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listArticle.clear();
                for(DataSnapshot itemSnapShot : snapshot.getChildren()){
                    Article article = itemSnapShot.getValue(Article.class);
                    if (article != null && article.getIdUserPost() != null && article.getIdUserPost().equals(id_User)) {
                        listArticle.add(article);
                    }
                }
                manageArticleAdapter.notifyDataSetChanged();
//                changeInProgress(false);
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });
        backMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if(listArticle.isEmpty()){
            empty.setVisibility(View.VISIBLE);
        }{
            empty.setVisibility(View.GONE);
        }
    }
    void setupRecycleView(){
        rcvManageArticle.setLayoutManager(new LinearLayoutManager(this));
        rcvManageArticle.setHasFixedSize(true);
        manageArticleAdapter = new ManageArticleAdapter(this,listArticle);
        rcvManageArticle.setAdapter(manageArticleAdapter);
    }
}