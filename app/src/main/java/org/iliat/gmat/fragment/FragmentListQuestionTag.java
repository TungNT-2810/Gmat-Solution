package org.iliat.gmat.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.iliat.gmat.R;
import org.iliat.gmat.adapter.ListTypeQuestionAdapter;
import org.iliat.gmat.adapter.QuestionAnswerSummaryAdapter;
import org.iliat.gmat.model.QuestionModel;
import org.iliat.gmat.model.QuestionTypeModel;
import org.iliat.gmat.view_model.QuestionViewModel;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by ZYuTernity on 6/13/2016.
 */
public class FragmentListQuestionTag extends BaseFragment{

    private View view;
    private ListView listView;
    private List<QuestionViewModel> questionViewModelList;
    private QuestionViewModel questionViewModel;
    private int tagId = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list_question_tag, container, false);
        init(view);
        fillData();
        return view;
    }

    private void init(View view){
        listView = (ListView) view.findViewById(R.id.lv_question_tag);
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    private void getDataForList(){
        questionViewModelList = new ArrayList<>();

        Realm realm = Realm.getDefaultInstance();

        RealmQuery<QuestionModel> query = realm.where(QuestionModel.class);
        query.equalTo("tagId", tagId);
        RealmResults<QuestionModel> results = query.findAll();
        if (results.size() > 0){
            for (int i = 0; i <= results.size(); i++){
                questionViewModel = new QuestionViewModel(results.get(i));
                questionViewModelList.add(questionViewModel);
            }
        }
    }

    private void fillData(){
        getDataForList();
        listView.setAdapter(new QuestionAnswerSummaryAdapter(getActivity(),
                R.layout.list_item_score_question_answer_summary, questionViewModelList));
    }
}