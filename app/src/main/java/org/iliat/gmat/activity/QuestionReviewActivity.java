package org.iliat.gmat.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
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
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ToxicBakery.viewpager.transforms.CubeOutTransformer;
import com.zyuternity.arclayout.ArcLayout;

import org.iliat.gmat.R;

import org.iliat.gmat.constant.Constant;
import org.iliat.gmat.database.Question;
import org.iliat.gmat.interf.ScreenManager;
import org.iliat.gmat.item_view.AnswerCRQuestionReview;
import org.iliat.gmat.model.QuestionModel;
import org.iliat.gmat.model.QuestionPackModel;
import org.iliat.gmat.utils.AnimatorUtils;
import org.iliat.gmat.view_model.QuestionPackViewModel;
import org.iliat.gmat.view_model.QuestionViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private Realm realm;
    private int currentItem;
    private int totalItem;
    private ScrollView scrollView;
    private boolean isGone;
    private int position;
    private View view;
    private boolean isOpen = false;
    private View btn_open;
    private View menuLayout;
    private ArcLayout arcLayout;
    private ImageButton btn;

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
        mViewPager.setPageTransformer(true,new CubeOutTransformer());
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

        btn_open = findViewById(R.id.btn_open);
        btn_open.setOnClickListener(this);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        PlaceholderFragment.context = this;
        btnNext.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        menuLayout = findViewById(R.id.menu_layout);
        menuLayout.setOnClickListener(this);
        arcLayout = (ArcLayout) findViewById(R.id.arc_layout);

        btnExpandStimulus = (ImageButton) findViewById(R.id.btnImgButton);
        btnExpandStimulus.setVisibility(View.GONE);
        btnBack = (Button) findViewById(R.id.btn_back);
        scrollView = (ScrollView) mViewPager.findViewById(R.id.scrollViewAnswer1);
        btnExpandStimulus.setOnClickListener(this);

        btnNext.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        isGone = true;
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
                if (isOpen){
                    hideMenu();
                }
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
        if (v.getId() == R.id.btn_open){
            onFabClick(v);
            return;
        }
        else {
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
                    if (isOpen){
                        hideMenu();
                    }
                    Log.d("position", position + "");
                    isGone = true;
                    btnExpandStimulus.setImageResource(R.drawable.ic_vertical_align_bottom_white_24dp);
                    if (mViewPager.getCurrentItem() + 1 < mQuestionPack.getQuestionViewModels().size()) {
                        mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                    }
                    break;
                }
                case R.id.btn_back: {
                    if (isOpen){
                        hideMenu();
                    }
                    isGone = true;
                    btnExpandStimulus.setImageResource(R.drawable.ic_vertical_align_bottom_white_24dp);
                    if (mViewPager.getCurrentItem() - 1 >= 0) {
                        mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
                    }
                    break;
                }
                case R.id.menu_layout: {
                    if (isOpen){
                        hideMenu();
                    }
                    break;
                }
                default:
                    break;
            }
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




    //Bên dưới là animation và chức năng của nút mở tag (làm ơn đừng sửa gì cả :(( )



    private void onFabClick(View v) {
        if (v.isSelected() && isOpen) {
            hideMenu();
        } else {
            showMenu();
        }
        v.setSelected(!v.isSelected());
    }

    @SuppressWarnings("NewApi")
    private void showMenu() {
        menuLayout.setVisibility(View.VISIBLE);
        isOpen = true;
        btn_open.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_forward));
        List<Animator> animList = new ArrayList<>();

        for (int i = 0, len = arcLayout.getChildCount(); i < len; i++) {
            animList.add(createShowItemAnimator(arcLayout.getChildAt(i)));
        }

        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(400);
        animSet.setInterpolator(new OvershootInterpolator());
        animSet.playTogether(animList);
        animSet.start();
    }

    @SuppressWarnings("NewApi")
    private void hideMenu() {
        isOpen = false;
        List<Animator> animList = new ArrayList<>();

        btn_open.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_backward));
        for (int i = arcLayout.getChildCount() - 1; i >= 0; i--) {
            animList.add(createHideItemAnimator(arcLayout.getChildAt(i)));
        }

        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(400);
        animSet.setInterpolator(new AnticipateInterpolator());
        animSet.playTogether(animList);
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                menuLayout.setVisibility(View.INVISIBLE);
            }
        });
        animSet.start();

    }

    private Animator createShowItemAnimator(View item) {

//        float dx = btn_open.getX() - item.getX();
//        float dy = btn_open.getY() - item.getY();

        float dx = -item.getX();
        float dy = item.getY();

        item.setRotation(0f);
        item.setTranslationX(dx);
        item.setTranslationY(dy);

        Animator anim = ObjectAnimator.ofPropertyValuesHolder(
                item,
                AnimatorUtils.rotation(0f, 720f),
                AnimatorUtils.translationX(dx, 0f),
                AnimatorUtils.translationY(dy, 0f)
        );

        return anim;
    }

    private Animator createHideItemAnimator(final View item) {
//        float dx = btn_open.getX() - item.getX();
//        float dy = btn_open.getY() - item.getY();
        float dx = -item.getX();
        float dy = item.getY();

        Animator anim = ObjectAnimator.ofPropertyValuesHolder(
                item,
                AnimatorUtils.rotation(720f, 0f),
                AnimatorUtils.translationX(0f, dx),
                AnimatorUtils.translationY(0f, dy)
        );

        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                item.setTranslationX(0f);
                item.setTranslationY(0f);
            }
        });

        return anim;
    }

}
