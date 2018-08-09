package com.gvjay.schedulemanager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

public class CalendarPagerAdapter extends FragmentStatePagerAdapter {

    public CalendarPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Log.i("Get Item Called", i+"");
        Bundle bundle = new Bundle();
        bundle.putInt("offset", i);
        bundle.putInt("granularity", 0);

        CalendarFragment calendarFragment = new CalendarFragment();
        calendarFragment.setArguments(bundle);

        return calendarFragment;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return "Page Title " + position;
    }
}
