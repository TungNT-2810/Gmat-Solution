package org.iliat.gmat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.iliat.gmat.R;
import org.iliat.gmat.database.AnswerChoice;
import org.iliat.gmat.database.Question;
import org.iliat.gmat.model.QuestionModel;
import org.iliat.gmat.view_model.AnswerChoiceViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hungtran on 3/13/16.
 */
public class ListAnswerChoiceAdapter extends BaseAdapter {

    private QuestionModel mQuestion;
    private ArrayList<AnswerChoiceViewModel> answerChoiceViewModelList;
    private LayoutInflater mLayoutInflater;

    public ListAnswerChoiceAdapter(LayoutInflater layoutInflater, QuestionModel question) {
        this.mLayoutInflater = layoutInflater;
        this.mQuestion = question;

        answerChoiceViewModelList = new ArrayList<>();

//        List<AnswerChoice> answer_choices = mQuestion.getAnswerChoiceList();
//        for(int idx = 0; idx < answer_choices.size(); idx++) {
//            answerChoiceViewModelList.add(new AnswerChoiceViewModel(answer_choices.get(idx)));
//        }
    }

    @Override
    public int getCount() {
        return answerChoiceViewModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return answerChoiceViewModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AnswerChoiceViewModel answerChoiceVM = answerChoiceViewModelList.get(position);

        if(convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.list_item_answer_choice, parent, false);
        }

        TextView txvAnswerChoice = (TextView)convertView.findViewById(R.id.txv_answer_choice);
        txvAnswerChoice.setText(answerChoiceVM.getChoice());

        convertView.setTag(answerChoiceVM);

        return convertView;
    }

}
