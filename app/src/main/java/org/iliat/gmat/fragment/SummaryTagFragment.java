package org.iliat.gmat.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.iliat.gmat.R;

/**
 * Created by MrBom on 6/14/2016.
 */
public class SummaryTagFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_summary_tag,container,false);

        return view;
    }
}
