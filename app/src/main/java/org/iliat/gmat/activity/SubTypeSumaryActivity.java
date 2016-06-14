package org.iliat.gmat.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.DefaultXAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.formatter.XAxisValueFormatter;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

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
    private String typeDetail;
    private TextView txtTitle;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_type_sumary);
        overridePendingTransition(R.anim.trans_in, R.anim.trans_out);
        init();
        getDataFromIntent();
        addListener();
        getDataForChart();
    }

    private void getDataForChart() {
        if (list != null) {
            ArrayList<String> labels = new ArrayList<>();
            ArrayList<BarEntry> groupTotalQuestion = new ArrayList<>();
            ArrayList<BarEntry> groupCorrectAnswer = new ArrayList<>();
            int index=0;
            for (QuestionSubTypeModel q: list) {
                //Add label
                labels.add(q.getDetail());
                //Add number of answered questions
                groupTotalQuestion.add(new BarEntry(DBContext.getNumberQustionAnsweredByTypeAndSubType(typeCode,
                        q.getCode()), index));
                //Add number of correct questions
                groupCorrectAnswer.add(new BarEntry(DBContext.getNumberCorrectByTypeAndSubType(typeCode,
                        q.getCode()), index));
                index++;
            }

            BarDataSet barDataSet1 = new BarDataSet(groupTotalQuestion, "Total answered");
            barDataSet1.setColor(Color.parseColor("#00BCD4"));

            BarDataSet barDataSet2 = new BarDataSet(groupCorrectAnswer, "Total correct");
            barDataSet2.setColor(Color.parseColor("#8BC34A"));

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(barDataSet1);
            dataSets.add(barDataSet2);

            BarData data = new BarData(labels,dataSets);
            data.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    if(value!=0) {
                        return String.format("%d", (int) value);
                    }
                    return "";
                }
            });
            horizontalBarChart.setData(data);
            horizontalBarChart.setDescription("");
            horizontalBarChart.getData().setHighlightEnabled(false);
            horizontalBarChart.getData().setGroupSpace(1f);
        }
    }

    private void getDataFromIntent() {
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            typeCode = bundle.getString("type");
            typeDetail = bundle.getString("detail");
            txtTitle.setText(typeDetail);
            if (typeCode != null) {
                list = DBContext.getQuestionTypeByCode(typeCode).getListSubType();
                if (list != null && list.size() != 0) {
                    listView.setAdapter(new ListSubTypeAdapter(getBaseContext(), list, typeCode));
                }
            }
        }
    }

    private void init() {
        horizontalBarChart = (HorizontalBarChart) findViewById(R.id.sub_type_chart);
        listView = (ListView) findViewById(R.id.list_sub_type);
        btnClose = (Button) findViewById(R.id.btn_close);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
    }

    private void addListener() {
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(toast!=null){
                    toast.cancel();
                }
                int totalQues = DBContext.getNumberQuestionByTypeAndSubTypeCode(typeCode,
                        list.get(position).getCode());
                if(totalQues==0){
                    toast=Toast.makeText(view.getContext(),list.get(position).getDetail().toUpperCase()
                            +" has no question!",Toast.LENGTH_SHORT);
                    toast.show();
                }else{
                    if(DBContext.getNumberQustionAnsweredByTypeAndSubType(typeCode,list.get(position).getCode())>0) {
                        Bundle bundle = new Bundle();
                        bundle.putString("type", typeCode);
                        bundle.putString("typeDetail", typeDetail);
                        bundle.putString("subTypeCode", list.get(position).getCode());
                        bundle.putString("subTypeDetail", list.get(position).getDetail());
                        Intent intent = new Intent(SubTypeSumaryActivity.this, ReviewSubTypeActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }else{
                        toast=Toast.makeText(view.getContext(),list.get(position).getDetail().toUpperCase()
                                +" has no completed question!",Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    //start animation
                    overridePendingTransition(R.anim.trans_in, R.anim.trans_out);
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_back_in, R.anim.trans_back_out);
    }
}
