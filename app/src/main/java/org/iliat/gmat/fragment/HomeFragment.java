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

import com.github.lzyzsd.circleprogress.ArcProgress;

import org.iliat.gmat.R;
import org.iliat.gmat.activity.AnswerQuestionActivity;
import org.iliat.gmat.adapter.ListQuestionPackAdapter;
import org.iliat.gmat.model.QuestionModel;
import org.iliat.gmat.model.QuestionPackModel;
import org.iliat.gmat.view_model.QuestionPackViewModel;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by ZYuTernity on 5/16/2016.
 * Modified by LinhDQ
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener,
        ListQuestionPackAdapter.OnListQuestionPackListener {

    private Realm realm;
    private RecyclerView recyclerView;
    private Button btnMore;
    private ArcProgress arcProgress;
    private RealmResults<QuestionModel> results;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_screen, container, false);
        getScreenManager().setTitleOfActionBar(getResources().getString(R.string.string_title_GMAT));
        inits(view);
        loadQuestionPack(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getDataForArcProgress();
    }

    public void inits(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.list_pack_question);
        btnMore=(Button)view.findViewById(R.id.btnMore);
        btnMore.setOnClickListener(this);
        arcProgress=(ArcProgress)view.findViewById(R.id.home_arc_progress);
    }

    private void loadQuestionPack(View view) {
        Context context = view.getContext();
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        ListQuestionPackAdapter listQuestionPackAdapter = new ListQuestionPackAdapter();
        realm = Realm.getDefaultInstance();
        listQuestionPackAdapter.setQuestionPackList(realm.where(QuestionPackModel.class).findAll());
        listQuestionPackAdapter.setQuestionPackListener(this);
        listQuestionPackAdapter.setContext(context);
        recyclerView.setAdapter(listQuestionPackAdapter);
        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
    }

    private void getDataForArcProgress() {
        int totalQuestion=0;
        int totalAnswered=0;
        results = realm.where(QuestionModel.class).findAll().distinct("id");
        totalQuestion = results.size();
        arcProgress.setMax(totalQuestion);
        results = realm.where(QuestionModel.class).notEqualTo("userAnswer",(-1)).findAll().distinct("id");
        totalAnswered = results.size();
        arcProgress.setProgress(totalAnswered);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnMore:{
                getScreenManager().setTitleOfActionBar("Summary");
                getScreenManager().openFragment(new SummaryFragment(),true);
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
