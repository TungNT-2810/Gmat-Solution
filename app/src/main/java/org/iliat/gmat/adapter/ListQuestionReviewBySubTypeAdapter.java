package org.iliat.gmat.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.iliat.gmat.R;
import org.iliat.gmat.constant.Constant;
import org.iliat.gmat.model.QuestionModel;

import io.realm.RealmResults;

/**
 * Created by MrBom on 6/11/2016.
 */
public class ListQuestionReviewBySubTypeAdapter extends BaseAdapter {
    private RealmResults<QuestionModel> list;
    private LayoutInflater inflater;
    private Context context;

    public ListQuestionReviewBySubTypeAdapter(RealmResults<QuestionModel> list, Context context) {
        this.list = list;
        this.context = context;
        this.inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.list_item_score_question_answer_summary, null);
        QuestionModel questionModel = list.get(position);
        if (view != null && questionModel != null) {
            TextView txtindex = (TextView) view.findViewById(R.id.txv_index);
            TextView txtPreview = (TextView) view.findViewById(R.id.txv_preview);
            ImageView imvStar = (ImageView) view.findViewById(R.id.imv_star);
            ImageView imvTag = (ImageView) view.findViewById(R.id.imv_tag);
            LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.item_list_answer);

            //bind data
            txtindex.setText(String.valueOf(position + 1) + ".");
            String stimulus = questionModel.getStimulus();
            stimulus = stimulus.replace("span style=\"text-decoration: underline;\"", "u").replace("span", "u");
            txtPreview.setText(Html.fromHtml(stimulus));

            if (questionModel.isStar()) {
                imvStar.setColorFilter(context.getResources().getColor(R.color.yellow), PorterDuff.Mode.SRC_ATOP);
            } else {
                imvStar.setColorFilter(context.getResources().getColor(R.color.color_white), PorterDuff.Mode.SRC_ATOP);
            }

            switch (questionModel.getTagId()) {
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

            if (questionModel.isCorrect()) {
                txtindex.setTextColor(view.getResources().getColor(R.color.color_green_500, null));
                txtPreview.setTextColor(view.getResources().getColor(R.color.color_green_500, null));
                linearLayout.setBackgroundColor(view.getResources().getColor(R.color.green_beautiful, null));
            } else {
                txtindex.setTextColor(view.getResources().getColor(R.color.color_red_500, null));
                txtPreview.setTextColor(view.getResources().getColor(R.color.color_red_500, null));
                linearLayout.setBackgroundColor(view.getResources().getColor(R.color.red_beautiful, null));
            }
            return view;
        }
        return null;
    }
}
