package org.iliat.gmat.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

import org.iliat.gmat.R;
import org.iliat.gmat.interf.ScreenManager;

/**
 * Created by ZYuTernity on 5/16/2016.
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener {

    private ImageButton btn_questions;
    private ImageButton btn_summary;
    private ImageButton btn_about;
    private ImageButton btn_1;
    private Animation animScale;
    private QuestionPackFragment questionPackFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home_menu,container, false);
        getScreenManager().setTitleOfActionBar("GMAT");
        setWidgets(view);
        return view;
    }

    public void setWidgets(View view){
        animScale = AnimationUtils.loadAnimation(view.getContext(), R.anim.anim_scale_button);
        btn_questions = (ImageButton) view.findViewById(R.id.btn_questions);
        btn_summary = (ImageButton) view.findViewById(R.id.btn_summary);
        btn_about = (ImageButton) view.findViewById(R.id.btn_about);
        btn_1 = (ImageButton) view.findViewById(R.id.btn_1);
        btn_questions.setOnClickListener(this);
        btn_summary.setOnClickListener(this);
        btn_about.setOnClickListener(this);
        btn_1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_questions:
                animScale.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        questionPackFragment = new QuestionPackFragment();
                        questionPackFragment.setmContext(getView().getContext());
                        getScreenManager().openFragment(questionPackFragment, true);
                        getScreenManager().setTitleOfActionBar("Question Packages");
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                v.startAnimation(animScale);
                break;
            case R.id.btn_summary:
                animScale.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        getScreenManager().openFragment(new SumaryFragment(),true);
                        getScreenManager().setTitleOfActionBar("Sumary");
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                v.startAnimation(animScale);
                break;
            case R.id.btn_about:
                v.startAnimation(animScale);
                break;
            case R.id.btn_1:
                v.startAnimation(animScale);
                break;
        }
    }
}
