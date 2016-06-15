package org.iliat.gmat.fragment;

import android.content.Intent;
import android.support.annotation.Nullable;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.ArcProgress;

import org.iliat.gmat.R;
import org.iliat.gmat.activity.ReviewQuestionTagActivity;
import org.iliat.gmat.activity.SubTypeSumaryActivity;
import org.iliat.gmat.adapter.ListTypeQuestionAdapter;
import org.iliat.gmat.constant.Constant;
import org.iliat.gmat.db_connect.DBContext;
import org.iliat.gmat.model.QuestionModel;
import org.iliat.gmat.model.QuestionType;
import org.iliat.gmat.model.QuestionTypeModel;

import java.util.ArrayList;

import io.realm.RealmQuery;
import io.realm.RealmResults;


public class SummaryFragment extends BaseFragment {

    private static final String TAG = SummaryFragment.class.toString();

    //view
    private ListView listTypeQuestion;
    private ArcProgress arcProgress;
    private TextView txtAverageTime;
    private TextView txtTagGrey;
    private TextView txtTagGreen;
    private TextView txtTagYellow;
    private TextView txtTagRed;
    private TextView txtStar;
    private View view;
    private LinearLayout linearLayout;

    //
    private ArrayList<QuestionType> arrayList;
    private RealmResults<QuestionModel> results;
    private RealmQuery<QuestionModel> query;
    private RealmResults<QuestionTypeModel> resultQTypes;
    private RealmQuery<QuestionTypeModel> queryType;
    private ListTypeQuestionAdapter listTypeQuestionAdapter;
    private DBContext dbContext;

    private int totalAnswered;
    private int totalRightAnswer;
    private int totalQuestion;
    private int totalTime;
    private int totalTagGrey;
    private int totalTagGreen;
    private int totalTagYellow;
    private int totalTagStar;
    private int totalTagRed;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_sumary, container, false);
        } else {
            container.removeView(view);
        }

        init(view);
        addListener();
        getSumaryTypeOfQuestion();
        bindDataToListview();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getDataForSumary();
    }

    private void bindDataToListview() {
        if (arrayList != null) {
            listTypeQuestionAdapter = new ListTypeQuestionAdapter(view.getContext(), arrayList);
            listTypeQuestion.setAdapter(listTypeQuestionAdapter);
        }
    }

    private void init(View view) {
        //
        listTypeQuestion = (ListView) view.findViewById(R.id.ltv_type_question);
        arcProgress = (ArcProgress) view.findViewById(R.id.sumary_arc_progress);
        txtAverageTime = (TextView) view.findViewById(R.id.sumary_avg_time);
        txtStar = (TextView) view.findViewById(R.id.sumary_Star);
        txtTagGreen = (TextView) view.findViewById(R.id.sumary_green);
        txtTagGrey = (TextView) view.findViewById(R.id.sumary_grey);
        txtTagRed = (TextView) view.findViewById(R.id.sumary_Red);
        txtTagYellow = (TextView) view.findViewById(R.id.sumary_Yellow);
        linearLayout = (LinearLayout) view.findViewById(R.id.tag_container);

        //
        arrayList = new ArrayList<>();
        dbContext=DBContext.getInst();
        totalTime = totalAnswered = totalRightAnswer = 0;
        totalTagGreen = totalTagGrey = totalTagYellow = totalTagStar = totalTagRed = 0;
    }

    private void addListener() {
        listTypeQuestion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (arrayList != null) {
                    Intent intent = new Intent(view.getContext(), SubTypeSumaryActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("type", arrayList.get(position).getCode());
                    bundle.putString("detail", arrayList.get(position).getTypeName());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ReviewQuestionTagActivity.class);
                startActivity(intent);
            }
        });
    }


    private void getSumaryTypeOfQuestion() {
        resultQTypes = dbContext.getAllQuestiontype();

        for (int i = 0; i < resultQTypes.size(); i++) {

            String code = resultQTypes.get(i).getCode();
            results=dbContext.getAllQuestionByType(code);

            totalRightAnswer=0;
            totalAnswered= dbContext.getNumberQuestionAnsweredByTypeCode(code);

            //total correct
            for (int j = 0; j < results.size(); j++) {
                if (results.get(j).getUserAnswer() == results.get(j).getRightAnswerIndex()) {
                    totalRightAnswer++;
                }
            }

            arrayList.add(new QuestionType(resultQTypes.get(i).getCode(), resultQTypes.get(i).getDetail(),
                    results.size(), totalRightAnswer, totalAnswered, resultQTypes.get(i).getListSubType()));
        }
    }

    private void getDataForSumary() {
        totalQuestion = dbContext.getNumberOfQuestion();
        totalAnswered = dbContext.getNumberOfQuestionAnswered();
        totalTime = (int) dbContext.getTotalTime();

        totalTagStar=dbContext.getNumberOfQuestionByTagId(Constant.TAG_STAR);
        totalTagGrey=dbContext.getNumberOfQuestionByTagId(Constant.TAG_GREY);
        totalTagGreen=dbContext.getNumberOfQuestionByTagId(Constant.TAG_GREEN);
        totalTagYellow=dbContext.getNumberOfQuestionByTagId(Constant.TAG_YELLOW);
        totalTagRed=dbContext.getNumberOfQuestionByTagId(Constant.TAG_RED);

        if (totalAnswered != 0) {
            totalTime /= totalAnswered;
            txtAverageTime.setText(totalTime / 60 + "m " + totalTime % 60 + "s");
        } else {
            txtAverageTime.setText("0m 0s");
        }

        arcProgress.setMax(100);

        if (totalQuestion != 0) {
            arcProgress.setProgress(totalAnswered * 100 / totalQuestion);
        } else {
            arcProgress.setProgress(0);
        }

        //bind data
        txtTagGrey.setText(String.valueOf(totalTagGrey));
        txtTagGreen.setText(String.valueOf(totalTagGreen));
        txtTagRed.setText(String.valueOf(totalTagRed));
        txtTagYellow.setText(String.valueOf(totalTagYellow));
        txtStar.setText(String.valueOf(totalTagStar));
    }
}
