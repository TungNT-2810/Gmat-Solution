package org.iliat.gmat.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.ArcProgress;

import org.iliat.gmat.R;
import org.iliat.gmat.activity.AnswerQuestionActivity;
import org.iliat.gmat.adapter.ListQuestionPackAdapter;
import org.iliat.gmat.db_connect.DBContext;
import org.iliat.gmat.model.QuestionModel;
import org.iliat.gmat.model.QuestionPackModel;
import org.iliat.gmat.view_model.QuestionPackViewModel;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by ZYuTernity on 5/16/2016.
 * Modified by LinhDQ
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener,
        ListQuestionPackAdapter.OnListQuestionPackListener {
    //view
    private View view;
    private TextView txtSkillLevel;
    private RecyclerView recyclerView;
    private Button btnMore;
    private ArcProgress arcProgress;

    //
    private DBContext dbContext;
    private RealmResults<QuestionModel> results;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.home_screen, container, false);

        getScreenManager().setTitleOfActionBar(getResources().getString(R.string.string_title_GMAT));

        init(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadQuestionPack(view);
        getDataForArcProgress();
    }

    public void init(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.list_pack_question);
        btnMore = (Button) view.findViewById(R.id.btnMore);
        arcProgress = (ArcProgress) view.findViewById(R.id.home_arc_progress);
        txtSkillLevel = (TextView) view.findViewById(R.id.txt_skill_leel);

        //add listener
        btnMore.setOnClickListener(this);

        //get singleton
        dbContext = DBContext.getInst();
    }

    private void calculateSkillLevel(int totalRightAnswer, int totalQuestion) {
        float rate = 0;
        if (totalQuestion > 0) {
            rate = (float) totalRightAnswer / totalQuestion;
            if (rate < 70) {
                txtSkillLevel.setText("Newbie");
            } else if (rate >= 70 && rate < 90) {
                txtSkillLevel.setText("Intermediate");
            } else {
                txtSkillLevel.setText("Master");
            }
        }
    }

    private void loadQuestionPack(View view) {
        Context context = view.getContext();

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        ListQuestionPackAdapter listQuestionPackAdapter = new ListQuestionPackAdapter();

        listQuestionPackAdapter.setQuestionPackList(dbContext.getAllQuestionPack());

        listQuestionPackAdapter.setQuestionPackListener(this);
        listQuestionPackAdapter.setContext(context);
        recyclerView.setAdapter(listQuestionPackAdapter);

        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(staggeredGridLayoutManager);
    }

    private void getDataForArcProgress() {
        int totalRightAnswer = dbContext.getNumberCorrectQuestion();
        int totalQuestion = dbContext.getNumberOfQuestion();
        int totalAnswered = dbContext.getNumberOfQuestionAnswered();

        arcProgress.setMax(100);
        if (totalQuestion != 0) {
            arcProgress.setProgress(totalAnswered * 100 / totalQuestion);
        } else {
            arcProgress.setProgress(0);
        }
        calculateSkillLevel(totalRightAnswer, totalQuestion);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnMore: {
                getScreenManager().setTitleOfActionBar("Question Type Summary");
                getScreenManager().openFragment(new SummaryFragment(), true);
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void onQuestionPackInteraction(QuestionPackViewModel item) {
        getScreenManager().goToActivity(AnswerQuestionActivity.class,
                AnswerQuestionActivity.buildBundle(item.getQuestionPack().getId()));
    }
}
