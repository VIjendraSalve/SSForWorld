package com.windhans.client.forworld.Adapter;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.windhans.client.forworld.Fragment.EnquiryDetailFragment;
import com.windhans.client.forworld.Fragment.StatusDetails1;

public class EnquiryDetailsApdater extends FragmentPagerAdapter {

    Bundle bundle;
    public EnquiryDetailsApdater(FragmentManager fm, Bundle bundle) {
        super(fm);
        this.bundle=bundle;
    }

    public EnquiryDetailsApdater(FragmentManager supportFragmentManager) {
        super(supportFragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        if (position == 0)
        {
            fragment = new EnquiryDetailFragment();

        }
        else if (position == 1)
        {
            fragment = new StatusDetails1();

        }




        return fragment;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;

        if (position == 0)
        {
            title = "Leads Details";
        }
        else if (position == 1)
        {
            title = "Status";
        }



        return title;
    }


}
