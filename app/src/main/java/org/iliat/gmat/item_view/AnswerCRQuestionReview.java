package org.iliat.gmat.item_view;

import android.content.Context;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.iliat.gmat.R;
import org.iliat.gmat.model.AnswerModel;
import org.iliat.gmat.view_model.AnswerChoiceViewModel;

/**
 * Created by hungtran on 4/25/16.
 */
public class AnswerCRQuestionReview extends LinearLayout implements View.OnClickListener{
    private boolean isClick = false;
    private AnswerChoiceViewModel answerModel;
    private TextView txtContentAnswer;
    private TextView txtExplanation;
    private ImageView imgChoise;
    private View line;
    private boolean isUserChoise;
    private boolean isRightAnswer;
    private String strAnswer;
    private String explanation;
    private int index;
    private int [] IMAGE_RESOURCE = {R.drawable.a,R.drawable.b,R.drawable.c,R.drawable.d,R.drawable.e};
    public AnswerCRQuestionReview(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.item_question_in_question_review,this);
        view.setOnClickListener(this);
        getRefercence(view);
    }

    private void getRefercence(View view){
        if(this.imgChoise == null){
            this.imgChoise = (ImageView)view.findViewById(R.id.img_icon_answer);
            this.txtContentAnswer = (TextView)view.findViewById(R.id.txt_content_answer);
            this.txtExplanation = (TextView)view.findViewById(R.id.txt_explanation);
            this.line = view.findViewById(R.id.line_between_question_explanation);
            this.txtExplanation.setVisibility(View.GONE);
            this.line.setVisibility(View.GONE);
        }
        Log.d("AnswerChoiseViewItem","getRefercence");
    }

    public void fillData(){
//        Log.i("AnswerChoiseViewItem",answerModel.getChoice());
        imgChoise.setImageResource(IMAGE_RESOURCE[this.index]);
        txtContentAnswer.setText(this.strAnswer);
        txtExplanation.setText(this.explanation);
        //Log.d("TAG",answerModel.getChoice());
        if (isUserChoise) {
            imgChoise.setColorFilter(getResources().getColor(R.color.color_red_500));
            txtContentAnswer.setTextColor(getResources().getColor(R.color.color_red_500));
        }
        if (isRightAnswer) {
            imgChoise.setColorFilter(getResources().getColor(R.color.color_green_500));
            txtContentAnswer.setTextColor(getResources().getColor(R.color.color_green_500));
        }
        Log.d("AnswerChoiseViewItem","fillData");
    }

    public TextView getTxtContentAnswer() {
        return txtContentAnswer;
    }

    public void setTxtContentAnswer(TextView txtContentAnswer) {
        this.txtContentAnswer = txtContentAnswer;
    }

    @Override
    public void onClick(View v) {
        if (!isClick){
            isClick = true;
            txtContentAnswer.setTypeface(Typeface.DEFAULT_BOLD);
            txtExplanation.setTypeface(Typeface.DEFAULT_BOLD);
            line.setVisibility(View.VISIBLE);
            txtExplanation.setVisibility(View.VISIBLE);
        } else {
            isClick = false;
            txtContentAnswer.setTypeface(Typeface.DEFAULT);
            txtExplanation.setTypeface(Typeface.DEFAULT);
            line.setVisibility(View.GONE);
            txtExplanation.setVisibility(View.GONE);
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
