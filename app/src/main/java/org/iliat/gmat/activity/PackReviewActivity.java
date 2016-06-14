package org.iliat.gmat.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.ArcProgress;

import org.iliat.gmat.R;
import org.iliat.gmat.adapter.QuestionAnswerSummaryAdapter;
import org.iliat.gmat.model.QuestionPackModel;
import org.iliat.gmat.view_model.QuestionPackViewModel;

import io.realm.Realm;

public class PackReviewActivity extends AppCompatActivity {
    private static final String TAG = PackReviewActivity.class.toString();
    public static final String TAG_QUESTION_PACK_VIEW_MODEL = "QUESTION_PACK_VIEW_MODEL";
    public static final String SCOREACTIIVTY_POSITION = "SCOREACTIIVTY_POSITION";

    private int yourScore = 10;//so cau tra loi dung
    private int maxScore = 16;//so cau hoi toi da
    private int countTimeAverage = 0;//thoi gian lam trung binh 1 cau

    private Realm realm;

    private ArcProgress arcProgress;
    private TextView txtCountStar;
    private TextView txtCountGreyTag;
    private TextView txtCountGreenTag;
    private TextView txtCountYellowTag;
    private TextView txtCountRedTag;
    private TextView txtCountTimeAverage;
    private ListView ltvQuestionAnswerSummary;

    private QuestionPackViewModel questionPackViewModel;

    public int getYourScore() {
        return yourScore;
    }

    public void setYourScore(int yourScore) {
        this.yourScore = yourScore;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_score);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Package review");
        init();
        overridePendingTransition(R.anim.trans_in, R.anim.trans_out);
    }

    @Override
    protected void onStart() {
        super.onStart();
        realm = Realm.getDefaultInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDataFromIntent();
        fillData();
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

    private void getDataFromIntent() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String questionPackId = getDataFromBundle(bundle);
        QuestionPackModel questionPack = realm.where(QuestionPackModel.class).equalTo("id", questionPackId).findFirst();
        questionPackViewModel = new QuestionPackViewModel(questionPack, this);
        countTimeAverage = bundle.getInt(AnswerQuestionActivity.KEY_TIME_AVERAGE);
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

    /**
     * Hàm này để đổ data vào các view
     *
     * @param
     */
    private void fillData() {
        yourScore = questionPackViewModel.getNumberOfCorrectAnswers();
        maxScore = questionPackViewModel.getNumberOfQuestions();
        txtCountStar.setText(String.valueOf(questionPackViewModel.getStarTag()));
        txtCountGreyTag.setText(String.valueOf(questionPackViewModel.getGreyTag()));
        txtCountGreenTag.setText(String.valueOf(questionPackViewModel.getGreenTag()));
        txtCountRedTag.setText(String.valueOf(questionPackViewModel.getRedTag()));
        txtCountYellowTag.setText(String.valueOf(questionPackViewModel.getYellowTag()));
        txtCountTimeAverage.setText(String.format("%dm %ds", countTimeAverage / 60, countTimeAverage % 60));

        arcProgress.setBottomText(String.format("%d / %d", yourScore, maxScore));
        arcProgress.setProgress((int) (yourScore * 100.0f / maxScore));

        ltvQuestionAnswerSummary.setAdapter(new QuestionAnswerSummaryAdapter(this,
                R.layout.list_item_score_question_answer_summary,
                questionPackViewModel.getQuestionViewModels()));
        ltvQuestionAnswerSummary.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PackReviewActivity.this, QuestionReviewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean("PackReviewActivity", true);
                bundle.putInt("position", position);
                bundle.putString(TAG_QUESTION_PACK_VIEW_MODEL, questionPackViewModel.getId());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private static final String QUESTION_PACK_VIEW_MODEL_BUNDLE_STRING = "Question_pack_view_model";

    public static Bundle buildBundle(String questionPackId) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(QUESTION_PACK_VIEW_MODEL_BUNDLE_STRING, questionPackId);
        return bundle;
    }

    public static String getDataFromBundle(Bundle bundle) {
        return (String) bundle.getSerializable(QUESTION_PACK_VIEW_MODEL_BUNDLE_STRING);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_back_in, R.anim.trans_back_out);
    }
}
