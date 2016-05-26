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

import org.iliat.gmat.R;
import org.iliat.gmat.activity.AnswerQuestionActivity;
import org.iliat.gmat.adapter.ListQuestionPackAdapter;
import org.iliat.gmat.model.QuestionPackModel;
import org.iliat.gmat.view_model.QuestionPackViewModel;

import io.realm.Realm;

/**
 * Created by ZYuTernity on 5/16/2016.
 * Modified by LinhDQ on 26/05/2016
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener,ListQuestionPackAdapter.OnListQuestionPackListener {

    private Realm realm;
    private RecyclerView recyclerView;
    private Button btnMore;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_screen, container, false);
        getScreenManager().setTitleOfActionBar("GMAT");
        inits(view);
        loadQuestionPack(view);
        return view;
    }

    public void inits(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.list_pack_question);
        btnMore=(Button)view.findViewById(R.id.btnMore);
        btnMore.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnMore:{
                getScreenManager().openFragment(new SumaryFragment(),true);
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
