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
import java.util.Comparator;


public class TopViewFragment extends Fragment {

    ImageView backarrow;
    private RecyclerView rcvview;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_top_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        backarrow = view.findViewById(R.id.backarrowview);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TrendFragment trendFragment = new TrendFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout,trendFragment).commit();
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
                rcvview= view.findViewById(R.id.rcvView);
                RecyclerArticleAdapter topViewedAritclesAdapter = new RecyclerArticleAdapter(getContext(),topViews,10);
                rcvview.setAdapter(topViewedAritclesAdapter);
//                rcv.setLayoutManager(new LinearLayoutManager(getContext()));
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                rcvview.setLayoutManager(linearLayoutManager);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}