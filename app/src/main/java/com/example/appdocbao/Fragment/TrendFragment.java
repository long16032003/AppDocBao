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


public class TrendFragment extends Fragment {
    private RecyclerView rcvArticle;
    private RecyclerArticleAdapter articleAdapter;
    private ArrayList<Article> listArticle;
    DatabaseReference databaseReference;
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
        rcvArticle = view.findViewById(R.id.recyclerView);
        databaseReference = FirebaseDatabase.getInstance().getReference("articles");

        listArticle = new ArrayList<>();
        articleAdapter = new RecyclerArticleAdapter(getContext(),listArticle);
        rcvArticle.setAdapter(articleAdapter);
        rcvArticle.setLayoutManager(new LinearLayoutManager(getContext()));

        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listArticle.clear();
                for(DataSnapshot itemSnapShot : snapshot.getChildren()){
                    Article article = itemSnapShot.getValue(Article.class);
                    listArticle.add(article);
                }
//                ArrayList<Article> sortedArticles = getSortedArticlesByLikes(listArticle);
//
//                // Kiểm tra nếu danh sách không rỗng và bài viết đầu tiên có tỉ lệ like nhỏ hơn bài viết cuối cùng
//                if (!sortedArticles.isEmpty() && sortedArticles.get(0).getLikes() < sortedArticles.get(sortedArticles.size() - 1).getLikes()) {
//                    Collections.reverse(sortedArticles); // Đảo ngược danh sách nếu cần
//                }

//                articleAdapter.setData(sortedArticles);
                articleAdapter.notifyDataSetChanged();
                isListLoaded = true;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if (isListLoaded) {
            progressbar.setVisibility(View.GONE);
        }
    }
//    private ArrayList<Article> getSortedArticlesByLikes(ArrayList<Article> articles) {
//        // Sắp xếp danh sách bài viết theo tỉ lệ like
//        Collections.sort(articles, new Comparator<Article>() {
//            @Override
//            public int compare(Article a1, Article a2) {
//                return Integer.compare(a2.getLikes(), a1.getLikes());
//            }
//        });

//        return articles;
//    }
}