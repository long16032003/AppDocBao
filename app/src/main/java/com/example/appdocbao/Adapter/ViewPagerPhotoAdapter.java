package com.example.appdocbao.Adapter;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.appdocbao.Activity.ViewPagerPhoto;
import com.example.appdocbao.R;

import java.util.List;


public class ViewPagerPhotoAdapter extends PagerAdapter {

    private List<ViewPagerPhoto> mListPhoto;

    public ViewPagerPhotoAdapter(List<ViewPagerPhoto> mListPhoto) {
        this.mListPhoto = mListPhoto;
    }

    public ViewPagerPhotoAdapter() {

    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_photo, container, false);
        ImageView imgPhoto = view.findViewById(R.id.img_photo);

        ViewPagerPhoto photo = mListPhoto.get(position);
        imgPhoto.setImageResource(photo.getResourceId());

        //add view
        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        if(mListPhoto != null){
            return mListPhoto.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);
        container.removeView((View) object);
    }
}
