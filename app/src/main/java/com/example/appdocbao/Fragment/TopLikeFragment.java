package com.example.appdocbao.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TopLikeFragment extends Fragment {

    ImageView backarrow;
    private RecyclerView rcvlike;
    private RecyclerArticleAdapter articleAdapter;
    private ArrayList<Article> listArticle;
    ValueEventListener eventListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_top_like, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);backarrow = view.findViewById(R.id.backarrow);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TrendFragment trendFragment = new TrendFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout,trendFragment).commit();
            }
        });

        rcvlike = view.findViewById(R.id.rcvlike);
        DatabaseReference likesRef = FirebaseDatabase.getInstance().getReference("likes");

        listArticle = new ArrayList<>();
        articleAdapter = new RecyclerArticleAdapter(getContext(), listArticle, 10);
        rcvlike.setAdapter(articleAdapter);
        rcvlike.setLayoutManager(new LinearLayoutManager(getContext()));

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
    }
}