package com.example.appdocbao.Fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.appdocbao.Activity.MainActivity;
import com.example.appdocbao.Adapter.RecyclerArticleAdapter;
import com.example.appdocbao.Model.Article;
import com.example.appdocbao.R;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class NewsFragment extends Fragment {
    DrawerLayout drawerLayout;
    LinearProgressIndicator progressIndicator;
    private RecyclerView rcvArticle;
    private RecyclerArticleAdapter articleAdapter;
    private ArrayList<Article> listArticle;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    EditText search_edit;
    TabItem item0,item1,item2,item3,item4,item5,item6;
    TabLayout tabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        item0=view.findViewById(R.id.item0);
        item1=view.findViewById(R.id.item1);
        item2=view.findViewById(R.id.item2);
        item3=view.findViewById(R.id.item3);
        item4=view.findViewById(R.id.item4);
        item5=view.findViewById(R.id.item5);
        item6=view.findViewById(R.id.item6);
        tabLayout = view.findViewById(R.id.tabLayout);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Xử lý khi một tab được chọn
                int position = tab.getPosition();
                filterArticles(position - 1);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Xử lý khi một tab không còn được chọn
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Xử lý khi một tab đã được chọn lại
            }
        });

        rcvArticle = view.findViewById(R.id.recycleview_items);
        progressIndicator = view.findViewById(R.id.progress_bar);
        setupRecycleView();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);
//        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
//        dialog.show();

        listArticle = new ArrayList<>();
        articleAdapter= new RecyclerArticleAdapter(getContext(),listArticle);
        rcvArticle.setAdapter(articleAdapter);

        getAllArticle();

        Toolbar toolbar =(Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        drawerLayout = view.findViewById(R.id.drawerlayout);
        NavigationView navigationView = view.findViewById(R.id.navigation_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.close_nav, R.string.open_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.news){
                    Toast.makeText(getActivity(),"Cài đặt",Toast.LENGTH_SHORT).show();
                    drawerLayout.closeDrawer(GravityCompat.START);
                    replaceFragement(new NewsFragment());
                }else if(item.getItemId()==R.id.setting) {
                    Toast.makeText(getActivity(),"Cài đặt",Toast.LENGTH_SHORT).show();
                    drawerLayout.closeDrawer(GravityCompat.START);
                    replaceFragement(new SettingFragment());
                }else if(item.getItemId()==R.id.contact) {
                    Toast.makeText(getActivity(), "Liên hệ", Toast.LENGTH_SHORT).show();
                    drawerLayout.closeDrawer(GravityCompat.START);
                    replaceFragement(new ContactFragment());
                } else if(item.getItemId()==R.id.share) {
                    Toast.makeText(getActivity(), "Donate us", Toast.LENGTH_SHORT).show();
                    drawerLayout.closeDrawer(GravityCompat.START);
                    replaceFragement(new ShareFragment());
                }
                return true;
            }
        });
        EditText searchEditText = view.findViewById(R.id.search_edit);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.hideBottomNavigationView();

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    MainActivity mainActivity = (MainActivity) getActivity();
                    if (mainActivity != null) {
                        mainActivity.hideBottomNavigationView();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    MainActivity mainActivity = (MainActivity) getActivity();
                    if (mainActivity != null) {
                        mainActivity.showBottomNavigationView();
                    }
                }
                filterList(s.toString());
            }
        });
    }
    private void filterList(String searchText) {
        ArrayList<Article> filteredList = new ArrayList<>();
        for (Article item : listArticle) {
            if (item.getTitle().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(item);
            }
        }
        if (filteredList.isEmpty()) {
            Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
        }
        articleAdapter.setData(filteredList);
    }
    private void replaceFragement(Fragment fragment) {
        FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
        fm.replace(R.id.frame_layout,fragment).commit();
    }
    void changeInProgress(boolean show){
        if(show){
            progressIndicator.setVisibility(View.VISIBLE);
        }else{
            progressIndicator.setVisibility(View.INVISIBLE);
        }
    }
    void setupRecycleView(){
        rcvArticle.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvArticle.setHasFixedSize(true);
        articleAdapter = new RecyclerArticleAdapter(getContext(),listArticle);
        rcvArticle.setAdapter(articleAdapter);
    }
    private void filterArticles(int categoryId) {
        if(categoryId == - 1){
            getAllArticle();
        }else{
            DatabaseReference articlesRef = FirebaseDatabase.getInstance().getReference("articles");
            articlesRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    listArticle.clear();
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        Article article = childSnapshot.getValue(Article.class);
                        if (article != null && article.getCategoryId() == categoryId ){
                            listArticle.add(article);
                        }
                    }
                    Collections.sort(listArticle, new Comparator<Article>() {
                        @Override
                        public int compare(Article article1, Article article2) {
                            // So sánh thời gian giữa hai bài báo
                            return Long.compare(article2.getTimestamp(), article1.getTimestamp());
                        }
                    });
                    articleAdapter.notifyDataSetChanged();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Xử lý lỗi nếu có
                }
            });
        }
    }
    private void getAllArticle(){
        databaseReference = FirebaseDatabase.getInstance().getReference("articles");
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
                articleAdapter.notifyDataSetChanged();
                changeInProgress(false);
//                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
//                dialog.dismiss();
            }
        });
    }
}