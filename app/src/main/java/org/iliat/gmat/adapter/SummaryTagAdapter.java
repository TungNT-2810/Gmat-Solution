package org.iliat.gmat.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.iliat.gmat.fragment.SummaryTagFragment;

/**
 * Created by MrBom on 6/14/2016.
 */
public class SummaryTagAdapter extends FragmentPagerAdapter {
    private int numberTag;

    public SummaryTagAdapter(FragmentManager fm, int numberTag) {
        super(fm);
        this.numberTag = numberTag;
    }

    @Override
    public Fragment getItem(int position) {
        SummaryTagFragment summaryTagFragment = new SummaryTagFragment();
        summaryTagFragment.setTagId(position + 1);
        return summaryTagFragment;
    }

    @Override
    public int getCount() {
        return numberTag;
    }

}
