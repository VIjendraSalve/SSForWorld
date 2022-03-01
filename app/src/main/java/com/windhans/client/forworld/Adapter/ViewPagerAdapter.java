package com.windhans.client.forworld.Adapter;

import android.content.res.Resources;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.windhans.client.forworld.Fragment.EnquiryProductListFragment;
import com.windhans.client.forworld.Fragment.LeadDetailsFragment;
import com.windhans.client.forworld.Fragment.ProductListFragment;
import com.windhans.client.forworld.Fragment.StatusDetails;
import com.windhans.client.forworld.R;

public class ViewPagerAdapter  extends FragmentPagerAdapter {

    Bundle bundle;
    public ViewPagerAdapter(FragmentManager fm, Bundle bundle) {
        super(fm);
        this.bundle=bundle;
    }

    public ViewPagerAdapter(FragmentManager supportFragmentManager) {
        super(supportFragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        if (position == 0)
        {
            fragment = new LeadDetailsFragment();

        }
        else if (position == 1)
        {
            //fragment = new ProductListFragment();
            fragment = new EnquiryProductListFragment();


        }
        else if(position==2)
        {
            fragment = new StatusDetails();
        }




        return fragment;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;

        if (position == 0)
        {
            title = "Lead Details";
        }
        else if (position == 1)
        {
            title = "Product";
        }
        else {
            title = "Status";
        }





        return title;
    }


}


