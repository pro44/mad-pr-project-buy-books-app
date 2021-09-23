package com.example.buybooksonline;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.google.android.material.appbar.AppBarLayout;

public class PageAdapter extends FragmentPagerAdapter {
    int tabcount;
    public PageAdapter(FragmentManager fm, int behavior){
        super(fm,behavior);
        tabcount = behavior;
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new UserLoginFragment();
            case 1: return new AdminLoginFragment();

            default: return null;
        }//end of switch statement

    }//end of getItem method

    @Override
    public int getCount() {
        return tabcount;
    }
}

