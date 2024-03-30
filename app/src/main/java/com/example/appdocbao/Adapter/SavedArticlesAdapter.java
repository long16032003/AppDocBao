package com.example.appdocbao.Adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SavedArticlesAdapter extends RecyclerView.Adapter<SavedArticlesAdapter.SavedViewHolder>{

    @NonNull
    @Override
    public SavedArticlesAdapter.SavedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull SavedArticlesAdapter.SavedViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class SavedViewHolder extends RecyclerView.ViewHolder{

        public SavedViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
