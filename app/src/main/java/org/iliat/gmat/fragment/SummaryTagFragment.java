package org.iliat.gmat.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.iliat.gmat.R;
import org.iliat.gmat.activity.QuestionReviewActivity;
import org.iliat.gmat.adapter.ListQuestionByTagAdapter;
import org.iliat.gmat.db_connect.DBContext;
import org.iliat.gmat.model.QuestionModel;

import io.realm.RealmResults;

/**
 * Created by MrBom on 6/14/2016.
 */
public class SummaryTagFragment extends Fragment {

    private ListView listQuestionBytag;
    private TextView txtEmpty;

    private RealmResults<QuestionModel> listQuestion;
    private ListQuestionByTagAdapter listQuestionByTagAdapter;
    private int tagId;

    public void setTagId(int id) {
        this.tagId = id;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_summary_tag, container, false);
        //init
        listQuestionBytag = (ListView) view.findViewById(R.id.list_question_by_tag);
        txtEmpty = (TextView) view.findViewById(R.id.text_empty);

        //add listener
        listQuestionBytag.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(listQuestion!=null && listQuestion.size()!=0){
                    Bundle bundle=new Bundle();
                    bundle.putBoolean("summaryTag",true);
                    bundle.putInt("tagId",tagId);
                    bundle.putInt("position",position);
                    Intent intent=new Intent(view.getContext(), QuestionReviewActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

        //query data
        listQuestion = DBContext.getAllQuestionAnsweredByTagId(tagId);

        //check data is empty or not
        if (listQuestion == null || listQuestion.size() == 0) {
            txtEmpty.setVisibility(View.VISIBLE);
        } else {
            txtEmpty.setVisibility(View.GONE);
        }

        //bind data to listview
        listQuestionByTagAdapter = new ListQuestionByTagAdapter(listQuestion, view.getContext());
        listQuestionBytag.setAdapter(listQuestionByTagAdapter);

        return view;
    }
}
