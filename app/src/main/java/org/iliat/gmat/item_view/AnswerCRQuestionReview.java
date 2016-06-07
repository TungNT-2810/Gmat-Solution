package org.iliat.gmat.item_view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.iliat.gmat.R;
import org.iliat.gmat.constant.Constant;
import org.iliat.gmat.model.AnswerModel;
import org.iliat.gmat.model.QuestionType;
import org.iliat.gmat.view_model.AnswerChoiceViewModel;
import org.iliat.gmat.view_model.QuestionViewModel;

import io.github.kexanie.library.MathView;

/**
 * Created by hungtran on 4/25/16.
 * Modified by Linh DQ
 */
public class AnswerCRQuestionReview extends LinearLayout implements View.OnClickListener, View.OnTouchListener {
    private WebView txtContentAnswer;
    private WebView txtExplanation;
    private TextView txtContenAnswerText;
    private TextView txtExplanationText;
    private ImageView imgChoise;
    private View line;
    private Context mContext;
    private LinearLayout layoutItem;

    private boolean isClick = false;
    private AnswerChoiceViewModel answerModel;
    private boolean isUserChoise;
    private boolean isRightAnswer;
    private String strAnswer;
    private String explanation;
    private int index;
    private String questionType;
    private int[] IMAGE_RESOURCE = {R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d, R.drawable.e};

    public AnswerCRQuestionReview(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_question_in_question_review, this);
        view.setOnClickListener(this);
        getRefercence(view);
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    private void getRefercence(View view) {
        if (this.imgChoise == null) {
            this.imgChoise = (ImageView) view.findViewById(R.id.img_icon_answer);
            this.txtContentAnswer = (WebView) view.findViewById(R.id.txt_content_answer);
            this.txtExplanation = (WebView) view.findViewById(R.id.txt_explanation);
            this.txtContentAnswer.setOnTouchListener(this);
            this.txtExplanation.setOnTouchListener(this);

            this.txtContentAnswer.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            this.txtExplanation.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            this.txtExplanation.getSettings().setJavaScriptEnabled(true);
            this.txtContentAnswer.getSettings().setJavaScriptEnabled(true);
            this.txtExplanation.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            this.txtContentAnswer.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            this.txtExplanation.setBackgroundColor(0x00000000);
            this.txtContentAnswer.setBackgroundColor(0x00000000);

            this.txtContenAnswerText = (TextView) view.findViewById(R.id.txt_content_answer_text);
            this.txtExplanationText = (TextView) view.findViewById(R.id.txt_explanation_text);
            this.line = view.findViewById(R.id.line_between_question_explanation);
            this.txtExplanation.setVisibility(View.GONE);
            this.txtContenAnswerText.setVisibility(GONE);
            this.txtExplanationText.setVisibility(GONE);
            this.line.setVisibility(View.GONE);
            this.layoutItem = (LinearLayout) view.findViewById(R.id.layout_item);
        }
    }

    public void fillData() {
        imgChoise.setImageResource(IMAGE_RESOURCE[this.index]);
        if (questionType != null && questionType.equals(Constant.TYPE_Q)) {
            txtContenAnswerText.setVisibility(GONE);
            txtContentAnswer.setVisibility(VISIBLE);
            txtExplanationText.setVisibility(GONE);
            txtExplanation.setVisibility(GONE);
            txtContentAnswer.loadDataWithBaseURL("file:///android_asset/mathscribe",
                    Constant.JS + this.strAnswer +
                            " $$cos^2θ+sin^2θ=1$$ </body></html>",
                    Constant.MIME_TYPE, Constant.HTML_ENCODE, null);
        } else {
            txtContenAnswerText.setVisibility(VISIBLE);
            txtContentAnswer.setVisibility(GONE);
            txtExplanationText.setVisibility(GONE);
            txtExplanation.setVisibility(GONE);
            txtContenAnswerText.setText(this.strAnswer);
        }

        if (isUserChoise) {
            imgChoise.setColorFilter(getResources().getColor(R.color.color_red_500));
            layoutItem.setBackgroundColor(getResources().getColor(R.color.red_beautiful));
            if (questionType != null && questionType.equals(Constant.TYPE_Q)) {
                txtContenAnswerText.setVisibility(GONE);
                txtContentAnswer.setVisibility(VISIBLE);
                txtExplanationText.setVisibility(GONE);
                txtExplanation.setVisibility(GONE);
            } else {
                txtContenAnswerText.setVisibility(VISIBLE);
                txtContentAnswer.setVisibility(GONE);
                txtExplanationText.setVisibility(GONE);
                txtExplanation.setVisibility(GONE);
            }
        }
        if (isRightAnswer) {
            imgChoise.setColorFilter(getResources().getColor(R.color.color_green_500));
            layoutItem.setBackgroundColor(getResources().getColor(R.color.green_beautiful));
            if (questionType != null && questionType.equals(Constant.TYPE_Q)) {
                txtContenAnswerText.setVisibility(GONE);
                txtContentAnswer.setVisibility(VISIBLE);
                txtExplanationText.setVisibility(GONE);
                txtExplanation.setVisibility(GONE);
            } else {
                txtContenAnswerText.setVisibility(VISIBLE);
                txtContentAnswer.setVisibility(GONE);
                txtExplanationText.setVisibility(GONE);
                txtExplanation.setVisibility(GONE);
            }
        }
    }


    @Override
    public void onClick(View v) {
        getActionClick();
    }

    private void getActionClick() {
        if (!isClick) {
            isClick = true;
            line.setVisibility(View.VISIBLE);
            if (questionType != null && questionType.equals(Constant.TYPE_Q)) {
                txtExplanationText.setVisibility(GONE);
                txtExplanation.setVisibility(VISIBLE);
                txtExplanation.loadDataWithBaseURL("file:///android_asset/mathscribe",
                        Constant.JS + this.explanation +
                                " $$cos^2θ+sin^2θ=1$$ </body></html>",
                        Constant.MIME_TYPE, Constant.HTML_ENCODE, null);
            } else {
                txtExplanationText.setVisibility(VISIBLE);
                txtExplanation.setVisibility(GONE);
                txtExplanationText.setText(this.explanation);
            }
        } else {
            isClick = false;
            line.setVisibility(View.GONE);
            txtExplanation.setVisibility(View.GONE);
            txtExplanationText.setVisibility(GONE);
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("superState", super.onSaveInstanceState());
        bundle.putString("answer", this.strAnswer);
        bundle.putString("explanation", this.explanation);
        bundle.putInt("index", this.index);
        bundle.putBoolean("isUserChoise", isUserChoise);
        bundle.putBoolean("isRightAnswer", isRightAnswer);
        bundle.putString("questionType",this.questionType);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {

        if (state instanceof Bundle) // implicit null check
        {
            Bundle bundle = (Bundle) state;
            this.strAnswer = bundle.getString("answer");
            this.explanation = bundle.getString("explanation");
            this.index = bundle.getInt("index");
            //this.answerModel = (AnswerChoiceViewModel) bundle.getSerializable("stuff"); // ... load stuff
            state = bundle.getParcelable("superState");
            isUserChoise = bundle.getBoolean("isUserChoise");
            isRightAnswer = bundle.getBoolean("isRightAnswer");
            this.questionType=bundle.getString("questionType");
            fillData();
        }
        super.onRestoreInstanceState(state);
    }

    public boolean isUserChoise() {
        return isUserChoise;
    }

    public void setUserChoise(boolean userChoise) {
        isUserChoise = userChoise;
    }

    public boolean isRightAnswer() {
        return isRightAnswer;
    }

    public void setRightAnswer(boolean rightAnswer) {
        isRightAnswer = rightAnswer;
    }

    public AnswerChoiceViewModel getAnswerModel() {
        return answerModel;
    }

    public void setAnswerModel(AnswerChoiceViewModel answerModel) {
        this.answerModel = answerModel;
        index = answerModel.getIndex();
        strAnswer = answerModel.getChoice();
        explanation = answerModel.getExplanation();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP: {
                getActionClick();
                break;
            }
        }

        return true;
    }
}
