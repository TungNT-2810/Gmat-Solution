package org.iliat.gmat.activity;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.media.Image;
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
import org.iliat.gmat.fragment.answer_question.RCQuestionFragment;
import org.iliat.gmat.interf.ScreenManager;
import org.iliat.gmat.item_view.AnswerCRQuestionReview;
import org.iliat.gmat.model.QuestionPackModel;
import org.iliat.gmat.view_model.QuestionPackViewModel;
import org.iliat.gmat.view_model.QuestionViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import io.github.kexanie.library.MathView;
import io.realm.Realm;


/**
 * Khi sử dụng nhớ set QuestionPack cho nó
 */
/*TODO*/
public class QuestionReviewActivity extends AppCompatActivity implements ScreenManager,View.OnClickListener {

    //question Pack của cái activity này
    private QuestionPackViewModel mQuestionPack;
    private android.app.FragmentManager mFragmentManager;
    TextView isCorrect;
    private RelativeLayout topController;
    TextView txtProcess;
    private Button btnNext;
    private Button btnBack;
    private ImageButton btn_open;
    private Realm realm;
    private int currentItem;
    private int totalItem;
    private ImageButton btn_tag_grey;
    private ImageButton btn_tag_green;
    private ImageButton btn_tag_yellow;
    private ImageButton btn_tag_red;
    private ImageButton btn_tag_star;
    private ImageButton btn_share;
    private boolean isOpen = false;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;

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
        }

    }

    private void inits(){
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
    }

    private void openButton(){
        if (!isOpen){
            btn_open.startAnimation(rotate_forward);
            btn_tag_grey.startAnimation(fab_open);
            btn_tag_grey.setClickable(true);
            btn_tag_green.startAnimation(fab_open);
            btn_tag_green.setClickable(true);
            btn_tag_yellow.startAnimation(fab_open);
            btn_tag_yellow.setClickable(true);
            btn_tag_red.startAnimation(fab_open);
            btn_tag_red.setClickable(true);
            btn_tag_star.startAnimation(fab_open);
            btn_tag_star.setClickable(true);
            btn_share.startAnimation(fab_open);
            btn_share.setClickable(true);
            isOpen = true;
        }
        else {
            btn_open.startAnimation(rotate_backward);
            btn_tag_grey.startAnimation(fab_close);
            btn_tag_grey.setClickable(false);
            btn_tag_green.startAnimation(fab_close);
            btn_tag_green.setClickable(false);
            btn_tag_yellow.startAnimation(fab_close);
            btn_tag_yellow.setClickable(false);
            btn_tag_red.startAnimation(fab_close);
            btn_tag_red.setClickable(false);
            btn_tag_star.startAnimation(fab_close);
            btn_tag_star.setClickable(false);
            btn_share.startAnimation(fab_close);
            btn_share.setClickable(false);
            isOpen = false;
        }
    }

    private void closeButton(){
        if (isOpen){
            btn_open.startAnimation(rotate_backward);
            btn_tag_grey.startAnimation(fab_close);
            btn_tag_grey.setClickable(false);
            btn_tag_green.startAnimation(fab_close);
            btn_tag_green.setClickable(false);
            btn_tag_yellow.startAnimation(fab_close);
            btn_tag_yellow.setClickable(false);
            btn_tag_red.startAnimation(fab_close);
            btn_tag_red.setClickable(false);
            btn_tag_star.startAnimation(fab_close);
            btn_tag_star.setClickable(false);
            btn_share.startAnimation(fab_close);
            btn_share.setClickable(false);
            isOpen = false;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_share:
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
            case R.id.btn_next:
                closeButton();
                if (mViewPager.getCurrentItem() + 1 < mQuestionPack.getQuestionViewModels().size()) {
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                }
                break;
            case R.id.btn_back:
                closeButton();
                if (mViewPager.getCurrentItem() - 1 >= 0) {
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
                }
                break;
            case R.id.btn_open:
                openButton();
                break;
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
                mQuestionPack.getQuestionViewModels().get(position).getTimeToFinish()/60,
                mQuestionPack.getQuestionViewModels().get(position).getTimeToFinish()%60);
        txtProcess.setText(str);
        if (mQuestionPack.getQuestionViewModels().get(position).getUserChoise()
                == mQuestionPack.getQuestionViewModels().get(position).getQuestion().getRightAnswerIndex()) {
            isCorrect.setText("Correct");
            topController.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.color_correct));
        } else {
            isCorrect.setText("Incorrect");
            topController.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.color_in_correct));
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
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
//        if(mViewPager!=null) {
//            currentItem = mViewPager.getCurrentItem();
//            totalItem=mViewPager.getChildCount();
//            if (currentItem>0 && currentItem<totalItem-1){
//                btnNext.setEnabled(true);
//                btnBack.setEnabled(true);
//            }else if(currentItem==0 && totalItem>0){
//                btnBack.setEnabled(false);
//                btnNext.setEnabled(true);
//            }else if( currentItem==totalItem-1 && totalItem-1!=0){
//                btnNext.setEnabled(false);
//                btnBack.setEnabled(true);
//            }else if(totalItem==0){
//                btnBack.setEnabled(false);
//                btnNext.setEnabled(false);
//                btnShare.setEnabled(false);
//            }
//        }
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

    /*TODO*/

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        public QuestionPackViewModel getmQuestionPack() {
            return mQuestionPack;
        }

        public void setQuestionPack(QuestionPackViewModel mQuestionPack) {
            this.mQuestionPack = mQuestionPack;
        }


        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private boolean isRC;
        private int position;
        private QuestionPackViewModel mQuestionPack;
        private WebView contentQuestion;
        private MathView stemQuestion;
        private CardView cardAnswers;
        private ArrayList<AnswerCRQuestionReview> answerChoiseViewItemArrayList;
        private View contentView;
        public static Context context;
        private TextView txtReadingText;
        private TextView txtQuestion;
        private ImageButton imageButton;
        private ScrollView scrollView;
        private boolean isGone=false;

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

            View rootView=null;
            if(mQuestionPack.getQuestionViewModels().get(position).getQuestion().getType().equals("RC")){
                Log.d("dm","RC day");
                rootView = inflater.inflate(R.layout.fragment_reading_question, container, false);
                isRC=true;
            }else{
                rootView = inflater.inflate(R.layout.fragment_question_review, container, false);
                isRC=false;
            }
            getRefercence(rootView);
            this.contentView = rootView;
            return rootView;

        }

        @Override
        public void onResume() {
            super.onResume();
            fillData();
            if(!isRC) {
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
        }

        private void fillData() {
            final QuestionViewModel questionViewModel = (mQuestionPack.getQuestionViewModels().get(position));
            if(!isRC) {
                contentQuestion.loadDataWithBaseURL("file:///android_asset/mathscribe", Constant.js + questionViewModel.getStimulus() +
                        " $$cos^2θ+sin^2θ=1$$ </body></html>", "text/html; charset=utf-8", "UTF-8", null);
            }else{
                txtReadingText.setText(questionViewModel.getStimulus());
                txtQuestion.setText(questionViewModel.getStem());
            }
        }

        private void getRefercence(View view) {
            if(!isRC) {
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
            }else{
                txtReadingText=(TextView) view.findViewById(R.id.txtReadingText);
                txtQuestion=(TextView)view.findViewById(R.id.txtQuestion);
                imageButton=(ImageButton)view.findViewById(R.id.btnImgButton);
                scrollView = (ScrollView) view.findViewById(R.id.scrollViewAnswer);
                imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isGone) {
                            scrollView.setVisibility(View.GONE);
                            imageButton.setImageResource(R.drawable.ic_vertical_align_top_white_24dp);
                            isGone = true;
                        } else {
                            scrollView.setVisibility(View.VISIBLE);
                            imageButton.setImageResource(R.drawable.ic_vertical_align_bottom_white_24dp);
                            isGone = false;
                        }
                    }
                });
            }

        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    /*TODO*/
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private QuestionPackViewModel questionPack;
        private QuestionViewModel questionCRModel;

        public SectionsPagerAdapter(FragmentManager fm, QuestionPackViewModel questionPack) {
            super(fm);
            this.questionPack = questionPack;
        }

        @Override
        public Fragment getItem(int position) {

            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            PlaceholderFragment placeholderFragment = PlaceholderFragment.newInstance(position + 1);
            placeholderFragment.setQuestionPack(questionPack);
            if(questionPack.getQuestionViewModels().get(position).getQuestion().getType().equals("RC")){
                RCQuestionFragment rcQuestionFragment=new RCQuestionFragment();
                rcQuestionFragment.setQuestion(questionPack.getQuestionViewModels().get(position));
                //return rcQuestionFragment;
            }
            return placeholderFragment;
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

    /**
     * Adapter cho các câu trả lời ở phần Review
     */


}
