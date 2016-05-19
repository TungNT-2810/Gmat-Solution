package org.iliat.gmat.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.ArcProgress;

import org.iliat.gmat.R;
import org.iliat.gmat.adapter.ListTypeQuestionAdapter;
import org.iliat.gmat.constant.Constant;
import org.iliat.gmat.model.QuestionModel;
import org.iliat.gmat.model.QuestionType;
import org.iliat.gmat.model.QuestionTypeModel;

import java.io.Serializable;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;


public class QuestionTypeDetailFragment extends BaseFragment implements Serializable{
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
    private TextView txtCategoryDetail;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sumary_type_detail, container, false);
        initControl(view);
        getDataForSumaryDetail();
        //getSumaryTypeOfQuestionDetail();
        //listTypeQuestion.setAdapter(new ListTypeQuestionAdapter(view.getContext(), arrayList));
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    private void initControl(View view) {
        listTypeQuestion = (ListView) view.findViewById(R.id.ltv_type_question_detail);
        arcProgress = (ArcProgress) view.findViewById(R.id.sumary_arc_progress_detail);
        txtAverageTime = (TextView) view.findViewById(R.id.sumary_avg_time_detail);
        txtStar = (TextView) view.findViewById(R.id.sumary_Star_detail);
        txtTagGreen = (TextView) view.findViewById(R.id.sumary_green_detail);
        txtTagGrey = (TextView) view.findViewById(R.id.sumary_grey_detail);
        txtTagRed = (TextView) view.findViewById(R.id.sumary_Red_detail);
        txtTagYellow = (TextView) view.findViewById(R.id.sumary_Yellow_detail);
        txtCategoryDetail=(TextView)view.findViewById(R.id.txtCategoryDetai);
        arrayList = new ArrayList<>();
        realm = Realm.getDefaultInstance();
        averageTime = totalAnswered = totalRightAnswer = 0;
        totalTagGreen=totalTagGrey=totalTagYellow=totalTagStar=totalTagRed=0;
    }

//    private void getSumaryTypeOfQuestionDetail() {
//        query = realm.where(QuestionModel.class);
//        queryType = realm.where(QuestionTypeModel.class);
//        resultQTypes = queryType.findAll();
//        for (int i = 0; i < resultQTypes.size(); i++) {
//            query.equalTo("type", resultQTypes.get(i).getCode());
//            results = query.findAll();
//            totalAnswered = totalRightAnswer = 0;
//            for (int j = 0; j < results.size(); j++) {
//                if (results.get(j).getUserAnswer() != 0) {
//                    totalAnswered++;
//                }
//                if (results.get(j).getUserAnswer() == results.get(j).getRightAnswerIndex()) {
//                    totalRightAnswer++;
//                }
//                if(results.get(j).getTagId()== Constant.TAG_ID[0]){
//                    totalTagGrey++;
//                }
//                if(results.get(j).getTagId()== Constant.TAG_ID[1]){
//                    totalTagGreen++;
//                }
//                if(results.get(j).getTagId()== Constant.TAG_ID[2]){
//                    totalTagYellow++;
//                }
//                if(results.get(j).getTagId()== Constant.TAG_ID[3]){
//                    totalTagRed++;
//                }
//                if(results.get(j).isStar()){
//                    totalTagStar++;
//                }
//            }
//            //arrayList.add(new QuestionType(resultQTypes.get(i).getDetail(), results.size(), totalAnswered, totalRightAnswer));
//            txtTagGrey.setText(String.valueOf(totalTagGrey));
//            txtTagGreen.setText(String.valueOf(totalTagGreen));
//            txtTagRed.setText(String.valueOf(totalTagRed));
//            txtTagYellow.setText(String.valueOf(totalTagYellow));
//            txtStar.setText(String.valueOf(totalTagStar));
//        }
//    }

    private void getDataForSumaryDetail() {
        QuestionType questionType=(QuestionType) this.getArguments().getSerializable("type");
        if(questionType==null){
            return;
        }
        arcProgress.setMax(questionType.getTotalQuestion());
        txtCategoryDetail.setText(questionType.getTypeName());
        txtStar.setText(String.valueOf(0));


        query = realm.where(QuestionModel.class);
        query.equalTo("type",questionType.getCode());
        results = query.findAll();
        totalAnswered=questionType.getTotalAnswer();
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
        arcProgress.setBottomText(totalAnswered + "/" + questionType.getTotalQuestion());
    }
}
