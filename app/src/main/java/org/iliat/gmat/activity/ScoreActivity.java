package org.iliat.gmat.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

public class ScoreActivity extends AppCompatActivity{
    private static final String TAG = ScoreActivity.class.toString();
    public static final String TAG_QUESTION_PACK_VIEW_MODEL = "QUESTION_PACK_VIEW_MODEL";
    public static final String SCOREACTIIVTY_POSITION = "SCOREACTIIVTY_POSITION";

    private int yourScore = 10;//so cau tra loi dung
    private int maxScore = 16;//so cau hoi toi da
    private int countStar = 0;//so cau danh dau sao
    private int countGreyTag = 0;//tam thoi truyen vao 0
    private int countGreenTag = 0;//nhu tren
    private int countYellowTag = 0;//nt
    private int countRedTag = 0;//nt
    private int countTimeAverage = 0;//thoi gian lam trung binh 1 cau

    Realm realm;

    public void setCountTimeAverage(int countTimeAverage) {
        this.countTimeAverage = countTimeAverage;
    }

    ArcProgress arcProgress;
    TextView txtCountStar;
    TextView txtCountGreyTag;
    TextView txtCountGreenTag;
    TextView txtCountYellowTag;
    TextView txtCountRedTag;
    TextView txtCountTimeAverage;
    ListView ltvQuestionAnswerSummary;

    QuestionPackViewModel questionPackViewModel;

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
        setContentView(R.layout.activity_score);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_score);
        setSupportActionBar(toolbar);
        setTitle("Score review");
        connectView();//ket noi xml voi java
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

    private void getDataFromIntent() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String questionPackId = getDataFromBundle(bundle);
        QuestionPackModel questionPack = realm.where(QuestionPackModel.class).equalTo("id",questionPackId).findFirst();
        questionPackViewModel = new QuestionPackViewModel(questionPack, this);
        countTimeAverage = bundle.getInt(AnswerQuestionActivity.KEY_TIME_AVERAGE);
        for (int i = 0; i < questionPackViewModel.getQuestionViewModels().size(); i++){
            Log.d("HungTD","" + questionPackViewModel.getQuestionViewModels().get(i).getUserAnswerIndex());
        }

    }

    private void connectView(){
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
     * @param
     */
    private void fillData(){
        yourScore = questionPackViewModel.getNumberOfCorrectAnswers();
        maxScore = questionPackViewModel.getNumberOfQuestions();
        txtCountStar.setText(String.valueOf(countStar));
        txtCountGreyTag.setText(String.valueOf(countGreyTag));
        txtCountGreenTag.setText(String.valueOf(countGreenTag));
        txtCountRedTag.setText(String.valueOf(countRedTag));
        txtCountYellowTag.setText(String.valueOf(countYellowTag));
        txtCountTimeAverage.setText(String.valueOf(countTimeAverage));

        arcProgress.setBottomText(String.format("%d / %d", yourScore, maxScore));
        arcProgress.setProgress((int) (yourScore * 100.0f / maxScore));

        ltvQuestionAnswerSummary.setAdapter(new QuestionAnswerSummaryAdapter(this,
                R.layout.list_item_score_question_answer_summary,
                questionPackViewModel.getQuestionViewModels()));
        ltvQuestionAnswerSummary.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /*TODO*/
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ScoreActivity.this, QuestionReviewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(SCOREACTIIVTY_POSITION, position);
                bundle.putString(TAG_QUESTION_PACK_VIEW_MODEL, questionPackViewModel.getId());
                intent.putExtra(TAG_QUESTION_PACK_VIEW_MODEL, bundle);
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
        return (String)bundle.getSerializable(QUESTION_PACK_VIEW_MODEL_BUNDLE_STRING);
    }
}
