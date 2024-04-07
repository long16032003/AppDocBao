package com.example.appdocbao.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appdocbao.Adapter.RecyclerArticleAdapter;
import com.example.appdocbao.Model.Article;
import com.example.appdocbao.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TrendFragment extends Fragment {
    private RecyclerView rcvLikestArticle, rcv;
    private RecyclerArticleAdapter articleAdapter;
    private ArrayList<Article> listArticle;
    ValueEventListener eventListener;
    private boolean isListLoaded = false;
    ProgressBar progressbar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trend, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressbar = view.findViewById(R.id.progressbar);
        rcvLikestArticle = view.findViewById(R.id.recyclerView);
        DatabaseReference likesRef = FirebaseDatabase.getInstance().getReference("likes");

        listArticle = new ArrayList<>();
        articleAdapter = new RecyclerArticleAdapter(getContext(), listArticle, 5);
        rcvLikestArticle.setAdapter(articleAdapter);
        rcvLikestArticle.setLayoutManager(new LinearLayoutManager(getContext()));

        likesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, Integer> likeCounts = new HashMap<>();

                for (DataSnapshot likeSnapshot : snapshot.getChildren()) {
                    String articleId = likeSnapshot.getKey();
                    int likeCount = (int) likeSnapshot.getChildrenCount();
                    likeCounts.put(articleId, likeCount);
                }

                List<Map.Entry<String, Integer>> sortedArticles = new ArrayList<>(likeCounts.entrySet());
                Collections.sort(sortedArticles, (entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

                for (Map.Entry<String, Integer> entry : sortedArticles) {
                    String articleId = entry.getKey();
                    Integer likeCount = entry.getValue();

                    DatabaseReference articleRef = FirebaseDatabase.getInstance().getReference("articles").child(articleId);
                    articleRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Article article = snapshot.getValue(Article.class);
                            if (article != null) {
                                listArticle.add(article);

                            }
                            articleAdapter.notifyDataSetChanged();
                            isListLoaded = true;

                            if (isListLoaded) {
                                progressbar.setVisibility(View.GONE);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Xử lý lỗi nếu có
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu có
            }
        });

        DatabaseReference arc = FirebaseDatabase.getInstance().getReference("articles");
        arc.orderByChild("view").limitToLast(10).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Article> topViews = new ArrayList<>();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Article article = dataSnapshot.getValue(Article.class);
                    topViews.add(article);
                }
                Collections.sort(topViews, new Comparator<Article>() {
                    @Override
                    public int compare(Article article1, Article article2) {
                        return Integer.compare(article2.getView(), article1.getView());
                    }
                });
                rcv= view.findViewById(R.id.rcvView);
                RecyclerArticleAdapter topViewedAritclesAdapter = new RecyclerArticleAdapter(getContext(),topViews,5);
                rcv.setAdapter(topViewedAritclesAdapter);
//                rcv.setLayoutManager(new LinearLayoutManager(getContext()));
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                rcv.setLayoutManager(linearLayoutManager);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}