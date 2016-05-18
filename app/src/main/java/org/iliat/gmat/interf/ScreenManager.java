package org.iliat.gmat.interf;

import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;

/**
 * Created by hungtran on 3/13/16.
 */
public interface ScreenManager {
    void openFragment(Fragment fragment, boolean addToBackStack);
    void showDialogFragment(DialogFragment dialogFragment, String tag);
    boolean back();
    void setTitleOfActionBar(String titles);
    /**
     * Hàm này để chuyển activity mới
     * @param activityClass Class của activity mới.
     * @example AnswerQuestionAcivity.
     */
    void goToActivity(Class activityClass, Bundle bundle);
}
