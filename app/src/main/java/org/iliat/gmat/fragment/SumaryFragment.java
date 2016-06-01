package org.iliat.gmat.fragment;

import android.support.annotation.Nullable;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.ArcProgress;
import com.github.lzyzsd.circleprogress.CircleProgress;
import com.github.lzyzsd.circleprogress.DonutProgress;

import org.iliat.gmat.R;
import org.iliat.gmat.adapter.ListTypeQuestionAdapter;
import org.iliat.gmat.constant.Constant;
import org.iliat.gmat.model.QuestionModel;
import org.iliat.gmat.model.QuestionType;
import org.iliat.gmat.model.QuestionTypeModel;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;


public class SumaryFragment extends BaseFragment {
    private static final String TAG = SumaryFragment.class.toString();

    private ListView listTypeQuestion;
    private ArrayList<QuestionType> arrayList;
    private Realm realm;
    private RealmResults<QuestionModel> results;
    private RealmQuery<QuestionModel> query;
    private RealmResults<QuestionTypeModel> resultQTypes;
    private RealmQuery<QuestionTypeModel> queryType;
    private int totalAnswered;
    private int totalRightAnswer;
    private int totalQuestion;
    private int averageTime;
    private int totalTagGrey;
    private int totalTagGreen;
    private int totalTagYellow;
    private int totalTagStar;
    private int totalTagRed;
    private ArcProgress arcProgress;
    private TextView txtAverageTime;
    private TextView txtTagGrey;
    private TextView txtTagGreen;
    private TextView txtTagYellow;
    private TextView txtTagRed;
    private TextView txtStar;
    private View view;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(view==null){
            view=inflater.inflate(R.layout.fragment_sumary,container,false);
        }else{
            container.removeView(view);
        }
        initControl(view);
        getDataForSumary();
        getSumaryTypeOfQuestion();
        listTypeQuestion.setAdapter(new ListTypeQuestionAdapter(view.getContext(), arrayList));
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    private void initControl(View view) {
        listTypeQuestion = (ListView) view.findViewById(R.id.ltv_type_question);
        arcProgress = (ArcProgress) view.findViewById(R.id.sumary_arc_progress);
        txtAverageTime = (TextView) view.findViewById(R.id.sumary_avg_time);
        txtStar = (TextView) view.findViewById(R.id.sumary_Star);
        txtTagGreen = (TextView) view.findViewById(R.id.sumary_green);
        txtTagGrey = (TextView) view.findViewById(R.id.sumary_grey);
        txtTagRed = (TextView) view.findViewById(R.id.sumary_Red);
        txtTagYellow = (TextView) view.findViewById(R.id.sumary_Yellow);
        arrayList = new ArrayList<>();
        realm = Realm.getDefaultInstance();
        averageTime = totalAnswered = totalRightAnswer = 0;
        totalTagGreen = totalTagGrey = totalTagYellow = totalTagStar = totalTagRed = 0;
    }

    private void getSumaryTypeOfQuestion() {
        queryType = realm.where(QuestionTypeModel.class);
        resultQTypes = queryType.findAll();

        for (int i = 0; i < resultQTypes.size(); i++) {
            query = realm.where(QuestionModel.class);
            String code = resultQTypes.get(i).getCode();
            query.equalTo("type", code);
            results = query.findAll();
            Log.d(TAG, code + " "+results.size());
            totalAnswered = totalRightAnswer = 0;
            Log.d("type",resultQTypes.get(i).getCode());
            for (int j = 0; j < results.size(); j++) {
                //total answered
                if (results.get(j).getUserAnswer() != 0) {
                    totalAnswered++;
                }
                //total correct
                if (results.get(j).getUserAnswer() == results.get(j).getRightAnswerIndex()) {
                    totalRightAnswer++;
                }
                //
                if (results.get(j).isStar()) {
                    totalTagStar++;
                }
                //calculate number of tag
                switch (results.get(j).getTagId()) {
                    case Constant.TAG_GREY: {
                        totalTagGrey++;
                        break;
                    }
                    case Constant.TAG_GREEN: {
                        totalTagGreen++;
                        break;
                    }
                    case Constant.TAG_YELLOW: {
                        totalTagYellow++;
                        break;
                    }
                    case Constant.TAG_RED: {
                        totalTagRed++;
                        break;
                    }
                    default:
                        break;
                }

            }
            arrayList.add(new QuestionType(resultQTypes.get(i).getCode(), resultQTypes.get(i).getDetail(),
                    results.size(), totalAnswered, totalRightAnswer));
            //push data
            txtTagGrey.setText(String.valueOf(totalTagGrey));
            txtTagGreen.setText(String.valueOf(totalTagGreen));
            txtTagRed.setText(String.valueOf(totalTagRed));
            txtTagYellow.setText(String.valueOf(totalTagYellow));
            txtStar.setText(String.valueOf(totalTagStar));
        }
    }

    private void getDataForSumary() {
        query = realm.where(QuestionModel.class);
        results = query.findAll();
        totalQuestion = results.size();

        arcProgress.setMax(totalQuestion);
        query.notEqualTo("userAnswer", 0);
        results = query.findAll();
        totalAnswered = results.size();
        for (QuestionModel q : results) {
            averageTime += q.getTimeToFinish();
        }
        if (totalAnswered != 0) {
            averageTime /= totalAnswered;
            txtAverageTime.setText(averageTime / 60 + "m " + averageTime % 60 + "s");
        } else {
            txtAverageTime.setText("0m 0s");
        }
        arcProgress.setProgress(totalAnswered);
    }
}
