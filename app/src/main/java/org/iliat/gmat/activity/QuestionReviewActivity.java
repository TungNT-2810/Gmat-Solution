package org.iliat.gmat.activity;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.iliat.gmat.R;

import org.iliat.gmat.constant.Constant;
import org.iliat.gmat.database.Question;
import org.iliat.gmat.interf.ScreenManager;
import org.iliat.gmat.item_view.AnswerCRQuestionReview;
import org.iliat.gmat.model.QuestionModel;
import org.iliat.gmat.model.QuestionPackModel;
import org.iliat.gmat.view_model.QuestionPackViewModel;
import org.iliat.gmat.view_model.QuestionViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.github.kexanie.library.MathView;
import io.realm.Realm;


/**
 * Khi sử dụng nhớ set QuestionPack cho nó
 */
/*TODO*/
public class QuestionReviewActivity extends AppCompatActivity implements ScreenManager, View.OnClickListener {

    //question Pack của cái activity này
    private QuestionPackViewModel mQuestionPack;
    private android.app.FragmentManager mFragmentManager;
    TextView isCorrect;
    private RelativeLayout topController;
    TextView txtProcess;
    private Button btnNext;
    private Button btnBack;
    private ImageButton btnShare;
    private ImageButton btnExpandStimulus;
    private ImageButton btn_open;
    private Realm realm;
    private int currentItem;
    private int totalItem;
    private ScrollView scrollView;
    private boolean isGone;
    private int position;
    private View view;
    private ImageButton btn_tag_grey;
    private ImageButton btn_tag_green;
    private ImageButton btn_tag_yellow;
    private ImageButton btn_tag_red;
    private ImageButton btn_tag_star;
    private ImageButton btn_share;
    private boolean isOpen = false;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;
    private TextView tv_grey;
    private TextView tv_green;
    private TextView tv_yellow;
    private TextView tv_red;
    private TextView tv_star;
    private TextView tv_share;
    private RelativeLayout layout_tag;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_review_fragment);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        PlaceholderFragment.context = this;
        inits();
        addListenerForTabChange();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_question_review, menu);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("ON PAUSE", "ON PAUSE");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("ON STOP", "ON STOP");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                break;
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        realm = Realm.getDefaultInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = this.getIntent();
        String questionPackID = (intent.getBundleExtra(ScoreActivity.TAG_QUESTION_PACK_VIEW_MODEL))
                .getString(ScoreActivity.TAG_QUESTION_PACK_VIEW_MODEL);

        mQuestionPack = new QuestionPackViewModel(realm.where(QuestionPackModel.class).equalTo("id", questionPackID).findFirst());
        Log.d("TAG", mQuestionPack.getQuestionViewModels().get(0).getAnswerChoices().get(0).getChoise());
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), mQuestionPack);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        Bundle bundle = getIntent().getBundleExtra(ScoreActivity.TAG_QUESTION_PACK_VIEW_MODEL);
        int position = bundle.getInt(ScoreActivity.SCOREACTIIVTY_POSITION);
        if (position != -1) {
            mViewPager.setCurrentItem(position);
            updateTopView(position);
            this.position = position;
            setImageButtonState(position);
        }
        isGone = true;
    }


    private void inits() {
        isCorrect = (TextView) findViewById(R.id.txt_correct);
        isCorrect.setTypeface(Typeface.DEFAULT_BOLD);
        mViewPager = (ViewPager) findViewById(R.id.container);
        mFragmentManager = getFragmentManager();
        topController = (RelativeLayout) findViewById(R.id.top_controller);
        txtProcess = (TextView) findViewById(R.id.txt_process);
        btnNext = (Button) findViewById(R.id.btn_next);
        btnBack = (Button) findViewById(R.id.btn_back);
        btn_open = (ImageButton) findViewById(R.id.btn_open);
        btn_tag_grey = (ImageButton) findViewById(R.id.btn_tag_grey);
        btn_tag_green = (ImageButton) findViewById(R.id.btn_tag_green);
        btn_tag_yellow = (ImageButton) findViewById(R.id.btn_tag_yellow);
        btn_tag_red = (ImageButton) findViewById(R.id.btn_tag_red);
        btn_tag_star = (ImageButton) findViewById(R.id.btn_tag_star);
        btn_share = (ImageButton) findViewById(R.id.btn_share);

        tv_grey = (TextView) findViewById(R.id.tv_grey);
        tv_grey.setVisibility(View.INVISIBLE);
        tv_green = (TextView) findViewById(R.id.tv_green);
        tv_green.setVisibility(View.INVISIBLE);
        tv_yellow = (TextView) findViewById(R.id.tv_yellow);
        tv_yellow.setVisibility(View.INVISIBLE);
        tv_red = (TextView) findViewById(R.id.tv_red);
        tv_red.setVisibility(View.INVISIBLE);
        tv_star = (TextView) findViewById(R.id.tv_star);
        tv_star.setVisibility(View.INVISIBLE);
        tv_share = (TextView) findViewById(R.id.tv_share);
        tv_share.setVisibility(View.INVISIBLE);
        layout_tag = (RelativeLayout) findViewById(R.id.layout_tag);
        layout_tag.setVisibility(View.INVISIBLE);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        PlaceholderFragment.context = this;
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_backward);
        btnNext.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btn_open.setOnClickListener(this);
        btn_tag_grey.setOnClickListener(this);
        btn_tag_grey.setVisibility(View.INVISIBLE);
        btn_tag_star.setOnClickListener(this);
        btn_tag_star.setVisibility(View.INVISIBLE);
        btn_tag_red.setOnClickListener(this);
        btn_tag_red.setVisibility(View.INVISIBLE);
        btn_tag_yellow.setOnClickListener(this);
        btn_tag_yellow.setVisibility(View.INVISIBLE);
        btn_tag_green.setOnClickListener(this);
        btn_tag_green.setVisibility(View.INVISIBLE);
        btn_share.setOnClickListener(this);
        btn_share.setVisibility(View.INVISIBLE);
        btnShare = (ImageButton) findViewById(R.id.btn_share);
        btnExpandStimulus = (ImageButton) findViewById(R.id.btnImgButton);
        btnExpandStimulus.setVisibility(View.GONE);
        btnBack = (Button) findViewById(R.id.btn_back);
        scrollView = (ScrollView) mViewPager.findViewById(R.id.scrollViewAnswer1);
        btnExpandStimulus.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        isGone = true;
    }

    private void openButton(){
        if (!isOpen){
            layout_tag.setVisibility(View.VISIBLE);
            btn_open.startAnimation(rotate_forward);
            tv_grey.startAnimation(AnimationUtils.loadAnimation(QuestionReviewActivity.this, android.R.anim.slide_in_left));
            tv_grey.setVisibility(View.VISIBLE);
            btn_tag_grey.startAnimation(fab_open);
            btn_tag_grey.setClickable(true);
            tv_green.startAnimation(AnimationUtils.loadAnimation(QuestionReviewActivity.this, android.R.anim.slide_in_left));
            tv_green.setVisibility(View.VISIBLE);
            btn_tag_green.startAnimation(fab_open);
            btn_tag_green.setClickable(true);
            tv_yellow.startAnimation(AnimationUtils.loadAnimation(QuestionReviewActivity.this, android.R.anim.slide_in_left));
            tv_yellow.setVisibility(View.VISIBLE);
            btn_tag_yellow.startAnimation(fab_open);
            btn_tag_yellow.setClickable(true);
            tv_red.startAnimation(AnimationUtils.loadAnimation(QuestionReviewActivity.this, android.R.anim.slide_in_left));
            tv_red.setVisibility(View.VISIBLE);
            btn_tag_red.startAnimation(fab_open);
            btn_tag_red.setClickable(true);
            tv_star.startAnimation(AnimationUtils.loadAnimation(QuestionReviewActivity.this, android.R.anim.slide_in_left));
            tv_star.setVisibility(View.VISIBLE);
            btn_tag_star.startAnimation(fab_open);
            btn_tag_star.setClickable(true);
            tv_share.startAnimation(AnimationUtils.loadAnimation(QuestionReviewActivity.this, android.R.anim.slide_in_left));
            tv_share.setVisibility(View.VISIBLE);
            btn_share.startAnimation(fab_open);
            btn_share.setClickable(true);
            isOpen = true;
        }
        else {
            layout_tag.setAnimation(AnimationUtils.loadAnimation(QuestionReviewActivity.this, R.anim.anim_down_gone));
            layout_tag.setVisibility(View.INVISIBLE);
            btn_open.startAnimation(rotate_backward);
            btn_tag_grey.setClickable(false);
            btn_tag_green.setClickable(false);
            btn_tag_yellow.setClickable(false);
            btn_tag_red.setClickable(false);
            btn_tag_star.setClickable(false);
            btn_share.setClickable(false);
            isOpen = false;
        }
    }

    private void closeButton(){
        if (isOpen){
            layout_tag.setAnimation(AnimationUtils.loadAnimation(QuestionReviewActivity.this, R.anim.anim_down_gone));
            layout_tag.setVisibility(View.INVISIBLE);
            btn_open.startAnimation(rotate_backward);
            btn_tag_grey.setClickable(false);
            btn_tag_green.setClickable(false);
            btn_tag_yellow.setClickable(false);
            btn_tag_red.setClickable(false);
            btn_tag_star.setClickable(false);
            btn_share.setClickable(false);
            isOpen = false;
        }
    }


    private Bitmap screenShot(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    private static File saveBitmap(Bitmap bm, String fileName) {
        final String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Screenshots";
        File dir = new File(path);
        if (!dir.exists())
            dir.mkdirs();
        File file = new File(dir, fileName);
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 90, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    private void updateTopView(int position) {
        String str = String.format("%d/%d | Time: %d:%d", position + 1, mQuestionPack.getQuestionViewModels().size(),
                mQuestionPack.getQuestionViewModels().get(position).getTimeToFinish() / 60,
                mQuestionPack.getQuestionViewModels().get(position).getTimeToFinish() % 60);
        txtProcess.setText(str);
        if (mQuestionPack.getQuestionViewModels().get(position).getUserChoise()
                == mQuestionPack.getQuestionViewModels().get(position).getQuestion().getRightAnswerIndex()) {
            isCorrect.setText("Correct");
            isCorrect.setTextColor(getResources().getColor(R.color.color_green_500));
            txtProcess.setTextColor(getResources().getColor(R.color.color_green_500));
            topController.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
        } else {
            isCorrect.setTextColor(getResources().getColor(R.color.color_red_500));
            txtProcess.setTextColor(getResources().getColor(R.color.color_red_500));
            isCorrect.setText("Incorrect");
            topController.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
        }
    }

    private void setImageButtonState(int position) {
        if (mQuestionPack.getQuestionViewModels().get(position).getQuestion().getType().equals("RC")) {
            btnExpandStimulus.setVisibility(View.VISIBLE);
            scrollView = (ScrollView) mViewPager.findViewById(R.id.scrollViewAnswer1);
        } else {
            btnExpandStimulus.setVisibility(View.GONE);
        }
    }

    /**
     * thêm listener cho sự kiện change tab
     */
    private void addListenerForTabChange() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                totalItem = mQuestionPack.getNumberOfQuestions();
                closeButton();
                if (position == 0) {
                    btnBack.setEnabled(false);
                } else {
                    btnBack.setEnabled(true);
                }
                if (position == totalItem - 1) {
                    btnNext.setEnabled(false);
                    btnBack.setEnabled(true);
                }
                if (position > 0 && position < totalItem - 1) {
                    btnBack.setEnabled(true);
                    btnNext.setEnabled(true);
                }
            }

            @Override
            public void onPageSelected(int position) {
                updateTopView(position);
                Log.d("pos", mViewPager.getCurrentItem() + "");
                setImageButtonState(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public void openFragment(android.app.Fragment fragment, boolean addToBackStack) {

    }

    @Override
    public void showDialogFragment(DialogFragment dialogFragment, String tag) {
        dialogFragment.show(mFragmentManager, tag);
    }

    @Override
    public boolean back() {
        return false;
    }

    @Override
    public void setTitleOfActionBar(String titles) {

    }

    @Override
    public void goToActivity(Class activityClass, Bundle bundle) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnImgButton: {
                Fragment fragment = mSectionsPagerAdapter.getFragment(this.mViewPager.getCurrentItem());
                if (fragment instanceof PlaceholderFragmentRC) {
                    isGone = !isGone;
                    ((PlaceholderFragmentRC) fragment).showHideScrollView(isGone);
                    if(!isGone){
                        btnExpandStimulus.setImageResource(R.drawable.ic_vertical_align_top_white_24dp);
                    }else{
                        btnExpandStimulus.setImageResource(R.drawable.ic_vertical_align_bottom_white_24dp);
                    }
                }
                break;
            }
            case R.id.btn_share: {
                Bitmap bm = screenShot(QuestionReviewActivity.this.mViewPager);
                File file = saveBitmap(bm, "mantis_image.png");
                Log.i("chase", "filepath: " + file.getAbsolutePath());
                Uri uri = Uri.fromFile(new File(file.getAbsolutePath()));
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out my app.");
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                shareIntent.setType("image/*");
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(shareIntent, "share via"));
                break;
            }
            case R.id.btn_next: {
                closeButton();
                Log.d("position", position + "");
                isGone = true;
                btnExpandStimulus.setImageResource(R.drawable.ic_vertical_align_bottom_white_24dp);
                if (mViewPager.getCurrentItem() + 1 < mQuestionPack.getQuestionViewModels().size()) {
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                }
                break;
            }
            case R.id.btn_back: {
                closeButton();
                isGone = true;
                btnExpandStimulus.setImageResource(R.drawable.ic_vertical_align_bottom_white_24dp);
                if (mViewPager.getCurrentItem() - 1 >= 0) {
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
                }
                break;
            }
            case R.id.btn_open:
                openButton();
                break;
            default:
                break;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private final String TAG = SectionsPagerAdapter.class.toString();

        private Map<Integer, String> fragmentTags;

        private QuestionPackViewModel questionPack;
        private Fragment currentFragment;

        public SectionsPagerAdapter(FragmentManager fm, QuestionPackViewModel questionPack) {
            super(fm);
            this.questionPack = questionPack;
            fragmentTags = new HashMap<>();
        }

        @Override
        public Fragment getItem(int position) {
            currentFragment = this.crateFragment(position);
            return currentFragment;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Object obj = super.instantiateItem(container, position);
            if (obj instanceof Fragment) {
                this.fragmentTags.put(position, ((Fragment) obj).getTag());
            }
            return obj;
        }

        public Fragment getFragment(int index) {
            String tag = this.fragmentTags.get(index);
            return getSupportFragmentManager().findFragmentByTag(tag);
        }

        private Fragment crateFragment(int position) {
            QuestionModel question = questionPack.getQuestionViewModels().get(position).getQuestion();
            if (question.getType().equals(Question.TYPE_RC)) {
                PlaceholderFragmentRC placeholderFragmentRc = PlaceholderFragmentRC.newInstance(position + 1);
                placeholderFragmentRc.setQuestionPack(questionPack);
                Log.d(TAG, "RC fragment created");
                return placeholderFragmentRc;
            } else {
                PlaceholderFragment fragment = PlaceholderFragment.newInstance(position + 1);
                fragment.setQuestionPack(questionPack);
                return fragment;
            }
        }


        @Override
        public int getCount() {
            return this.questionPack.getNumberOfQuestions();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return "SECTION 4";
        }
    }

    /*TODO*/

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public void setQuestionPack(QuestionPackViewModel mQuestionPack) {
            this.mQuestionPack = mQuestionPack;
        }


        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private int position;
        private QuestionPackViewModel mQuestionPack;
        private WebView contentQuestion;
        private MathView stemQuestion;
        private CardView cardAnswers;
        private ArrayList<AnswerCRQuestionReview> answerChoiseViewItemArrayList;
        private View contentView;
        public static Context context;

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
            Log.d("PlaceholderFragment", "PlaceholderFragment");
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            Log.d("PlaceholderFragment", "newInstance");
            PlaceholderFragment fragment = new PlaceholderFragment();
            fragment.position = sectionNumber - 1;
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Log.d("PlaceholderFragment", "onCreateView");
            View rootView = inflater.inflate(R.layout.fragment_question_review, container, false);
            getRefercence(rootView);
            this.contentView = rootView;
            return rootView;

        }

        @Override
        public void onResume() {
            super.onResume();
            fillData();
            for (int i = 0; i < 5; i++) {
                answerChoiseViewItemArrayList.get(i)
                        .setAnswerModel(mQuestionPack.getQuestionViewModels().get(position).getAnswerChoiceViewModel(i));
                if (mQuestionPack.getQuestionViewModels().get(position).getUserChoise() == i) {
                    answerChoiseViewItemArrayList.get(i).setUserChoise(true);
                }
                if (mQuestionPack.getQuestionViewModels().get(position).getQuestion().getRightAnswerIndex() == i) {
                    answerChoiseViewItemArrayList.get(i).setRightAnswer(true);
                }
                answerChoiseViewItemArrayList.get(i).setQuestionType(mQuestionPack.getQuestionViewModels().get(position).getQuestion().getType());
                answerChoiseViewItemArrayList.get(i).fillData();
            }
        }

        private void fillData() {
            final QuestionViewModel questionViewModel = (mQuestionPack.getQuestionViewModels().get(position));
            contentQuestion.loadDataWithBaseURL("file:///android_asset/mathscribe", Constant.js + questionViewModel.getStimulus() +
                    " $$cos^2θ+sin^2θ=1$$ </body></html>", "text/html; charset=utf-8", "UTF-8", null);
        }


        private void getRefercence(View view) {
            cardAnswers = (CardView) view.findViewById(R.id.card_answer);
            contentQuestion = (WebView) view.findViewById(R.id.question_content);
            if (Build.VERSION.SDK_INT >= 19) {
                contentQuestion.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            } else {
                contentQuestion.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }
            contentQuestion.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            contentQuestion.getSettings().setJavaScriptEnabled(true);

            if (answerChoiseViewItemArrayList == null) {
                answerChoiseViewItemArrayList = new ArrayList<AnswerCRQuestionReview>();
                AnswerCRQuestionReview answerChoiseViewItem0 = (AnswerCRQuestionReview) view.findViewById(R.id.answer_queston_review_1);
                AnswerCRQuestionReview answerChoiseViewItem1 = (AnswerCRQuestionReview) view.findViewById(R.id.answer_queston_review_2);
                AnswerCRQuestionReview answerChoiseViewItem2 = (AnswerCRQuestionReview) view.findViewById(R.id.answer_queston_review_3);
                AnswerCRQuestionReview answerChoiseViewItem3 = (AnswerCRQuestionReview) view.findViewById(R.id.answer_queston_review_4);
                AnswerCRQuestionReview answerChoiseViewItem4 = (AnswerCRQuestionReview) view.findViewById(R.id.answer_queston_review_5);
                answerChoiseViewItemArrayList.add(answerChoiseViewItem0);
                answerChoiseViewItemArrayList.add(answerChoiseViewItem1);
                answerChoiseViewItemArrayList.add(answerChoiseViewItem2);
                answerChoiseViewItemArrayList.add(answerChoiseViewItem3);
                answerChoiseViewItemArrayList.add(answerChoiseViewItem4);
            }
        }

    }

    public static class PlaceholderFragmentRC extends Fragment {

        public void setQuestionPack(QuestionPackViewModel mQuestionPack) {
            this.mQuestionPack = mQuestionPack;
        }

        /**
         * The fragment argument representing the section number for this
         * fragment.
         */

        private int position;
        private QuestionPackViewModel mQuestionPack;
        private ArrayList<AnswerCRQuestionReview> answerChoiseViewItemArrayList;
        public static Context context;
        private TextView txtReadingText;
        private TextView txtQuestion;
        public ScrollView scrollView;

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragmentRC() {
            Log.d("PlaceholderFragment", "PlaceholderFragment");
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragmentRC newInstance(int sectionNumber) {
            Log.d("PlaceholderFragment", "newInstance");
            PlaceholderFragmentRC fragment = new PlaceholderFragmentRC();
            fragment.position = sectionNumber - 1;
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Log.d("PlaceholderFragment", "onCreateView");

            View rootView = inflater.inflate(R.layout.fragment_reading_question_review, container, false);
            getRefercence(rootView);
            return rootView;

        }

        public void showHideScrollView(boolean visible) {
            if (visible) {
                Log.d("PlaceholderFragmentRC", "da gone");
                scrollView.setVisibility(View.VISIBLE);
            } else {
                Log.d("PlaceholderFragmentRC", "da hien");
                scrollView.setVisibility(View.GONE);
            }
        }

        @Override
        public void onResume() {
            super.onResume();
            fillData();
            for (int i = 0; i < 5; i++) {
                answerChoiseViewItemArrayList.get(i)
                        .setAnswerModel(mQuestionPack.getQuestionViewModels().get(position).getAnswerChoiceViewModel(i));
                if (mQuestionPack.getQuestionViewModels().get(position).getUserChoise() == i) {
                    answerChoiseViewItemArrayList.get(i).setUserChoise(true);
                }
                if (mQuestionPack.getQuestionViewModels().get(position).getQuestion().getRightAnswerIndex() == i) {
                    answerChoiseViewItemArrayList.get(i).setRightAnswer(true);
                }
                answerChoiseViewItemArrayList.get(i).setQuestionType(mQuestionPack.getQuestionViewModels().get(position).getQuestion().getType());
                answerChoiseViewItemArrayList.get(i).fillData();
            }
        }

        private void fillData() {
            final QuestionViewModel questionViewModel = (mQuestionPack.getQuestionViewModels().get(position));
            txtReadingText.setText(questionViewModel.getStimulus());
            txtQuestion.setText(questionViewModel.getStem());

        }

        private void getRefercence(View view) {

            txtReadingText = (TextView) view.findViewById(R.id.txtReadingText);
            txtQuestion = (TextView) view.findViewById(R.id.txtQuestion);
            scrollView = (ScrollView) view.findViewById(R.id.scrollViewAnswer1);

            if (answerChoiseViewItemArrayList == null) {
                answerChoiseViewItemArrayList = new ArrayList<AnswerCRQuestionReview>();
                AnswerCRQuestionReview answerChoiseViewItem0 = (AnswerCRQuestionReview) view.findViewById(R.id.answer_queston_review_1);
                AnswerCRQuestionReview answerChoiseViewItem1 = (AnswerCRQuestionReview) view.findViewById(R.id.answer_queston_review_2);
                AnswerCRQuestionReview answerChoiseViewItem2 = (AnswerCRQuestionReview) view.findViewById(R.id.answer_queston_review_3);
                AnswerCRQuestionReview answerChoiseViewItem3 = (AnswerCRQuestionReview) view.findViewById(R.id.answer_queston_review_4);
                AnswerCRQuestionReview answerChoiseViewItem4 = (AnswerCRQuestionReview) view.findViewById(R.id.answer_queston_review_5);
                answerChoiseViewItemArrayList.add(answerChoiseViewItem0);
                answerChoiseViewItemArrayList.add(answerChoiseViewItem1);
                answerChoiseViewItemArrayList.add(answerChoiseViewItem2);
                answerChoiseViewItemArrayList.add(answerChoiseViewItem3);
                answerChoiseViewItemArrayList.add(answerChoiseViewItem4);
            }
        }

    }
}
