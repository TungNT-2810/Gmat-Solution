package org.iliat.gmat.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import org.iliat.gmat.constant.Constant;
import org.iliat.gmat.fragment.PlaceholderFragment;
import org.iliat.gmat.fragment.PlaceholderFragmentRC;
import org.iliat.gmat.model.QuestionModel;
import org.iliat.gmat.view_model.QuestionPackViewModel;
import org.iliat.gmat.view_model.QuestionViewModel;

import java.util.HashMap;
import java.util.Map;

import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by MrBom on 6/3/2016.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private final String TAG = SectionsPagerAdapter.class.toString();

    private Map<Integer, String> fragmentTags;

    private Fragment currentFragment;
    private FragmentManager fragmentManager;
    private RealmList<QuestionModel> listQuestion;

    public SectionsPagerAdapter(FragmentManager fm, RealmList<QuestionModel> listQuestion) {
        super(fm);
        fragmentTags = new HashMap<>();
        this.fragmentManager = fm;
        this.listQuestion = listQuestion;
    }

    @Override
    public Fragment getItem(int position) {
        currentFragment = this.createFragment(position);
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

    private Fragment createFragment(int position) {

        QuestionModel question = listQuestion.get(position);
        if (question.getType().equals(Constant.TYPE_RC)) {
            PlaceholderFragmentRC placeholderFragmentRc = PlaceholderFragmentRC.newInstance(position + 1);
            placeholderFragmentRc.setQuestionPack(listQuestion);
            return placeholderFragmentRc;
        } else {
            PlaceholderFragment fragment = PlaceholderFragment.newInstance(position + 1);
            fragment.setQuestionList(listQuestion, position);
            return fragment;
        }
    }


    @Override
    public int getCount() {
        return this.listQuestion.size();
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