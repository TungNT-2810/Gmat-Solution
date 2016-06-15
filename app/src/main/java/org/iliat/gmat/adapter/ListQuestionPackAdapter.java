package org.iliat.gmat.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.iliat.gmat.R;


import org.iliat.gmat.constant.Constant;
import org.iliat.gmat.model.QuestionPackModel;
import org.iliat.gmat.view_model.QuestionPackViewModel;


import java.text.SimpleDateFormat;
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
    private Toast toast;
    private LayoutInflater inflater;

    public void setContext(Context context) {
        this.mContext = context;
        inflater=LayoutInflater.from(this.mContext);
    }

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
        View v = inflater.inflate(R.layout.list_item_card_question_pack, parent, false);

        QuestionPackViewHolder questionPackViewHolder = new QuestionPackViewHolder(v);

        //init toast
        toast=Toast.makeText(v.getContext(), "This package is not available for today!",
                Toast.LENGTH_SHORT);

        return questionPackViewHolder;
    }

    @Override
    public void onBindViewHolder(QuestionPackViewHolder holder, final int position) {

        final QuestionPackViewModel questionPack = this.mQuestionPackVIewModels.get(position);
        if(questionPack!=null) {
            holder.cardMain.setCardBackgroundColor(Constant.COLOR_PICKER[position % Constant.COLOR_PICKER.length]);
            holder.txtDateOfPack.setText(questionPack.getAvailableTime());
            holder.imgRateStar.setImageResource(Constant.STARS[questionPack.getQuestionPack().getLevel()]);
            holder.txtPackIndex.setText(String.format("%d/%d", (position + 1), mQuestionPackVIewModels.size()));
            holder.questionPack = questionPack;

            holder.imgStatusLock.setVisibility(View.GONE);
            holder.txtAnswered.setVisibility(View.VISIBLE);
            holder.txtStatus.setVisibility(View.VISIBLE);
            holder.txtCorrect.setVisibility(View.VISIBLE);
            holder.txtStatusCorrect.setVisibility(View.VISIBLE);

            if (questionPack.isNew() && !questionPack.haveAnyTag()) {
                holder.txtStatusCorrect.setVisibility(View.INVISIBLE);
                holder.txtAnswered.setText(String.valueOf(questionPack.getNumberOfQuestions()));
                holder.txtCorrect.setText("New");
                holder.txtStatus.setText("Questions");
            } else {
                Log.d("DM",questionPack.getNumberQuestionAnswered()+"");
                holder.txtStatus.setText("Completed");
                holder.txtStatusCorrect.setVisibility(View.VISIBLE);
                holder.txtAnswered.setText(String.format("%d/%d", questionPack.getNumberQuestionAnswered(),
                        questionPack.getNumberOfQuestions()));
                holder.txtCorrect.setText(String.format("%.1f",
                        (float) questionPack.getNumberOfCorrectAnswers()*100 / questionPack.getNumberOfQuestions()) + "%");
            }

            //check available time to unpack
            if (!questionPack.isUnpack(position)) {
                holder.txtAnswered.setVisibility(View.GONE);
                holder.txtStatus.setVisibility(View.GONE);
                holder.txtCorrect.setVisibility(View.GONE);
                holder.txtStatusCorrect.setVisibility(View.GONE);
                holder.imgStatusLock.setVisibility(View.VISIBLE);
                holder.cardMain.setCardBackgroundColor(Color.parseColor("#9E9E9E"));
            }
        }

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
        //view
        private CardView cardMain;
        private TextView txtDateOfPack;
        private ImageView imgRateStar;
        private TextView txtPackIndex;
        private TextView txtAnswered;
        private TextView txtStatus;
        private TextView txtCorrect;
        private TextView txtStatusCorrect;
        private ImageView imgStatusLock;
        private Animation animationIn, animationOut;

        public QuestionPackViewModel questionPack;

        public QuestionPackViewHolder(View itemView) {
            super(itemView);
            //init controls
            cardMain = (CardView) itemView.findViewById(R.id.card_question_pack);
            txtDateOfPack = (TextView) itemView.findViewById(R.id.txtDateOfPack);
            imgRateStar=(ImageView)itemView.findViewById(R.id.imgRateStar);
            txtPackIndex = (TextView) itemView.findViewById(R.id.txt_pack_index);
            txtAnswered = (TextView) itemView.findViewById(R.id.txt_answered);
            txtStatus = (TextView) itemView.findViewById(R.id.txt_status);
            txtCorrect = (TextView) itemView.findViewById(R.id.txt_correct);
            imgStatusLock = (ImageView) itemView.findViewById(R.id.status_lock);
            txtStatusCorrect = (TextView) itemView.findViewById(R.id.txt_status_correct);
            animationIn=AnimationUtils.loadAnimation(itemView.getContext(),R.anim.zoom_in);
            animationOut=AnimationUtils.loadAnimation(itemView.getContext(),R.anim.zoom_out);

            //add listener
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(final View v) {
            v.startAnimation(animationIn);
            animationOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    if(toast!=null){
                        toast.cancel();
                    }
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (questionPack.isUnpack(getAdapterPosition())) {
                        mQuestionPackListener.onQuestionPackInteraction(questionPack);
                        if (mMultipleSelectAdapterCallback != null && getAdapterPosition()
                                != RecyclerView.NO_POSITION) {
                            int clickedPosition = getAdapterPosition();
                            notifyItemChanged(clickedPosition);
                            SelectedItem selectedItem = getSelectedItem();
                            mMultipleSelectAdapterCallback.itemClicked(selectedItem.getCount(), selectedItem.getSelectedItemIds());
                        }
                    } else {
                        toast.show();
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
                }
            }
        }

        selectedItem.setCount(counter);

        return selectedItem;

    }
    private class SelectedItem {

        private int mCount;

        private List<String> mSelectedItemIds = new ArrayList<>();

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
