package org.iliat.gmat.fragment;

import android.app.Fragment;
import android.content.Context;
import android.widget.Toast;

import org.iliat.gmat.interf.ScreenManager;

/**
 * Created by hungtran on 3/13/16.
 */
public class BaseFragment extends Fragment {
    public BaseFragment() {
        // Required empty public constructor
    }

    public void showToastMessage(String message) {
        Context context = getActivity();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }

    protected ScreenManager getScreenManager() {
        return (ScreenManager)getActivity();
    }
}
