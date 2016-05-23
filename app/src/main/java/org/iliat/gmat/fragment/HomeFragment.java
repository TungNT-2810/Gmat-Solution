package org.iliat.gmat.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

import org.iliat.gmat.R;

/**
 * Created by ZYuTernity on 5/16/2016.
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener {

    private ImageButton btn_questions;
    private ImageButton btn_summary;
    private ImageButton btn_about;
    private ImageButton btn_1;
    private Animation animIn;
    private QuestionPackFragment questionPackFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_menu, container, false);
        getScreenManager().setTitleOfActionBar("GMAT");
        setWidgets(view);
        return view;
    }

    public void setWidgets(View view) {
        animIn = AnimationUtils.loadAnimation(view.getContext(), R.anim.anim_button_in);
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
        switch (v.getId()) {
            case R.id.btn_questions: {
                btn_questions.startAnimation(animIn);
                questionPackFragment = new QuestionPackFragment();
                questionPackFragment.setmContext(getView().getContext());
                getScreenManager().openFragment(questionPackFragment, true);
                getScreenManager().setTitleOfActionBar("Question Packages");
                break;
            }
            case R.id.btn_summary: {
                btn_summary.startAnimation(animIn);
                getScreenManager().openFragment(new SumaryFragment(), true);
                getScreenManager().setTitleOfActionBar("Sumary");
                break;
            }
            case R.id.btn_about: {
                btn_about.startAnimation(animIn);
                break;
            }
            case R.id.btn_1: {
                btn_1.startAnimation(animIn);
                break;
            }
            default:
                break;
        }
    }
}
