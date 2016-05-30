package org.iliat.gmat.item_view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.iliat.gmat.R;
import org.iliat.gmat.constant.Constant;
import org.iliat.gmat.model.AnswerModel;
import org.iliat.gmat.model.QuestionType;
import org.iliat.gmat.view_model.AnswerChoiceViewModel;

import io.github.kexanie.library.MathView;

/**
 * Created by hungtran on 4/25/16.
 */
public class AnswerCRQuestionReview extends LinearLayout implements View.OnClickListener{
    private boolean isClick = false;
    private AnswerChoiceViewModel answerModel;
    private WebView txtContentAnswer;
    private WebView txtExplanation;
    private TextView txtContenAnswerText;
    private TextView txtExplanationText;
    private ImageView imgChoise;
    private View line;
    private boolean isUserChoise;
    private boolean isRightAnswer;
    private String strAnswer;
    private String explanation;
    private int index;
    private int red = Color.parseColor("#FFCDD2");
    private int green = Color.parseColor("#C8E6C9");
    private String questionType;
    private Context mContext;
    private LinearLayout layoutItem;
    private int [] IMAGE_RESOURCE = {R.drawable.a,R.drawable.b,R.drawable.c,R.drawable.d,R.drawable.e};
    public AnswerCRQuestionReview(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.item_question_in_question_review,this);
        view.setOnClickListener(this);
        getRefercence(view);
    }

    public void setQuestionType(String questionType){
        this.questionType=questionType;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    private void getRefercence(View view){
        if(this.imgChoise == null){
            this.imgChoise = (ImageView)view.findViewById(R.id.img_icon_answer);
            this.txtContentAnswer = (WebView) view.findViewById(R.id.txt_content_answer);
            this.txtExplanation = (WebView) view.findViewById(R.id.txt_explanation);
            this.txtContenAnswerText=(TextView)view.findViewById(R.id.txt_content_answer_text);
            this.txtExplanationText=(TextView)view.findViewById(R.id.txt_explanation_text);
            this.line = view.findViewById(R.id.line_between_question_explanation);
            this.txtExplanation.setVisibility(View.GONE);
            this.txtContenAnswerText.setVisibility(GONE);
            this.txtExplanationText.setVisibility(GONE);
            this.line.setVisibility(View.GONE);
            this.layoutItem=(LinearLayout)view.findViewById(R.id.layout_item);
        }
        Log.d("AnswerChoiseViewItem","getRefercence");
    }

    public void fillData(){
//        Log.i("AnswerChoiseViewItem",answerModel.getChoice());
        imgChoise.setImageResource(IMAGE_RESOURCE[this.index]);
        if(questionType!=null && questionType.equals("Q")) {
            txtContenAnswerText.setVisibility(GONE);
            txtContentAnswer.setVisibility(VISIBLE);
            txtExplanationText.setVisibility(GONE);
            txtExplanation.setVisibility(GONE);
            txtContentAnswer.loadData(Constant.js + this.strAnswer, "text/html; charset=utf-8", "UTF-8");
        }else{
            txtContenAnswerText.setVisibility(VISIBLE);
            txtContentAnswer.setVisibility(GONE);
            txtExplanationText.setVisibility(GONE);
            txtExplanation.setVisibility(GONE);
            txtContenAnswerText.setText(this.strAnswer);
        }

        if (isUserChoise) {
            imgChoise.setColorFilter(getResources().getColor(R.color.color_red_500));
            layoutItem.setBackgroundColor(red);
            if(questionType!=null && questionType.equals("Q")) {
                txtContenAnswerText.setVisibility(GONE);
                txtContentAnswer.setVisibility(VISIBLE);
                txtExplanationText.setVisibility(GONE);
                txtExplanation.setVisibility(GONE);
            }else{
                txtContenAnswerText.setVisibility(VISIBLE);
                txtContentAnswer.setVisibility(GONE);
                txtExplanationText.setVisibility(GONE);
                txtExplanation.setVisibility(GONE);
            }
        }
        if (isRightAnswer) {
            imgChoise.setColorFilter(getResources().getColor(R.color.color_green_500));
            if(questionType!=null && questionType.equals("Q")) {
                txtContenAnswerText.setVisibility(GONE);
                txtContentAnswer.setVisibility(VISIBLE);
                txtExplanationText.setVisibility(GONE);
                txtExplanation.setVisibility(GONE);
            }else{
                txtContenAnswerText.setVisibility(VISIBLE);
                txtContentAnswer.setVisibility(GONE);
                txtExplanationText.setVisibility(GONE);
                txtExplanation.setVisibility(GONE);
            }
            layoutItem.setBackgroundColor(green);
        }
    }

    public WebView getTxtContentAnswer() {
        return txtContentAnswer;
    }

    public void setTxtContentAnswer(WebView txtContentAnswer) {
        this.txtContentAnswer = txtContentAnswer;
    }

    @Override
    public void onClick(View v) {
        if (!isClick){
            isClick = true;
            line.setVisibility(View.VISIBLE);
            if(questionType!=null && questionType.equals("Q")) {
                txtExplanationText.setVisibility(GONE);
                txtExplanation.setVisibility(VISIBLE);
                txtExplanation.getSettings().setDefaultTextEncodingName("utf-8");
                txtExplanation.loadData(this.explanation, "text/html; charset=utf-8", "UTF-8");
            }else{
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
        bundle.putBoolean("isUserChoise",isUserChoise);
        bundle.putBoolean("isRightAnswer",isRightAnswer);
        //bundle.putSerializable("stuff", this.answerModel); // ... save stuff
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
}
