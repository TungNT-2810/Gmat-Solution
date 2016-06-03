package org.iliat.gmat.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import org.iliat.gmat.database.Question;
import org.iliat.gmat.fragment.PlaceholderFragment;
import org.iliat.gmat.fragment.PlaceholderFragmentRC;
import org.iliat.gmat.model.QuestionModel;
import org.iliat.gmat.view_model.QuestionPackViewModel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MrBom on 6/3/2016.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private final String TAG = SectionsPagerAdapter.class.toString();

    private Map<Integer, String> fragmentTags;

    private QuestionPackViewModel questionPack;
    private Fragment currentFragment;
    private FragmentManager fragmentManager;

    public SectionsPagerAdapter(FragmentManager fm, QuestionPackViewModel questionPack) {
        super(fm);
        this.questionPack = questionPack;
        fragmentTags = new HashMap<>();
        this.fragmentManager=fm;
    }

    @Override
    public Fragment getItem(int position) {
        currentFragment = this.crateFragment(position);
        return currentFragment;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object obj = super.instantiateItem(container, position);
        if (obj instanceof Fragment) {
            this.fragmentTags.put(position, ((Fragment) obj).getTag());
        }
        return obj;
    }

    public Fragment getFragment(int index) {
        String tag = this.fragmentTags.get(index);
        return fragmentManager.findFragmentByTag(tag);
    }

    private Fragment crateFragment(int position) {
        QuestionModel question = questionPack.getQuestionViewModels().get(position).getQuestion();
        if (question.getType().equals(Question.TYPE_RC)) {
            PlaceholderFragmentRC placeholderFragmentRc = PlaceholderFragmentRC.newInstance(position + 1);
            placeholderFragmentRc.setQuestionPack(questionPack);
            Log.d(TAG, "RC fragment created");
            return placeholderFragmentRc;
        } else {
            PlaceholderFragment fragment = PlaceholderFragment.newInstance(position + 1);
            fragment.setQuestionPack(questionPack);
            return fragment;
        }
    }


    @Override
    public int getCount() {
        return this.questionPack.getNumberOfQuestions();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "SECTION 1";
            case 1:
                return "SECTION 2";
            case 2:
                return "SECTION 3";
        }
        return "SECTION 4";
    }
}