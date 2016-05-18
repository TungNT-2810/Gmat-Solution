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
        holder.prbProcess.getProgressDrawable().setColorFilter(
                Color.parseColor("#FF5722"), android.graphics.PorterDuff.Mode.SRC_IN);
        holder.prbProcess.setMax(10);
        holder.prbProcess.setProgress(8);

        holder.btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuestionPackListener.onQuestionPackInteraction(questionPack);
            }
        });

        ListQuestionDetailAdapter adapter = new ListQuestionDetailAdapter
                (mContext, R.layout.item_question_on_list,
                        mQuestionPackVIewModels.get(position).getQuestionViewModels());

        holder.listViewQuestion.setAdapter(adapter);


        holder.listViewQuestion.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int positionOfQuestion, long id) {
                Intent intent = new Intent(mContext, QuestionReviewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(ScoreActivity.SCOREACTIIVTY_POSITION, positionOfQuestion);
                bundle.putString(ScoreActivity.TAG_QUESTION_PACK_VIEW_MODEL, mQuestionPackVIewModels.get(position).getId());
                intent.putExtra(ScoreActivity.TAG_QUESTION_PACK_VIEW_MODEL, bundle);
                mContext.startActivity(intent);
            }
        });

        LayoutParams list = (LayoutParams) holder.listViewQuestion.getLayoutParams();
        int totalHeight = holder.listViewQuestion.getPaddingTop() + holder.listViewQuestion.getPaddingBottom();;
        for (int i = 0; i < mQuestionPackVIewModels.get(position).getQuestionViewModels().size(); i++){
            View itemView = adapter.getView(i, null, holder.listViewQuestion);
            itemView.measure(0, 0);
            totalHeight += itemView.getMeasuredHeight();
        }
        list.height = totalHeight;
        holder.totalHeight = totalHeight;
        holder.listViewQuestion.setLayoutParams(list);

        // Expand supporting text view

        if (mQuestionPackVIewModels.get(position).isShowDetail()) {
            holder.listViewQuestion.setVisibility(View.VISIBLE);
        } else {
            holder.listViewQuestion.setVisibility(View.GONE);
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
        CardView cardMain;
        int totalHeight;
        TextView txtTime;
        ProgressBar prbProcess;
        Button btnStart;
        ImageButton btnExtend;
        ListView listViewQuestion;
        QuestionPack questionPack;

        public QuestionPackViewHolder(View itemView) {
            super(itemView);
            btnExtend = (ImageButton) itemView.findViewById(R.id.imageButton);
            listViewQuestion = (ListView) itemView.findViewById(R.id.list_question_detail);
            cardMain = (CardView) itemView.findViewById(R.id.card_question_pack);
            txtTime = (TextView) itemView.findViewById(R.id.question_pack_name);
            prbProcess = (ProgressBar) itemView.findViewById(R.id.progressBar);
            btnStart = (Button) itemView.findViewById(R.id.button_question_pack);

            btnExtend.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    if (getAdapterPosition() != RecyclerView.NO_POSITION) {

                        if (mQuestionPackVIewModels.get(getAdapterPosition()).isShowDetail()) {
                            mQuestionPackVIewModels.get(getAdapterPosition()).setIsShowDetail(false);
                            collapse();
                        } else {
                            mQuestionPackVIewModels.get(getAdapterPosition()).setIsShowDetail(true);
                            expand();
                        }
                        //notifyItemChanged(getAdapterPosition());
                    }
                }
            });

            itemView.setOnClickListener(this);
        }

        private void expand() {
            //set Visible

            RotateAnimation rotate = new RotateAnimation(0, 180,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                    0.5f);
            rotate.setFillAfter(true);
            rotate.setDuration(600);
            btnExtend.setAnimation(rotate);


            listViewQuestion.setVisibility(View.VISIBLE);

            final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            listViewQuestion.measure(widthSpec, heightSpec);

            ValueAnimator mAnimator = slideAnimator(0, totalHeight);
            mAnimator.start();
        }

        private void collapse() {
            int finalHeight = listViewQuestion.getHeight();
            RotateAnimation rotate = new RotateAnimation(180, 360,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                    0.5f);
            //rotate.setFillAfter(true);
            rotate.setDuration(600);
            btnExtend.setAnimation(rotate);
            ValueAnimator mAnimator = slideAnimator(finalHeight, 0);
            mAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    //Height=0, but it set visibility to GONE
                    listViewQuestion.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }

            });
            mAnimator.start();
        }

        private ValueAnimator slideAnimator(int start, int end) {

            ValueAnimator animator = ValueAnimator.ofInt(start, end);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    //Update Height
                    int value = (Integer) valueAnimator.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = listViewQuestion.getLayoutParams();
                    layoutParams.height = value;
                    listViewQuestion.setLayoutParams(layoutParams);
                }
            });
            return animator;
        }

        @Override
        public void onClick(View v) {
            Log.d("ViewHoder", "Clicked");
            if (mMultipleSelectAdapterCallback != null && getAdapterPosition() != RecyclerView.NO_POSITION) {

                int clickedPosition = getAdapterPosition();


                notifyItemChanged(clickedPosition);

                SelectedItem selectedItem = getSelectedItem();
                //Log.d(LOG_TAG, "count selected item: " + selectedItem.getCount() + " selected item ID list: " + selectedItem.getSelectedItemIds());


                mMultipleSelectAdapterCallback.itemClicked(selectedItem.getCount(), selectedItem.getSelectedItemIds());

            }
        }
    }

//    public interface OnListFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onListFragmentInteraction(QuestionPack item);
//    }

    public interface MultipleSelectAdapterCallback {
        public void itemClicked(int count, List<String> selectedItemId);
    }

    public MultipleSelectAdapterCallback getMultipleSelectAdapterCallback() {
        return mMultipleSelectAdapterCallback;
    }

    public void setMultipleSelectAdapterCallback(MultipleSelectAdapterCallback multipleSelectAdapterCallback) {
        mMultipleSelectAdapterCallback = multipleSelectAdapterCallback;
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
