package org.iliat.gmat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.ArcProgress;

import org.iliat.gmat.R;
import org.iliat.gmat.adapter.ListQuestionReviewBySubTypeAdapter;
import org.iliat.gmat.constant.Constant;
import org.iliat.gmat.db_connect.DBContext;
import org.iliat.gmat.model.QuestionModel;

import io.realm.RealmResults;

/**
 * Created by MrBom on 6/11/2016.
 */
public class ReviewSubTypeActivity extends AppCompatActivity {
    private ArcProgress arcProgress;
    private TextView txtCountStar;
    private TextView txtCountGreyTag;
    private TextView txtCountGreenTag;
    private TextView txtCountYellowTag;
    private TextView txtCountRedTag;
    private TextView txtCountTimeAverage;
    private ListView ltvQuestionAnswerSummary;

    private String typeCode;
    private String typeDetail;
    private String subTypeCode;
    private String subTypeDetail;

    private RealmResults<QuestionModel> list;
    private ListQuestionReviewBySubTypeAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_score);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();

        //start animation
        overridePendingTransition(R.anim.trans_in, R.anim.trans_out);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDataFromIntent();
        bindDataToListView();
        bindDataToTopView();
        addListener();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        txtCountStar = (TextView) this.findViewById(R.id.txtCountStar);
        txtCountGreyTag = (TextView) this.findViewById(R.id.txtCountGrey);
        txtCountGreenTag = (TextView) this.findViewById(R.id.txtCountGreen);
        txtCountYellowTag = (TextView) this.findViewById(R.id.txtCountYellow);
        txtCountRedTag = (TextView) this.findViewById(R.id.txtCountRed);
        txtCountTimeAverage = (TextView) this.findViewById(R.id.txtTime);
        ltvQuestionAnswerSummary = (ListView) this.findViewById(R.id.ltv_score_question_answer_summary);
        arcProgress = (ArcProgress) this.findViewById(R.id.arc_progress);
    }

    private void bindDataToTopView() {
        //bind to progress
        int total = DBContext.getNumberQuestionByTypeAndSubTypeCode(typeCode, subTypeCode);
        arcProgress.setMax(100);
        arcProgress.setProgress(list.size() * 100 / total);
        arcProgress.setBottomText(list.size() + "/" + total);
    }

    private void bindDataToListView() {
        if (list != null) {
            adapter = new ListQuestionReviewBySubTypeAdapter(list, this.getBaseContext());
            ltvQuestionAnswerSummary.setAdapter(adapter);
        }
        txtCountGreenTag.setText(String.valueOf(DBContext.getNumberOfTagByTagId(Constant.TAG_GREEN,
                typeCode, subTypeCode)));
        txtCountGreyTag.setText(String.valueOf(DBContext.getNumberOfTagByTagId(Constant.TAG_GREY,
                typeCode, subTypeCode)));
        txtCountRedTag.setText(String.valueOf(DBContext.getNumberOfTagByTagId(Constant.TAG_RED,
                typeCode, subTypeCode)));
        txtCountYellowTag.setText(String.valueOf(DBContext.getNumberOfTagByTagId(Constant.TAG_YELLOW,
                typeCode, subTypeCode)));
        txtCountStar.setText(String.valueOf(DBContext.getNumberOfStar(true,
                typeCode, subTypeCode)));
        int totalTime = 0;
        for (QuestionModel q : list) {
            totalTime += q.getTimeToFinish();
        }
        txtCountTimeAverage.setText(String.format("%dm %ds", (totalTime / list.size()) / 60,
                (totalTime / list.size()) % 60));
    }

    private void addListener() {
        ltvQuestionAnswerSummary.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putInt("position", position);
                bundle.putString("typeCode", typeCode);
                bundle.putString("subTypeCode", subTypeCode);
                bundle.putString("subTypeDetail", subTypeDetail);
                Intent intent = new Intent(ReviewSubTypeActivity.this, QuestionReviewActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void getDataFromIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            typeCode = bundle.getString("type");
            typeDetail = bundle.getString("typeCode");
            subTypeCode = bundle.getString("subTypeCode");
            subTypeDetail = bundle.getString("subTypeDetail");
            if (typeCode != null && subTypeCode != null) {
                list = DBContext.getAllQuestionAnsweredByTypeAndSubType(typeCode, subTypeCode);
            }
            setTitle(subTypeDetail+" Review");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_back_in, R.anim.trans_back_out);
    }
}
