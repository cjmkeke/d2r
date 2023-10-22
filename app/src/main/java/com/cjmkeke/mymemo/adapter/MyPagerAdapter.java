package com.cjmkeke.mymemo.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.cjmkeke.mymemo.DatabaseStorageMemo;
import com.cjmkeke.mymemo.DatabaseStorageMemoShare;

public class MyPagerAdapter extends FragmentStateAdapter {

    public MyPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new DatabaseStorageMemo();
            case 1:
                return new DatabaseStorageMemoShare();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 2;       // 페이지 수
    }
}