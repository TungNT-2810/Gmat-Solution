package org.iliat.gmat.adapter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.iliat.gmat.R;
import org.iliat.gmat.constant.Constant;
import org.iliat.gmat.view_model.QuestionViewModel;

import java.util.List;

/**
 * Created by qhuydtvt on 4/6/2016.
 * Modified by Linh DQ on 6/6/2016
 */
public class QuestionAnswerSummaryAdapter extends ArrayAdapter<QuestionViewModel> {
    private LayoutInflater mLayoutInflater;

    public QuestionAnswerSummaryAdapter(Context context,
                                        int resource,
                                        List<QuestionViewModel> objects) {
        super(context, resource, objects);
        mLayoutInflater = ((Activity) context).getLayoutInflater();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        QuestionViewModel questionViewModel = getItem(position);
        if (convertView == null) {
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
        private ImageView imvStar;
        private ImageView imvTag;
        private QuestionViewModel questionViewModel;
        private LinearLayout linearLayout;
        private int position;
        private Context context;

        public ViewHolder(View view, QuestionViewModel questionViewModel, int position) {
            txvIndex = (TextView) view.findViewById(R.id.txv_index);
            txvPreview = (TextView) view.findViewById(R.id.txv_preview);
            imvStar = (ImageView) view.findViewById(R.id.imv_star);
            imvTag = (ImageView) view.findViewById(R.id.imv_tag);
            linearLayout = (LinearLayout) view.findViewById(R.id.item_list_answer);
            this.questionViewModel = questionViewModel;
            this.position = position;
            context = view.getContext();
        }

        @TargetApi(Build.VERSION_CODES.M)
        public void update() {
            //bind data
            txvIndex.setText(String.valueOf(position + 1) + ".");

            String stimulus = questionViewModel.getStimulus();
            stimulus = stimulus.replace("span style=\"text-decoration: underline;\"", "u").replace("span", "u");
            txvPreview.setText(Html.fromHtml(stimulus));

            //set color for star
            if (questionViewModel.isStar()) {
                imvStar.setColorFilter(context.getResources().getColor(R.color.yellow, null), PorterDuff.Mode.SRC_ATOP);
            } else {
                imvStar.setColorFilter(context.getResources().getColor(R.color.color_white, null), PorterDuff.Mode.SRC_ATOP);
            }

            //set tag
            switch (questionViewModel.getTag()) {
                case Constant.TAG_GREY:
                    imvTag.setImageResource(R.mipmap.grey);
                    break;
                case Constant.TAG_GREEN:
                    imvTag.setImageResource(R.mipmap.green);
                    break;
                case Constant.TAG_YELLOW:
                    imvTag.setImageResource(R.mipmap.yellow);
                    break;
                case Constant.TAG_RED:
                    imvTag.setImageResource(R.mipmap.red);
                    break;
            }

            //set color
            if (questionViewModel.isCorrect()) {
                txvIndex.setTextColor(context.getResources().getColor(R.color.color_green_500, null));
                txvPreview.setTextColor(context.getResources().getColor(R.color.color_green_500, null));
            } else {
                txvIndex.setTextColor(context.getResources().getColor(R.color.color_red_500, null));
                txvPreview.setTextColor(context.getResources().getColor(R.color.color_red_500, null));
            }
        }
    }
}
