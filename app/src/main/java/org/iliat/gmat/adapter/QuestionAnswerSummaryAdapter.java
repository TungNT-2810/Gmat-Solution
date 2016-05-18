package org.iliat.gmat.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.iliat.gmat.R;
import org.iliat.gmat.view_model.QuestionViewModel;

import java.lang.reflect.AccessibleObject;
import java.util.List;

/**
 * Created by qhuydtvt on 4/6/2016.
 */
public class QuestionAnswerSummaryAdapter extends ArrayAdapter<QuestionViewModel> {
    private LayoutInflater mLayoutInflater;
    private Context context;
    public QuestionAnswerSummaryAdapter(Context context,
                                        int resource,
                                        List<QuestionViewModel> objects) {
        super(context, resource, objects);
        this.context = context;
        mLayoutInflater = ((Activity)context).getLayoutInflater();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        QuestionViewModel questionViewModel = getItem(position);
        if(convertView == null) {
            convertView = mLayoutInflater.inflate(
                    R.layout.list_item_score_question_answer_summary,
                    parent, false);
        }

        ViewHolder viewHolder = new ViewHolder(convertView, questionViewModel, position);
        viewHolder.update();

        return convertView;
    }

    private class ViewHolder {
        private TextView txvIndex;
        private TextView txvPreview;
        private ImageView imvStatus;
        private QuestionViewModel questionViewModel;
        private int position;

        public ViewHolder(View view,QuestionViewModel questionViewModel, int position) {
            txvIndex = (TextView)view.findViewById(R.id.txv_index);
            txvPreview = (TextView)view.findViewById(R.id.txv_preview);
            imvStatus = (ImageView)view.findViewById(R.id.imv_status);
            this.questionViewModel = questionViewModel;
            this.position = position;
        }

        public void update() {
            txvIndex.setText(String.valueOf(position + 1) + ".");
            txvPreview.setText(questionViewModel.getStimulus());

            switch (questionViewModel.getAnswerStatus()) {
                case QuestionViewModel.ANSWER_NOT_DONE:
                    imvStatus.setImageResource(R.drawable.ic_warning_black_24dp);
                    break;
                case QuestionViewModel.ANSWER_INCORRECT:
                    imvStatus.setImageResource(R.drawable.ic_clear_black_24dp);
                    imvStatus.setColorFilter(ContextCompat.getColor(QuestionAnswerSummaryAdapter.this.context,R.color.color_red_500));
                    break;
                case QuestionViewModel.ANSWER_CORRECT:
                    imvStatus.setImageResource(R.drawable.ic_done_black_24dp);
                    imvStatus.setColorFilter(ContextCompat.getColor(QuestionAnswerSummaryAdapter.this.context,R.color.color_green_500));
                    break;
            }
        }
    }
}
