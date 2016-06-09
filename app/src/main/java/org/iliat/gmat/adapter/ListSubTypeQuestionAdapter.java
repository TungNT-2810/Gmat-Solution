package org.iliat.gmat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;

import org.iliat.gmat.R;
import org.iliat.gmat.model.QuestionModel;
import org.iliat.gmat.model.QuestionSubTypeModel;
import org.iliat.gmat.model.QuestionType;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by MrBom on 9/6/2016.
 */
public class ListSubTypeQuestionAdapter extends BaseAdapter {

    private Context context = null;
    private ArrayList<QuestionSubTypeModel> list = null;
    private LayoutInflater inflater = null;
    private Realm realm;

    public ListSubTypeQuestionAdapter(Context context, ArrayList<QuestionSubTypeModel> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(this.context);
        realm = Realm.getDefaultInstance();
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView != null) {
            return convertView;
        }
        View view = inflater.inflate(R.layout.item_question_type_on_list, null);
        QuestionSubTypeModel questionSubTypeModel = list.get(position);
        if (view != null && questionSubTypeModel != null) {
            RealmResults<QuestionModel> listQuestion = realm.where(QuestionModel.class).equalTo("subType",
                    questionSubTypeModel.getCode()).findAll();
            int numberOfAnswered=0;
            for(QuestionModel questionModel:listQuestion){
                if(questionModel.getUserAnswer()!=-1){
                    numberOfAnswered++;
                }
            }
            TextView txtPackName = (TextView) view.findViewById(R.id.sumary_pack_name);
            TextView txtTotalQues = (TextView) view.findViewById(R.id.sumary_total_question);
            NumberProgressBar numberProgressBar = (NumberProgressBar) view.findViewById(R.id.sumary_progress);
            txtPackName.setText(questionSubTypeModel.getDetail());
            txtTotalQues.setText(listQuestion.size() + " questions");
            numberProgressBar.setMax(listQuestion.size());
            numberProgressBar.setProgress(numberOfAnswered);
        }
        return view;
    }
}
