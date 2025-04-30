package com.afaryseller.ui.onlineorderhistory;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class OrderFragmentAdapter extends FragmentPagerAdapter {

    private Context myContext;
    int totalTabs;

    public OrderFragmentAdapter(Context context, FragmentManager fm) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:

                return new OrderCompleteFragment();

            case 1:
                return new OrderCancelFragment();



            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return 2;
    }
}
