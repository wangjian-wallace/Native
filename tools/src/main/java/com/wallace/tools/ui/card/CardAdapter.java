package com.wallace.tools.ui.card;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by akmob on 2017/8/15.
 */

 class CardAdapter extends FragmentPagerAdapter{
    private ArrayList<NCardFragment> fragments;

    public ArrayList<NCardFragment> getFragments() {
        return fragments;
    }

    public void setFragments(ArrayList<NCardFragment> fragments) {
        this.fragments = fragments;
    }

    public void setListener(int index,CardInputListener listener){
        fragments.get(index).setListener(listener);
    }

    CardAdapter(FragmentManager fm, ArrayList<NCardFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
