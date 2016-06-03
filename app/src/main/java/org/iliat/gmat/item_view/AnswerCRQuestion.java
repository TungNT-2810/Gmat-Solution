package org.iliat.gmat.item_view;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.iliat.gmat.R;
import org.iliat.gmat.constant.Constant;
import org.iliat.gmat.interf.ChangeStateOfAnswerItemsInterface;
import org.iliat.gmat.interf.ButtonControl;
import org.iliat.gmat.view_model.AnswerChoiceViewModel;

/**
 * Created by hungtran on 5/2/16.
 * Modified by Linh DQ
 */
public class AnswerCRQuestion extends LinearLayout implements View.OnClickListener{
    private AnswerChoiceViewModel answerModel;
    private ChangeStateOfAnswerItemsInterface changeStateOfAnswerItemsInterface;
    private WebView txtContentAnswer;
    private WebView txtExplanation;
    private Context mContext;
    private TextView txtContenAnswerText;
    private TextView txtExplanationText;
    private ButtonControl buttonControl;
    private ImageView imgChoise;
    private boolean isUserChoise;
    private String strAnswer;
    private int index;
    private int [] IMAGE_RESOURCE = {R.drawable.a,R.drawable.b,R.drawable.c,R.drawable.d,R.drawable.e};
    private String questionType;
    private LinearLayout layoutItem;


    public AnswerCRQuestion(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.item_question_in_question_review,this);
        view.setOnClickListener(this);
        getRefercence(view);
    }

    public void setQuestionType(String questionType){
        this.questionType=questionType;
    }
    public void setChangeStateOfAnswerItemsInterface(ChangeStateOfAnswerItemsInterface changeStateOfAnswerItemsInterface) {
        this.changeStateOfAnswerItemsInterface = changeStateOfAnswerItemsInterface;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public void setButtonControl(ButtonControl buttonControl) {
        this.buttonControl = buttonControl;
    }

    private void getRefercence(View view){
        if(this.imgChoise == null){
            this.imgChoise = (ImageView)view.findViewById(R.id.img_icon_answer);
            this.txtContentAnswer = (WebView) view.findViewById(R.id.txt_content_answer);
            this.txtExplanation = (WebView) view.findViewById(R.id.txt_explanation);
            this.txtContenAnswerText=(TextView)view.findViewById(R.id.txt_content_answer_text);
            this.txtExplanationText=(TextView)view.findViewById(R.id.txt_explanation_text);
            this.layoutItem=(LinearLayout)view.findViewById(R.id.layout_item);
        }
    }

    public void fillData(){
        isUserChoise = false;
        imgChoise.setImageResource(IMAGE_RESOURCE[this.index]);
        if(questionType!=null && questionType.equals(Constant.TYPE_Q)) {
            txtContentAnswer.setVisibility(VISIBLE);
            txtContenAnswerText.setVisibility(GONE);
            txtExplanation.setVisibility(GONE);
            txtExplanationText.setVisibility(GONE);
            txtContentAnswer.loadData(Constant.js+this.strAnswer,Constant.MIME_TYPE,Constant.HTML_ENCODE);
        }else{
            txtContentAnswer.setVisibility(GONE);
            txtExplanation.setVisibility(GONE);
            txtExplanationText.setVisibility(GONE);
            txtContenAnswerText.setVisibility(VISIBLE);
            txtContenAnswerText.setText(this.strAnswer);
        }
        if (isUserChoise) {
            layoutItem.setBackgroundColor(getResources().getColor(R.color.blue_beautiful));
        }
    }

    public void setUserChoise(boolean userChoise) {
        isUserChoise = userChoise;
        if (isUserChoise) {
            imgChoise.setColorFilter(ContextCompat.getColor(mContext, R.color.color_selected_answer));
            layoutItem.setBackgroundColor(getResources().getColor(R.color.blue_beautiful));
        } else {
            imgChoise.setColorFilter(ContextCompat.getColor(mContext, R.color.color_normal_answer));
            layoutItem.setBackgroundColor(getResources().getColor(R.color.color_white));
        }
    }

    @Override
    public void onClick(View v) {
        isUserChoise = true;
        changeStateOfAnswerItemsInterface.changeState(index);
        buttonControl.setButtonNextState(1);
    }

    public void setAnswerModel(AnswerChoiceViewModel answerModel) {
        this.answerModel = answerModel;
        index = answerModel.getIndex();
        strAnswer = answerModel.getChoice();
    }
}
