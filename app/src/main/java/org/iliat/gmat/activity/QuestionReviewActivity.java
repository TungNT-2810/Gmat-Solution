package org.iliat.gmat.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ToxicBakery.viewpager.transforms.CubeOutTransformer;
import com.zyuternity.arclayout.ArcLayout;

import org.iliat.gmat.R;
import org.iliat.gmat.adapter.SectionsPagerAdapter;
import org.iliat.gmat.constant.Constant;
import org.iliat.gmat.db_connect.DBContext;
import org.iliat.gmat.fragment.PlaceholderFragment;
import org.iliat.gmat.fragment.PlaceholderFragmentRC;
import org.iliat.gmat.interf.ScreenManager;
import org.iliat.gmat.model.QuestionModel;
import org.iliat.gmat.utils.AnimatorUtils;
import org.iliat.gmat.view_model.QuestionViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;


/**
 * Khi sử dụng nhớ set QuestionPack cho nó
 */
/*TODO*/
public class QuestionReviewActivity extends AppCompatActivity implements ScreenManager, View.OnClickListener {

    //question Pack của cái activity này
    private RealmList<QuestionModel> listQuestion;
    private android.app.FragmentManager mFragmentManager;
    private TextView isCorrect;
    private LinearLayout topController;
    private TextView txtProcess;
    private Button btnNext;
    private Button btnBack;
    private ImageButton btnExpandStimulus;
    private ImageButton btnShare;
    private ImageButton btnStar;
    private ImageButton btnGreen;
    private ImageButton btnYellow;
    private ImageButton btnRed;
    private View btn_open;
    private View menuLayout;
    private ArcLayout arcLayout;
    private ImageView imageTag;
    private ImageView imageStar;
    private int totalItem;
    private boolean isGone;
    private int position;
    private boolean isOpen = false;
    private QuestionModel questionModel;


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
        overridePendingTransition(R.anim.trans_in, R.anim.trans_out);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_question_review, menu);
        return true;
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


    @Override
    protected void onResume() {
        super.onResume();
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getBoolean("PackReviewActivity")) {
                getSupportActionBar().setTitle("Package Review");
                String questionPackID = bundle.getString(PackReviewActivity.TAG_QUESTION_PACK_VIEW_MODEL);
                listQuestion = DBContext.getAllQuestionModelByPackId(questionPackID);
                position = bundle.getInt(PackReviewActivity.SCOREACTIIVTY_POSITION);
            } else {
                String typeCode = bundle.getString("typeCode");
                String subTypeCode = bundle.getString("subTypeCode");
                String subTypeDetail = bundle.getString("subTypeDetail");
                getSupportActionBar().setTitle(subTypeDetail+" Review");
                listQuestion = new RealmList<>();
                listQuestion.addAll(DBContext.getAllQuestionAnsweredByTypeAndSubType(typeCode, subTypeCode));
                position = bundle.getInt("position");
            }
            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), listQuestion);
            mViewPager.setAdapter(mSectionsPagerAdapter);
            mViewPager.setPageTransformer(true, new CubeOutTransformer());
            if (position != -1) {
                mViewPager.setCurrentItem(position);
                updateTopView(position);
                setImageButtonState(position);
            }
        }
        isGone = true;
    }

    private void inits() {
        isCorrect = (TextView) findViewById(R.id.txt_correct);
        isCorrect.setTypeface(Typeface.DEFAULT_BOLD);
        mViewPager = (ViewPager) findViewById(R.id.container);
        topController = (LinearLayout) findViewById(R.id.top_controller);
        txtProcess = (TextView) findViewById(R.id.txt_process);
        btnNext = (Button) findViewById(R.id.btn_next);
        btnBack = (Button) findViewById(R.id.btn_back);
        btn_open = findViewById(R.id.btn_open);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        menuLayout = findViewById(R.id.menu_layout);
        arcLayout = (ArcLayout) findViewById(R.id.arc_layout);
        btnExpandStimulus = (ImageButton) findViewById(R.id.btnExpand);
        btnBack = (Button) findViewById(R.id.btn_back);
        mFragmentManager = getFragmentManager();
        btnExpandStimulus.setVisibility(View.GONE);
        btnShare = (ImageButton) findViewById(R.id.btn_share);
        btnStar = (ImageButton) findViewById(R.id.btn_tag_star);
        btnGreen = (ImageButton) findViewById(R.id.btn_tag_green);
        btnYellow = (ImageButton) findViewById(R.id.btn_tag_yellow);
        btnRed = (ImageButton) findViewById(R.id.btn_tag_red);
        imageTag = (ImageView) findViewById(R.id.image_tag);
        imageStar = (ImageView) findViewById(R.id.image_star);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        isGone = true;
        addListener();
    }

    private void addListener() {
        btnExpandStimulus.setOnClickListener(this);
        menuLayout.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btn_open.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        btnStar.setOnClickListener(this);
        btnRed.setOnClickListener(this);
        btnYellow.setOnClickListener(this);
        btnGreen.setOnClickListener(this);
        PlaceholderFragment.context = this;
        for (int i = 0; i < arcLayout.getChildCount(); i++) {
            arcLayout.getChildAt(i).setOnClickListener(this);
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
        QuestionViewModel questionViewModel = new QuestionViewModel(listQuestion.get(position));
        String str = String.format("%d/%d\nTime: %d:%d", position + 1, listQuestion.size(),
                questionViewModel.getTimeToFinish() / 60,
                questionViewModel.getTimeToFinish() % 60);
        txtProcess.setText(str);
        if (questionViewModel.getUserChoise()
                == questionViewModel.getQuestion().getRightAnswerIndex()) {
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
        switch (questionViewModel.getTag()) {
            case Constant.TAG_GREY:
                imageTag.setImageResource(R.mipmap.grey);
                break;
            case Constant.TAG_GREEN:
                imageTag.setImageResource(R.mipmap.green);
                break;
            case Constant.TAG_YELLOW:
                imageTag.setImageResource(R.mipmap.yellow);
                break;
            case Constant.TAG_RED:
                imageTag.setImageResource(R.mipmap.red);
                break;
        }
        if (questionViewModel.isStar()) {
            imageStar.setColorFilter(getResources().getColor(R.color.yellow), PorterDuff.Mode.SRC_ATOP);
        } else {
            imageStar.setColorFilter(getResources().getColor(R.color.color_white), PorterDuff.Mode.SRC_ATOP);
        }
    }

    private void setImageButtonState(int position) {
        if (listQuestion.get(position).getType().equals("RC")) {
            btnExpandStimulus.setVisibility(View.VISIBLE);
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
                totalItem = listQuestion.size();
                if (isOpen) {
                    hideMenu();
                }
                btnBack.setEnabled(position > 0 && totalItem > 0);
                btnNext.setEnabled(totalItem > 0 && position < totalItem - 1);
            }

            @Override
            public void onPageSelected(int position) {
                updateTopView(position);
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
            case R.id.btn_open: {
                if (isOpen) {
                    hideMenu();
                } else {
                    showMenu();
                }
                break;
            }

            case R.id.btnExpand: {
                if (isOpen) {
                    hideMenu();
                }
                Fragment fragment = mSectionsPagerAdapter.getFragment(this.mViewPager.getCurrentItem());
                if (fragment instanceof PlaceholderFragmentRC) {
                    isGone = !isGone;
                    ((PlaceholderFragmentRC) fragment).showHideScrollView(isGone);
                    if (!isGone) {
                        btnExpandStimulus.setImageResource(R.drawable.ic_vertical_align_top_white_24dp);
                    } else {
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
                if (isOpen) {
                    hideMenu();
                }
                isGone = true;
                btnExpandStimulus.setImageResource(R.drawable.ic_vertical_align_bottom_white_24dp);
                if (mViewPager.getCurrentItem() + 1 < listQuestion.size()) {
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                }
                break;
            }
            case R.id.btn_back: {
                if (isOpen) {
                    hideMenu();
                }
                isGone = true;
                btnExpandStimulus.setImageResource(R.drawable.ic_vertical_align_bottom_white_24dp);
                if (mViewPager.getCurrentItem() > 0) {
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
                }
                break;
            }
            case R.id.menu_layout: {
                if (isOpen) {
                    hideMenu();
                }
                break;
            }
            case R.id.btn_tag_green:
            case R.id.layout_tag_green:
                updateTagToDatabase(Constant.TAG_GREEN);
                imageTag.setImageResource(R.mipmap.green);
                hideMenu();
                break;
            case R.id.btn_tag_yellow:
            case R.id.layout_tag_yellow:
                updateTagToDatabase(Constant.TAG_YELLOW);
                imageTag.setImageResource(R.mipmap.yellow);
                hideMenu();
                break;
            case R.id.btn_tag_red:
            case R.id.layout_tag_red:
                updateTagToDatabase(Constant.TAG_RED);
                imageTag.setImageResource(R.mipmap.red);
                hideMenu();
                break;
            case R.id.btn_tag_star:
            case R.id.layout_tag_star:
                updateTagToDatabase(Constant.TAG_STAR);
                if (questionModel.isStar()) {
                    imageStar.setColorFilter(getResources().getColor(R.color.yellow), PorterDuff.Mode.SRC_ATOP);
                } else {
                    imageStar.setColorFilter(getResources().getColor(R.color.color_white), PorterDuff.Mode.SRC_ATOP);
                }
                hideMenu();
                break;
            default:
                break;
        }
    }

    private void updateTagToDatabase(int tag) {
        int currentPossition = mViewPager.getCurrentItem();
        Fragment fragment = mSectionsPagerAdapter.getFragment(currentPossition);
        if (fragment instanceof PlaceholderFragment) {
            questionModel = ((PlaceholderFragment) fragment).getQuestion();
        } else {
            questionModel = ((PlaceholderFragmentRC) fragment).getQuestion();
        }
        if (tag == Constant.TAG_STAR) {
            DBContext.updateStarForEachQuestion(questionModel);
        } else {
            DBContext.updateTagForEachQuestion(questionModel, tag);
        }
    }

    //Bên dưới là animation và chức năng của nút mở tag (làm ơn đừng sửa gì cả :(( )


    @SuppressWarnings("NewApi")
    private void showMenu() {
        menuLayout.setVisibility(View.VISIBLE);
        isOpen = true;
        btn_open.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_forward));
        btnShare.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open));
        btnStar.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open));
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

        btn_open.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_backward));
        btnShare.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close));
        btnStar.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close));
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_back_in, R.anim.trans_back_out);
    }
}
