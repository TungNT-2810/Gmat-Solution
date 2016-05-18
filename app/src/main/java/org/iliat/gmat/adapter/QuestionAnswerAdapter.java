package org.iliat.gmat.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.iliat.gmat.R;
import org.iliat.gmat.interf.ButtonNextControl;
import org.iliat.gmat.view_model.QuestionViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qhuydtvt on 3/19/2016.
 */
public class QuestionAnswerAdapter extends BaseAdapter {
    private ButtonNextControl buttonNextControl;
    private static final String TAG = QuestionAnswerAdapter.class.toString();

    private int fixedItemsCnt = 2;
    private LayoutInflater mLayoutInflater;

    private QuestionViewModel mQuestionViewModel;
    private Context mContext;
    private Typeface typeFace;



    public QuestionAnswerAdapter(QuestionViewModel questionViewModel, Context context, LayoutInflater layoutInflater, ButtonNextControl buttonNextControl) {
        this.mQuestionViewModel = questionViewModel;
        this.mLayoutInflater = layoutInflater;
        this.mContext = context;
        this.buttonNextControl = buttonNextControl;
    }




    @Override
    public int getCount() {

        if(mQuestionViewModel.stemIsEmpty())
        {
            fixedItemsCnt = 1;
        }
        else {
            fixedItemsCnt = 2;
        }
        Log.d(TAG, "FixedItemsCnt : " + String.valueOf(fixedItemsCnt));
        return fixedItemsCnt + mQuestionViewModel.getNumberOfAnswerChoices();
    }

    @Override
    public Object getItem(int position) {
        if (position < fixedItemsCnt) {
            if (position == 0)
                return mQuestionViewModel.getQuestion().getStimulus();
            return "<b>" + mQuestionViewModel.getQuestion().getStem() + "</b>";
        }

        int idx = position - fixedItemsCnt;
        Log.d(TAG, String.valueOf(idx));
        return mQuestionViewModel.getAnswerChoiceViewModel(idx).getChoice();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private int getAnswerLabelImvId (int answerChoice) {
        switch (answerChoice) {
            case 0: return R.drawable.a;
            case 1: return R.drawable.b;
            case 2: return R.drawable.c;
            case 3: return R.drawable.d;
            case 4: return R.drawable.e;
        }
        return -1;
    }

    private List<TextView> answerTextViewList = new ArrayList<>();
    private List<ImageView> answerIconViewList = new ArrayList<>();

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            int layoutId;
            if (position < fixedItemsCnt) {
                layoutId = R.layout.list_item_question_prompt;
            } else {
                layoutId = R.layout.list_item_question_answer_choice;
            }

            convertView = mLayoutInflater.inflate(
                    layoutId, parent, false);
        }

        String content = (String)getItem(position);
        if (position < fixedItemsCnt) {
            WebView wvContent = (WebView) convertView.findViewById(R.id.wv_content);
            wvContent.loadData(content, "text/html", "utf-8");
        } else {
            int  answerIdx = position - fixedItemsCnt;

            TextView textView = (TextView) convertView.findViewById(R.id.wv_content);
            typeFace = textView.getTypeface();
            textView.setText(content);
            answerTextViewList.add(textView);


            ImageView imvLabel = (ImageView) convertView.findViewById(R.id.iv_answer_label);
            answerIconViewList.add(imvLabel);
            int imgId = getAnswerLabelImvId(answerIdx);
            if (imgId != -1) {
                imvLabel.setImageResource(imgId);
            }
            convertView.setTag(answerIdx);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer answerIdx = (Integer) v.getTag();
                    mQuestionViewModel.selectAnswerChoice(answerIdx);
                    TextView textView = (TextView) v.findViewById(R.id.wv_content);
                    ImageView imgIcon = (ImageView) v.findViewById(R.id.iv_answer_label);
                    for(TextView txv : answerTextViewList){
                        Log.d("FUCK","FUCK");
                        txv.setTextColor(ContextCompat.getColor(mContext, R.color.color_normal_answer));
                        txv.setTypeface(typeFace);
                    }
                    buttonNextControl.setButtonNextState(1);
                    for(ImageView img : answerIconViewList){
                        Log.d("FUCK","FUCK");
                        img.setColorFilter(ContextCompat.getColor(mContext, R.color.color_normal_answer));
                    }
                    textView.setTextColor(ContextCompat.getColor(mContext, R.color.color_selected_answer));
                    textView.setTypeface(Typeface.DEFAULT_BOLD);
                    imgIcon.setColorFilter(ContextCompat.getColor(mContext, R.color.color_selected_answer));
                }
            });
        }

        return convertView;
    }
}
