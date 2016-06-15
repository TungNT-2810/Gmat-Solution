package org.iliat.gmat.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.iliat.gmat.R;
import org.iliat.gmat.model.QuestionModel;

import io.realm.RealmResults;

/**
 * Created by MrBom on 6/14/2016.
 */
public class ListQuestionByTagAdapter extends BaseAdapter {

    private RealmResults<QuestionModel> listQuestion;
    private Context context;
    private LayoutInflater inflater;

    public ListQuestionByTagAdapter(RealmResults<QuestionModel> listQuestion, Context context) {
        this.listQuestion = listQuestion;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return listQuestion.size();
    }

    @Override
    public Object getItem(int position) {
        return listQuestion.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.list_item_score_question_answer_summary, null);
        QuestionModel questionModel = listQuestion.get(position);
        if (view != null && questionModel != null) {
            //init control
            TextView txvIndex = (TextView) view.findViewById(R.id.txv_index);
            TextView txvPreview = (TextView) view.findViewById(R.id.txv_preview);
            ((LinearLayout) view.findViewById(R.id.container_tag)).setVisibility(View.GONE);
            LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.item_list_answer);

            //bind data
            txvIndex.setText(String.valueOf(position + 1) + ".");

            String stimulus = questionModel.getStimulus();
            stimulus = stimulus.replace("span style=\"text-decoration: underline;\"", "u").replace("span", "u");
            txvPreview.setText(Html.fromHtml(stimulus));

            //set color
            if (questionModel.isCorrect()) {
                txvIndex.setTextColor(context.getResources().getColor(R.color.color_green_500));
                txvPreview.setTextColor(context.getResources().getColor(R.color.color_green_500));
                //linearLayout.setBackgroundColor(context.getResources().getColor(R.color.green_beautiful));
            } else {
                txvIndex.setTextColor(context.getResources().getColor(R.color.color_red_500));
                txvPreview.setTextColor(context.getResources().getColor(R.color.color_red_500));
                //linearLayout.setBackgroundColor(context.getResources().getColor(R.color.red_beautiful));
            }
        }
        return view;
    }
}
