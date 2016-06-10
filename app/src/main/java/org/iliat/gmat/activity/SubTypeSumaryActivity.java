package org.iliat.gmat.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import org.iliat.gmat.R;
import org.iliat.gmat.adapter.ListSubTypeAdapter;
import org.iliat.gmat.db_connect.DBContext;
import org.iliat.gmat.model.QuestionSubTypeModel;

import java.util.ArrayList;

import io.realm.RealmList;

public class SubTypeSumaryActivity extends Activity {
    private HorizontalBarChart horizontalBarChart;
    private ListView listView;
    private Button btnClose;
    private RealmList<QuestionSubTypeModel> list;
    private String typeCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_type_sumary);
        overridePendingTransition(R.anim.trans_in, R.anim.trans_out);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDataFromIntent();
        addListener();
        getDataForChart();
    }

    private void getDataForChart() {
        if (list != null) {
            ArrayList<String> labels = new ArrayList<>();
            ArrayList<BarEntry> groupTotalQuestion = new ArrayList<>();
            ArrayList<BarEntry> groupCorrectAnswer = new ArrayList<>();
            int numberOfQuestion = 0;
            int numberOfCorrectQues = 0;
            int numberOfOtherQues = 0;
            int numberOfOtherQuesCorrect = 0;

            for (int i = 0; i < list.size(); i++) {
                labels.add(list.get(i).getDetail());
                groupTotalQuestion.add(new BarEntry(DBContext.getNumberQustionAnsweredByTypeAndSubType(typeCode,
                        list.get(i).getCode()), i));
                groupCorrectAnswer.add(new BarEntry(DBContext.getNumberCorrectByTypeAndSubType(typeCode,
                        list.get(i).getCode()), i));
                numberOfQuestion += DBContext.getNumberQustionAnsweredByTypeAndSubType(typeCode, list.get(i).getCode());
                numberOfCorrectQues += DBContext.getNumberCorrectByTypeAndSubType(typeCode, list.get(i).getCode());
                Log.d("Linh",typeCode+":"+list.get(i).getDetail()+":"+DBContext.getNumberQustionAnsweredByTypeAndSubType(typeCode, list.get(i).getCode()));
                Log.d("Linh",typeCode+":"+list.get(i).getDetail()+": correct:"+DBContext.getNumberCorrectByTypeAndSubType(typeCode, list.get(i).getCode()));
            }
            numberOfOtherQues=DBContext.getNumberQustionByType(typeCode)-numberOfQuestion;
            if(numberOfOtherQues!=0){
                numberOfOtherQuesCorrect=DBContext.getNumberCorrectByType(typeCode)-numberOfCorrectQues;
                labels.add("Others");
                groupCorrectAnswer.add(new BarEntry(numberOfOtherQuesCorrect,list.size()));
                groupTotalQuestion.add(new BarEntry(numberOfOtherQues,list.size()));
            }

            BarDataSet barDataSet1 = new BarDataSet(groupTotalQuestion, "Total answered");
            barDataSet1.setColor(Color.parseColor("#00BCD4"));

            BarDataSet barDataSet2 = new BarDataSet(groupCorrectAnswer, "Total correct");
            barDataSet2.setColor(Color.parseColor("#8BC34A"));

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(barDataSet1);
            dataSets.add(barDataSet2);

            BarData data = new BarData(labels, dataSets);
            horizontalBarChart.setData(data);
            horizontalBarChart.setDescription("");
        }
    }

    private void getDataFromIntent() {
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            typeCode = bundle.getString("type");
            if (typeCode != null) {
                list = DBContext.getQuestionTypeByCode(typeCode).getListSubType();
                if (list != null && list.size() != 0) {
                    listView.setAdapter(new ListSubTypeAdapter(getBaseContext(), list,
                            DBContext.getQuestionTypeByCode(typeCode).getCode()));
                }
            }
        }
    }

    private void init() {
        horizontalBarChart = (HorizontalBarChart) findViewById(R.id.sub_type_chart);
        listView = (ListView) findViewById(R.id.list_sub_type);
        btnClose = (Button) findViewById(R.id.btn_close);
    }

    private void addListener() {
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_back_in, R.anim.trans_back_out);
    }
}
