package org.iliat.gmat.item_view;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.iliat.gmat.R;
import org.iliat.gmat.fragment.answer_question.ChangeStateOfAnswerItemsInterface;
import org.iliat.gmat.interf.ButtonNextControl;
import org.iliat.gmat.view_model.AnswerChoiceViewModel;

/**
 * Created by hungtran on 5/2/16.
 */
public class AnswerCRQuestion extends LinearLayout implements View.OnClickListener{
    private AnswerChoiceViewModel answerModel;
    private ChangeStateOfAnswerItemsInterface changeStateOfAnswerItemsInterface;
    private TextView txtContentAnswer;
    private TextView txtExplanation;
    private Context mContext;
    private ButtonNextControl buttonNextControl;
    private ImageView imgChoise;
    private boolean isUserChoise;
    private String strAnswer;
    private int index;
    private int [] IMAGE_RESOURCE = {R.drawable.a,R.drawable.b,R.drawable.c,R.drawable.d,R.drawable.e};

    public AnswerCRQuestion(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.item_question_in_question_review,this);
        view.setOnClickListener(this);
        getRefercence(view);
    }

    public void setChangeStateOfAnswerItemsInterface(ChangeStateOfAnswerItemsInterface changeStateOfAnswerItemsInterface) {
        this.changeStateOfAnswerItemsInterface = changeStateOfAnswerItemsInterface;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public void setButtonNextControl(ButtonNextControl buttonNextControl) {
        this.buttonNextControl = buttonNextControl;
    }

    private void getRefercence(View view){
        if(this.imgChoise == null){
            this.imgChoise = (ImageView)view.findViewById(R.id.img_icon_answer);
            this.txtContentAnswer = (TextView)view.findViewById(R.id.txt_content_answer);
            this.txtExplanation = (TextView)view.findViewById(R.id.txt_explanation);
            this.txtExplanation.setVisibility(View.GONE);
        }
    }

    public void fillData(){
        isUserChoise = false;
        imgChoise.setImageResource(IMAGE_RESOURCE[this.index]);
        txtContentAnswer.setText(this.strAnswer);
        if (isUserChoise) {
            imgChoise.setColorFilter(ContextCompat.getColor(mContext, R.color.color_selected_answer));
            txtContentAnswer.setTextColor(ContextCompat.getColor(mContext, R.color.color_selected_answer));
            txtContentAnswer.setTypeface(Typeface.DEFAULT_BOLD);
        }
    }

    public void setUserChoise(boolean userChoise) {
        isUserChoise = userChoise;
        if (isUserChoise) {
            imgChoise.setColorFilter(ContextCompat.getColor(mContext, R.color.color_selected_answer));
            txtContentAnswer.setTextColor(ContextCompat.getColor(mContext, R.color.color_selected_answer));
            txtContentAnswer.setTypeface(Typeface.DEFAULT_BOLD);
        } else {
            imgChoise.setColorFilter(ContextCompat.getColor(mContext, R.color.color_normal_answer));
            txtContentAnswer.setTextColor(ContextCompat.getColor(mContext, R.color.color_normal_answer));
            txtContentAnswer.setTypeface(Typeface.DEFAULT);
        }

    }

    @Override
    public void onClick(View v) {
        isUserChoise = true;
        changeStateOfAnswerItemsInterface.changeState(index);
        buttonNextControl.setButtonNextState(1);
    }

    public void setAnswerModel(AnswerChoiceViewModel answerModel) {
        this.answerModel = answerModel;
        index = answerModel.getIndex();
        strAnswer = answerModel.getChoice();
    }
}
