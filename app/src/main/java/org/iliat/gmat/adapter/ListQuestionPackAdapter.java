package org.iliat.gmat.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import org.iliat.gmat.R;


import org.iliat.gmat.constant.Constant;
import org.iliat.gmat.model.QuestionPackModel;
import org.iliat.gmat.view_model.QuestionPackViewModel;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by hungtran on 3/14/16.
 * Modified by LinhDQ on 26/05/2016
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
        holder.cardMain.setCardBackgroundColor(Constant.COLOR_PICKER[position%Constant.COLOR_PICKER.length]);
        holder.txtDateOfPack.setText(mQuestionPackVIewModels.get(position).getAvailableTime());
        holder.imageView.setImageResource(Constant.PICTURES[position%Constant.PICTURES.length]);
        holder.imgRateStar.setImageResource(Constant.STARS[position%Constant.STARS.length]);
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
        private CardView cardMain;
        private TextView txtDateOfPack;
        private ImageView imageView;
        private Animation animationIn, animationOut;
        private ImageView imgRateStar;

        public QuestionPackViewModel questionPack;
        public QuestionPackViewHolder(View itemView) {
            super(itemView);
            cardMain = (CardView) itemView.findViewById(R.id.card_question_pack);
            txtDateOfPack = (TextView) itemView.findViewById(R.id.txtDateOfPack);
            imageView=(ImageView)itemView.findViewById(R.id.imgContent);
            imgRateStar=(ImageView)itemView.findViewById(R.id.imgRateStar);
            animationIn=AnimationUtils.loadAnimation(itemView.getContext(),R.anim.zoom_in);
            animationOut=AnimationUtils.loadAnimation(itemView.getContext(),R.anim.zoom_out);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            v.startAnimation(animationIn);
            animationOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mQuestionPackListener.onQuestionPackInteraction(questionPack);
                    if (mMultipleSelectAdapterCallback != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                        int clickedPosition = getAdapterPosition();
                        notifyItemChanged(clickedPosition);
                        SelectedItem selectedItem = getSelectedItem();
                        mMultipleSelectAdapterCallback.itemClicked(selectedItem.getCount(), selectedItem.getSelectedItemIds());
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            v.startAnimation(animationOut);
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
