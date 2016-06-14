package org.iliat.gmat.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.iliat.gmat.fragment.SummaryTagFragment;

/**
 * Created by MrBom on 6/14/2016.
 */
public class SummaryTagAdapter extends FragmentPagerAdapter {
    int count=5;
    public SummaryTagAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:{
                return new SummaryTagFragment();
            }
            case 1:{
                return new SummaryTagFragment();
            }
            case 2:{
                return new SummaryTagFragment();
            }
            case 3:{
                return new SummaryTagFragment();
            }
            case 4:{
                return new SummaryTagFragment();
            }
            default:return null;
        }
    }

    @Override
    public int getCount() {
        return count;
    }
}
