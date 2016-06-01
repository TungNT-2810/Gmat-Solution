package org.iliat.gmat.activity;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.iliat.gmat.R;
import org.iliat.gmat.fragment.answer_question.RCQuestionFragment;
import org.iliat.gmat.fragment.answer_question.SCQuestionFragment;
import org.iliat.gmat.interf.ButtonControl;
import org.iliat.gmat.interf.CallBackAnswerQuestion;
import org.iliat.gmat.interf.ScreenManager;
import org.iliat.gmat.model.QuestionPackModel;
import org.iliat.gmat.view_model.QuestionPackViewModel;
import org.iliat.gmat.view_model.QuestionViewModel;

import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;

public class AnswerQuestionActivity
        extends AppCompatActivity
        implements ScreenManager, CallBackAnswerQuestion, ButtonControl, View.OnClickListener {
    public static final String KEY_TIME_AVERAGE = "ANSWER_QUESTION_KEY_TIME_AVERAGE";
    private long countTime = 0;
    private long timeQuestion = 0;
    private Realm realm;
    private int countAnswer = 0;
    private int maxQuestion = 16;
    private TextView txtCountTime;
    private TextView progressText;
    private ProgressBar progressBarDoing;
    private FrameLayout fragmentView;
    private Button btnNext;
    private FragmentManager mFragmentManager;
    private QuestionViewModel questionViewModel;
    private QuestionPackViewModel questionPackViewModel;
    private SCQuestionFragment scQuestionFragment;
    private RCQuestionFragment rcQuestionFragment;
    private ImageButton btnImage;
    private ImageButton btnExit;
    private boolean isGone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_question);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        setTitle("Quiz Test");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getDataFromIntent();
        inits();
        createTimer();
        fillData();
        openQuestionFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getDataFromIntent() {
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        String questionPackId = getQuestionPackFromBundle(bundle);
        realm = Realm.getDefaultInstance();
        QuestionPackModel questionPack = realm.where(QuestionPackModel.class).equalTo("id", questionPackId).findFirst();
        questionPackViewModel = new QuestionPackViewModel(questionPack, this);
        questionViewModel = questionPackViewModel.getFirstQuestionViewModel();
        countAnswer = 0;
        maxQuestion = questionPackViewModel.getNumberOfQuestions();
    }

    /**
     * LinhDQ changed
     * if type of a question is RC then open rcQuestionFragment
     * else open SCQuestionFragment
     */
    private void openQuestionFragment() {
        if (questionViewModel.getQuestion().getType().equalsIgnoreCase("RC")) {
            this.setButtonHideState(false);
            if (rcQuestionFragment == null) {
                rcQuestionFragment = new RCQuestionFragment();
                rcQuestionFragment.setButtonControl(this);
                this.setButtonNextState(0);
                rcQuestionFragment.setQuestion(this.questionViewModel);
                openFragment(rcQuestionFragment, true);
            } else {
                if (rcQuestionFragment.getmQuestionCRModel().getStimulus().equalsIgnoreCase(questionViewModel.getStimulus())) {
                    rcQuestionFragment.setStem(questionViewModel);
                } else {
                    rcQuestionFragment.setQuestion(this.questionViewModel);
                    openFragment(rcQuestionFragment, true);
                }
            }
        } else {
            this.setButtonHideState(true);
            scQuestionFragment = new SCQuestionFragment();
            scQuestionFragment.setButtonControl(this);
            this.setButtonNextState(0);
            scQuestionFragment.setQuestion(this.questionViewModel);
            openFragment(scQuestionFragment, true);
            rcQuestionFragment = null;
        }
    }

    private void inits() {
        txtCountTime = (TextView) this.findViewById(R.id.textView_count_down);
        progressText = (TextView) this.findViewById(R.id.text_progress);
        progressBarDoing = (ProgressBar) this.findViewById(R.id.doing_progressBar);
        fragmentView = (FrameLayout) this.findViewById(R.id.fragment_view_of_answer_question);
        btnNext = (Button) findViewById(R.id.btn_next);
        mFragmentManager = getFragmentManager();
        btnImage=(ImageButton)findViewById(R.id.btnImgButton);
        btnImage.setVisibility(View.GONE);
        btnExit =(ImageButton)findViewById(R.id.btnImgButtonExit);
        isGone=true;
        addListeners();
    }

    private void updateSubmitButtonText() {
        if (this.questionPackViewModel.isLastQuestionInPack(this.questionViewModel)) {
            btnNext.setText(getString(R.string.submit_question_pack));
        } else {
            btnNext.setText(getString(R.string.next_question));
        }
    }

    /**
     * Ham nay de fill data vao view
     * mỗi khi làm 1 câu thì thay gọi lại hàm này để cập nhật
     * ở fragment có thêm cái CallBackAnswerQuestion rồi truyền cái class này vào đấy
     */
    public void fillData() {
        progressText.setText(String.format("%d/%d", countAnswer + 1, maxQuestion));
        progressBarDoing.setMax(maxQuestion);
        progressBarDoing.setProgress(countAnswer + 1);
        updateSubmitButtonText();
    }

    /**
     * Hàm này để add listener cho cái button NEXT, listener có 2 cái, 1 cái ở activity 1 cái ở fragment
     */
    private void addListeners() {
        btnExit.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnImage.setOnClickListener(this);
    }


    /**
     * Ham nay de tao dong do dem gio
     */
    private void createTimer() {
        Timer T = new Timer();
        T.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (countTime / 3600 >= 1) {
                            txtCountTime.setText(String.format("%02d:%02d:%02d | Total: %02d:%02d:%02d", timeQuestion / 3600, (timeQuestion % 3600) / 60, timeQuestion % 60, countTime / 3600, (countTime % 3600) / 60, countTime % 60));
                        } else {
                            txtCountTime.setText(String.format("%02d:%02d | Total: %02d:%02d", timeQuestion / 60, timeQuestion % 60, countTime / 60, countTime % 60));
                        }
                        countTime++;
                        timeQuestion++;
                    }
                });
            }
        }, 1000, 1000);
    }


    @Override
    public void openFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.trans_left_in, R.anim.trans_left_out);
        fragmentTransaction.replace(R.id.fragment_view_of_answer_question, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(fragment.getClass().getName());
        }
        fragmentTransaction.commit();
    }

    @Override
    public void showDialogFragment(DialogFragment dialogFragment, String tag) {

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    public boolean back() {
        return false;
    }

    @Override
    public void setTitleOfActionBar(String titles) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(titles);
        }
    }

    @Override
    public void goToActivity(Class activityClass, Bundle bundle) {
        Intent intent = new Intent(this, activityClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtras(bundle);
        getApplicationContext().startActivity(intent);
        this.finish();
    }

    private static final String QUESTION_PACK_BUNDLE_STRING = "question pack";

    public static Bundle buildBundle(String questionPackId) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(QUESTION_PACK_BUNDLE_STRING, questionPackId);
        return bundle;
    }

    public static String getQuestionPackFromBundle(Bundle bundle) {
        return (String) bundle.getSerializable(QUESTION_PACK_BUNDLE_STRING);
    }

    @Override
    public void setButtonNextState(int state) {
        if (state == 1) {//enable
            btnNext.setEnabled(true);
        } else {
            btnNext.setEnabled(false);
        }
    }

    @Override
    public void setButtonHideState(boolean isGone) {
        if(!isGone){
            btnImage.setVisibility(View.VISIBLE);
        }else{
            btnImage.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnImgButtonExit:{
                new AlertDialog.Builder(this)
                        .setTitle("Finish Quiz")
                        .setMessage("Are you sure you want to finish?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent=new Intent(AnswerQuestionActivity.this,MainActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                break;
            }
            case R.id.btn_next:{
                questionViewModel.saveUserAnswer();
                if (questionPackViewModel.isLastQuestionInPack(questionViewModel)) {
                    realm.beginTransaction();
                    questionViewModel.getQuestion().setTimeToFinish((int) timeQuestion);
                    realm.copyToRealmOrUpdate(questionViewModel.getQuestion());
                    realm.commitTransaction();
                    Bundle bundle = ScoreActivity.buildBundle(questionPackViewModel.getQuestionPack().getId());
                    bundle.putInt(KEY_TIME_AVERAGE, (int) (countTime / 10));
                    goToActivity(ScoreActivity.class, bundle);
                } else {
                    countAnswer++;
                    fillData();
                    realm.beginTransaction();
                    questionViewModel.getQuestion().setTimeToFinish((int) timeQuestion);
                    realm.copyToRealmOrUpdate(questionViewModel.getQuestion());
                    realm.commitTransaction();
                    questionViewModel = questionPackViewModel.getNextQuestionViewModel(questionViewModel);
                    timeQuestion = 0;
                    openQuestionFragment();
                }
                updateSubmitButtonText();
                break;
            }
            case R.id.btnImgButton:{
                rcQuestionFragment.setQuestionState(isGone);
                if (isGone) {
                    btnImage.setImageResource(R.drawable.ic_vertical_align_top_white_24dp);
                    isGone = false;
                } else {
                    btnImage.setImageResource(R.drawable.ic_vertical_align_bottom_white_24dp);
                    isGone = true;
                }
                break;
            }
            default:break;
        }
    }
}
