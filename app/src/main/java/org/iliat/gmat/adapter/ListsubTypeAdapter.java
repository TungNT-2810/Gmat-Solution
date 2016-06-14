package org.iliat.gmat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.iliat.gmat.R;
import org.iliat.gmat.db_connect.DBContext;
import org.iliat.gmat.model.QuestionSubTypeModel;

import io.realm.RealmList;

/**
 * Created by MrBom on 6/10/2016.
 */
public class ListSubTypeAdapter extends BaseAdapter {
    private Context context = null;
    private RealmList<QuestionSubTypeModel> list = null;
    private LayoutInflater inflater = null;
    private String typeCode;

    public ListSubTypeAdapter(Context context, RealmList<QuestionSubTypeModel> list, String typeCode) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(this.context);
        this.typeCode = typeCode;
    }

    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (list != null && position<list.size()) {
            return list.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_sub_type_on_list, null);
        QuestionSubTypeModel questionSubTypeModel = list.get(position);
        if (view != null && questionSubTypeModel != null) {
            TextView txtSubTypeName = (TextView) view.findViewById(R.id.sub_type_name);
            TextView txtSubTypeTotalQues = (TextView) view.findViewById(R.id.sub_type_total_ques);
            int totalQues = DBContext.getNumberQuestionByTypeAndSubTypeCode(typeCode, questionSubTypeModel.getCode());
            txtSubTypeName.setText(questionSubTypeModel.getDetail());
            if (totalQues > 1) {
                txtSubTypeTotalQues.setText("Total " + totalQues + " questions");
            } else if (totalQues == 1) {
                txtSubTypeTotalQues.setText("Total " + totalQues + " question");
            } else {
                txtSubTypeTotalQues.setText("Empty");
            }
            return view;
        }
        return null;
    }
}
