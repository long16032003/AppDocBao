package com.example.appdocbao.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.appdocbao.Adapter.RecyclerDataAdapter;
import com.example.appdocbao.Model.Article;
import com.example.appdocbao.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;


public class NewsFragment extends Fragment {
    DrawerLayout drawerLayout;
    private RecyclerView rcvArticle;
    private RecyclerDataAdapter articleAdapter;
    private ArrayList<Article> listArticle;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listArticle = new ArrayList<>();
        createArticleList();
        rcvArticle = view.findViewById(R.id.recycleview_items);
        rcvArticle.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvArticle.setHasFixedSize(true);
        articleAdapter = new RecyclerDataAdapter(getContext(),listArticle);
        rcvArticle.setAdapter(articleAdapter);

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
                if(item.getItemId()==R.id.setting){
                    Toast.makeText(getActivity(),"Cài đặt",Toast.LENGTH_SHORT).show();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }else if(item.getItemId()==R.id.contact) {
                    Toast.makeText(getActivity(), "Liên hệ", Toast.LENGTH_SHORT).show();
                    drawerLayout.closeDrawer(GravityCompat.START);

                }else if(item.getItemId()==R.id.share) {
                    Toast.makeText(getActivity(), "Chia sẻ", Toast.LENGTH_SHORT).show();
                    drawerLayout.closeDrawer(GravityCompat.START);

                }
                return true;
            }
        });


    }
    private void createArticleList() {
        listArticle.add(new Article("Thor",R.drawable.logo_news));
        listArticle.add(new Article("IronMan",R.drawable.logo_news));
        listArticle.add(new Article("Hulk",R.drawable.logo_news));
        listArticle.add(new Article("SpiderMan",R.drawable.logo_news));
        listArticle.add(new Article("Thor",R.drawable.logo_news));
        listArticle.add(new Article("IronMan",R.drawable.logo_news));
        listArticle.add(new Article("Hulk",R.drawable.logo_news));
        listArticle.add(new Article("SpiderMan",R.drawable.logo_news));
        listArticle.add(new Article("Thor",R.drawable.logo_news));
        listArticle.add(new Article("IronMan",R.drawable.logo_news));
    }
}