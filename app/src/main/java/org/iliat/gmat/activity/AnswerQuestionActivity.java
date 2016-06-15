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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.iliat.gmat.R;
import org.iliat.gmat.constant.Constant;
import org.iliat.gmat.db_connect.DBContext;
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

/**
 * Modified by Linh DQ
 */

public class AnswerQuestionActivity
        extends AppCompatActivity
        implements ScreenManager, CallBackAnswerQuestion, ButtonControl, View.OnClickListener {
    public static final String KEY_TIME_AVERAGE = "ANSWER_QUESTION_KEY_TIME_AVERAGE";
    private static final String QUESTION_PACK_BUNDLE_STRING = "question pack";
    private static final String LOG_TAG = AnswerQuestionActivity.class.getSimpleName();
    private long totalTime;
    private long timeForEachQuestion;
    private int countAnswer;
    private int maxQuestion;
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
    private ImageButton btnExpand;
    private ImageButton btnExit;
    private boolean isGone;

    private String currentStimulus;
    private String nextStimulus;

    private DBContext dbContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_question);
        init();
        getDataFromIntent();
        overridePendingTransition(R.anim.trans_in, R.anim.trans_out);
    }

    @Override
    protected void onResume() {
        super.onResume();
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

    private void startProcess() {
        createTimer();
        fillData();
        openQuestionFragment();
    }

    /**
     * isCompleted() function will be return TRUE if current question pack is already done.
     * if isCompleted() == true then show a dialog to confirm that user want to startOver or No,
     * if user choose startOver then clear all userAnswer in this pack and start over this quiz.
     * if No then come back home screen.
     * <p/>
     * If isCompleteed()==false then check isNew() function. this function will return True if
     * current pack is new.
     * show a dialog and provide functions do continue and do again if isNew return False
     * else start quiz from first question in this pack.
     */
    private void getDataFromIntent() {
        Log.d(LOG_TAG, "getDataFromIntent");
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        String questionPackId = getQuestionPackFromBundle(bundle);
        final QuestionPackModel questionPack = dbContext.getQuestionPackModelById(questionPackId);
        questionPackViewModel = new QuestionPackViewModel(questionPack, this);
        maxQuestion = questionPackViewModel.getNumberOfQuestions();

        if (questionPackViewModel.isCompleted()) {
            totalTime = questionPack.getTotalTimeToFinish();
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.string_title_confirmation))
                    .setMessage(getString(R.string.string_message_pack_already_done))
                    .setPositiveButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(AnswerQuestionActivity.this, MainActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.trans_back_in, R.anim.trans_back_out);
                        }
                    })
                    .setNegativeButton("Review", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Bundle bundle = PackReviewActivity.buildBundle(questionPack.getId());
                            bundle.putInt(KEY_TIME_AVERAGE, (int) (totalTime / maxQuestion));
                            goToActivity(PackReviewActivity.class, bundle);
                            overridePendingTransition(R.anim.trans_in, R.anim.trans_out);
                        }
                    })
                    .setNeutralButton("Start over", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //save total time to finish
                            dbContext.saveTotalTimeToFinish(questionPackViewModel ,0);

                            questionPackViewModel.clearUserAnswers();
                            questionViewModel = questionPackViewModel.getFirstQuestionViewModel();
                            countAnswer = 0;
                            startProcess();
                        }
                    })
                    .show();
        } else {
            if (!questionPackViewModel.isNew()) {
                new AlertDialog.Builder(this)
                        .setTitle(R.string.string_title_confirmation)
                        .setMessage(getString(R.string.string_message_pack_not_done))
                        .setPositiveButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(AnswerQuestionActivity.this, MainActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.trans_back_in, R.anim.trans_back_out);
                            }
                        })
                        .setNegativeButton("Continue", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (questionPackViewModel.getContinueQuestionViewModel() != null) {
                                    totalTime = questionPack.getTotalTimeToFinish();
                                    questionViewModel = questionPackViewModel.getContinueQuestionViewModel();
                                    countAnswer = questionPackViewModel.getNumberQuestionAnswered();
                                    startProcess();
                                } else {
                                    Intent intent = new Intent(AnswerQuestionActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.trans_back_in, R.anim.trans_back_out);
                                }
                            }
                        })
                        .setNeutralButton("Do again", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //save total time to finish
                                dbContext.saveTotalTimeToFinish(questionPackViewModel ,0);

                                questionPackViewModel.clearUserAnswers();
                                questionViewModel = questionPackViewModel.getFirstQuestionViewModel();
                                countAnswer = 0;
                                startProcess();
                            }
                        })
                        .show();
            } else {
                questionViewModel = questionPackViewModel.getFirstQuestionViewModel();
                countAnswer = 0;
                startProcess();
            }
        }
    }

    /**
     * LinhDQ changed
     * if type of a question is RC then open rcQuestionFragment
     * else open SCQuestionFragment
     */
    private void openQuestionFragment() {
        Log.d(LOG_TAG, "openQuestionFragment");
        if (questionViewModel != null) {
            if (questionViewModel.getQuestion().getType().equalsIgnoreCase(Constant.TYPE_RC)) {
                this.setButtonHideState(false);
                if (rcQuestionFragment == null) {
                    rcQuestionFragment = new RCQuestionFragment();
                    rcQuestionFragment.setButtonControl(this);
                    this.setButtonNextState(0);
                    rcQuestionFragment.setQuestion(this.questionViewModel);
                    openFragment(rcQuestionFragment, true);
                } else {
                    //if current stimulus and next stimulus are same then change stem only
                    currentStimulus = rcQuestionFragment.getmQuestionCRModel().getStimulus();
                    nextStimulus = questionViewModel.getStimulus();
                    if (currentStimulus.equalsIgnoreCase(nextStimulus)) {
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
    }

    private void init() {
        txtCountTime = (TextView) this.findViewById(R.id.textView_count_down);
        progressText = (TextView) this.findViewById(R.id.text_progress);
        progressBarDoing = (ProgressBar) this.findViewById(R.id.doing_progressBar);
        fragmentView = (FrameLayout) this.findViewById(R.id.fragment_view_of_answer_question);
        btnNext = (Button) findViewById(R.id.btn_next);
        btnNext.setEnabled(false);
        mFragmentManager = getFragmentManager();
        btnExpand = (ImageButton) findViewById(R.id.btnExpand);
        btnExpand.setVisibility(View.GONE);
        btnExit = (ImageButton) findViewById(R.id.btnImgButtonExit);
        isGone = true;
        addListeners();
        dbContext=DBContext.getInst();
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
        btnExpand.setOnClickListener(this);
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
                        if (totalTime / 3600 >= 1) {
                            txtCountTime.setText(String.format("Total: %02d:%02d:%02d", timeForEachQuestion % 60, totalTime / 3600, (totalTime % 3600) / 60, totalTime % 60));
                        } else {
                            txtCountTime.setText(String.format("Total: %02d:%02d", totalTime / 60, totalTime % 60));
                        }
                        totalTime++;
                        timeForEachQuestion++;
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
        if (!isGone) {
            btnExpand.setVisibility(View.VISIBLE);
        } else {
            btnExpand.setVisibility(View.GONE);
        }
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnImgButtonExit: {
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.string_title_finish_quiz))
                        .setMessage(getString(R.string.string_message_finish_quiz))
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //save total time to finish
                                dbContext.saveTotalTimeToFinish(questionPackViewModel ,totalTime);
                                //
                                Intent intent = new Intent(AnswerQuestionActivity.this, MainActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.trans_back_in, R.anim.trans_back_out);
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                break;
            }
            case R.id.btn_next: {
                //save user answer and time to finish to db when button Next is clicked
                questionViewModel.saveUserAnswer();
                //save time to finish for each question
                dbContext.saveTimeFinishForEachQuestion(questionViewModel,timeForEachQuestion);
                //save tag default for each question
                if (questionViewModel.getQuestion().getTagId() == 0) {
                    dbContext.saveTagForEachQuestion(questionViewModel,Constant.TAG_GREY);
                }

                if (questionPackViewModel.isLastQuestionInPack(questionViewModel)) {
                    //save total time to finish
                    dbContext.saveTotalTimeToFinish(questionPackViewModel ,totalTime);

                    Bundle bundle = PackReviewActivity.buildBundle(questionPackViewModel.getQuestionPack().getId());
                    bundle.putInt(KEY_TIME_AVERAGE, (int) (totalTime / 10));
                    goToActivity(PackReviewActivity.class, bundle);
                    overridePendingTransition(R.anim.trans_in, R.anim.trans_out);
                } else {
                    countAnswer++;
                    fillData();
                    questionViewModel = questionPackViewModel.getNextQuestionViewModel(questionViewModel);
                    timeForEachQuestion = 0;
                    openQuestionFragment();
                }
                updateSubmitButtonText();
                break;
            }
            case R.id.btnExpand: {
                rcQuestionFragment.setQuestionState(isGone);
                if (isGone) {
                    btnExpand.setImageResource(R.drawable.ic_vertical_align_top_white_24dp);
                    isGone = false;
                } else {
                    btnExpand.setImageResource(R.drawable.ic_vertical_align_bottom_white_24dp);
                    isGone = true;
                }
                break;
            }
            default:
                break;
        }
    }
}
