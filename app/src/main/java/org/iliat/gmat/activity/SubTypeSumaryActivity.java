package org.iliat.gmat.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.iliat.gmat.R;
import org.iliat.gmat.adapter.ListSubTypeAdapter;
import org.iliat.gmat.db_connect.DBContext;
import org.iliat.gmat.model.QuestionSubTypeModel;

import java.util.ArrayList;

import io.realm.RealmList;

public class SubTypeSumaryActivity extends Activity {
    //view
    private HorizontalBarChart horizontalBarChart;
    private ListView listView;
    private Button btnClose;
    private TextView txtTitle;
    private Toast toast;
    //
    private RealmList<QuestionSubTypeModel> list;
    private String typeCode;
    private String typeDetail;

    private DBContext dbContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_type_sumary);
        //set animation
        overridePendingTransition(R.anim.trans_in, R.anim.trans_out);
        //
        init();
        getDataFromIntent();
        addListener();
        getDataForChart();
    }

    private void addStyleForChart(HorizontalBarChart chart) {
        // View
        chart.setDescription("");
        chart.animateXY(1000, 1000);
        chart.setClickable(false);
        chart.setTouchEnabled(false);
        chart.setFocusable(false);
        chart.setSelected(false);
        chart.setPinchZoom(false);
        chart.setPressed(false);

        // Label
        Legend legend = chart.getLegend();
        legend.setFormSize(12f);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        legend.setTextSize(12f);
        legend.setTextColor(Color.BLACK);
        legend.setXEntrySpace(8f);
        legend.setYEntrySpace(8f);
    }

    private void getDataForChart() {
        if (list != null) {
            ArrayList<String> labels = new ArrayList<>();
            ArrayList<BarEntry> groupTotalQuestion = new ArrayList<>();
            ArrayList<BarEntry> groupCorrectAnswer = new ArrayList<>();
            int index = 0;
            for (QuestionSubTypeModel q : list) {
                //Add label
                labels.add(q.getDetail());
                //Add number of answered questions
                groupTotalQuestion.add(new BarEntry(dbContext.getNumberQustionAnsweredByTypeAndSubType(typeCode,
                        q.getCode()), index));
                //Add number of correct questions
                groupCorrectAnswer.add(new BarEntry(dbContext.getNumberCorrectByTypeAndSubType(typeCode,
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

            BarData data = new BarData(labels, dataSets);
            data.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    if (value != 0) {
                        return String.format("%d", (int) value);
                    }
                    return "";
                }
            });

            horizontalBarChart.setData(data);
        }
    }

    private void getDataFromIntent() {
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            typeCode = bundle.getString("type");
            typeDetail = bundle.getString("detail");
            txtTitle.setText(typeDetail);
            if (typeCode != null) {
                list = dbContext.getQuestionTypeByCode(typeCode).getListSubType();
                if (list != null && list.size() != 0) {
                    listView.setAdapter(new ListSubTypeAdapter(getBaseContext(), list, typeCode));
                }
            }
        }
    }

    private void init() {
        //view
        horizontalBarChart = (HorizontalBarChart) findViewById(R.id.sub_type_chart);
        listView = (ListView) findViewById(R.id.list_sub_type);
        btnClose = (Button) findViewById(R.id.btn_close);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        addStyleForChart(horizontalBarChart);

        dbContext = DBContext.getInst();
    }

    private void showToast(String mess) {
        toast = Toast.makeText(this, mess, Toast.LENGTH_SHORT);
        toast.show();
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
                if (toast != null) {
                    toast.cancel();
                }
                int totalQues = dbContext.getNumberQuestionByTypeAndSubTypeCode(typeCode,
                        list.get(position).getCode());
                if (totalQues == 0) {
                    showToast(list.get(position).getDetail().toUpperCase()
                            + " is empty!");
                } else {
                    if (dbContext.getNumberQustionAnsweredByTypeAndSubType(typeCode, list.get(position).getCode()) > 0) {
                        Bundle bundle = new Bundle();
                        bundle.putString("type", typeCode);
                        bundle.putString("typeDetail", typeDetail);
                        bundle.putString("subTypeCode", list.get(position).getCode());
                        bundle.putString("subTypeDetail", list.get(position).getDetail());
                        Intent intent = new Intent(SubTypeSumaryActivity.this, ReviewSubTypeActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else {
                        showToast(list.get(position).getDetail().toUpperCase()
                                + " has no completed question!");
                    }
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
