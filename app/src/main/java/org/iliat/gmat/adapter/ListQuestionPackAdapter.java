package org.iliat.gmat.adapter;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.iliat.gmat.R;


import org.iliat.gmat.activity.QuestionReviewActivity;
import org.iliat.gmat.activity.ScoreActivity;
import org.iliat.gmat.database.QuestionPack;
import org.iliat.gmat.model.QuestionPackModel;
import org.iliat.gmat.view_model.QuestionPackViewModel;
import org.iliat.gmat.view_model.QuestionViewModel;


import java.util.ArrayList;
import android.view.ViewGroup.LayoutParams;
import java.util.List;

/**
 * Created by hungtran on 3/14/16.
 */
public class ListQuestionPackAdapter extends
        RecyclerView.Adapter<ListQuestionPackAdapter.QuestionPackViewHolder>
{
    private List<QuestionPackViewModel> mQuestionPackVIewModels;
    private OnListQuestionPackListener mQuestionPackListener;
    private MultipleSelectAdapterCallback mMultipleSelectAdapterCallback;
    private Context mContext;

    public void setContext(Context context) {
        this.mContext = context;
    }

    public ListQuestionPackAdapter() { }

    public void setQuestionPackList(List<QuestionPackModel> questionPackList){
        mQuestionPackVIewModels = new ArrayList<>();
        for(QuestionPackModel questionPack : questionPackList) {
            mQuestionPackVIewModels.add(new QuestionPackViewModel(questionPack, mContext));
        }
    }

    public void setQuestionPackListener(OnListQuestionPackListener mQuestionPackListener) {
        this.mQuestionPackListener = mQuestionPackListener;
    }

    @Override
    public QuestionPackViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.list_item_card_question_pack,
                        parent, false);
        QuestionPackViewHolder questionPackViewHolder = new QuestionPackViewHolder(v);
        return questionPackViewHolder;
    }

    @Override
    public void onBindViewHolder(QuestionPackViewHolder holder, final int position) {

        final QuestionPackViewModel questionPack = this.mQuestionPackVIewModels.get(position);

        holder.txtTime.setText(mQuestionPackVIewModels.get(position).getAvailableTime());
        holder.questionPack = questionPack;

    }

    @Override
    public int getItemCount() {
        return mQuestionPackVIewModels.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class QuestionPackViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CardView cardMain;
        TextView txtTime;
        public QuestionPackViewModel questionPack;

        public QuestionPackViewHolder(View itemView) {
            super(itemView);
            cardMain = (CardView) itemView.findViewById(R.id.card_question_pack);
            txtTime = (TextView) itemView.findViewById(R.id.question_pack_name);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            Log.d("ViewHoder", "Clicked");
            mQuestionPackListener.onQuestionPackInteraction(questionPack);
            if (mMultipleSelectAdapterCallback != null && getAdapterPosition() != RecyclerView.NO_POSITION) {

                int clickedPosition = getAdapterPosition();


                notifyItemChanged(clickedPosition);

                SelectedItem selectedItem = getSelectedItem();
                //Log.d(LOG_TAG, "count selected item: " + selectedItem.getCount() + " selected item ID list: " + selectedItem.getSelectedItemIds());


                mMultipleSelectAdapterCallback.itemClicked(selectedItem.getCount(), selectedItem.getSelectedItemIds());

            }
        }
    }


    public interface MultipleSelectAdapterCallback {
        void itemClicked(int count, List<String> selectedItemId);
    }



    public interface OnListQuestionPackListener {
        void onQuestionPackInteraction (QuestionPackViewModel item);
    }
    private SelectedItem getSelectedItem() {
        SelectedItem selectedItem = new SelectedItem();
        int counter = 0;
        if (mQuestionPackVIewModels != null) {
            for (QuestionPackViewModel item : mQuestionPackVIewModels) {
                if (item.isChecked()) {
                    counter++;
                    //selectedItem.addItemIds(item.getId());
                }
            }
        }

        selectedItem.setCount(counter);

        return selectedItem;

    }
    private class SelectedItem {

        private int mCount;
        private List<String> mSelectedItemIds = new ArrayList<>();

        private void addItemIds(String id) {
            mSelectedItemIds.add(id);
        }

        public int getCount() {
            return mCount;
        }

        public void setCount(int count) {
            mCount = count;
        }

        public List<String> getSelectedItemIds() {
            return mSelectedItemIds;
        }
    }

}
