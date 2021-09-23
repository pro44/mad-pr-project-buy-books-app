package com.example.buybooksonline;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PageAdapter_Registration extends FragmentPagerAdapter {
    int tabcount;
    public PageAdapter_Registration(FragmentManager fm, int behavior){
        super(fm,behavior);
        tabcount = behavior;
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new UserRegister();
            case 1: return new AdminRegister();

            default: return null;
        }//end of switch statement

    }//end of getItem method

    @Override
    public int getCount() {
        return tabcount;
    }
}

