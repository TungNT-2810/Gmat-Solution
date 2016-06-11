package org.iliat.gmat.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import org.iliat.gmat.R;
import org.iliat.gmat.item_view.AnswerCRQuestionReview;
import org.iliat.gmat.model.QuestionModel;
import org.iliat.gmat.view_model.QuestionPackViewModel;
import org.iliat.gmat.view_model.QuestionViewModel;

import java.util.ArrayList;

import io.realm.RealmList;

/**
 * Created by MrBom on 6/3/2016.
 */
public class PlaceholderFragmentRC extends Fragment {

    public void setQuestionPack(RealmList<QuestionModel> listQuestion) {
        this.listQuestion = listQuestion;
    }

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */

    private int position;
    private RealmList<QuestionModel> listQuestion;
    private ArrayList<AnswerCRQuestionReview> answerChoiseViewItemArrayList;
    public static Context context;
    private TextView txtReadingText;
    private TextView txtQuestion;
    public ScrollView scrollView;

    private static final String ARG_SECTION_NUMBER = "section_number";

    public PlaceholderFragmentRC() {
        Log.d("PlaceholderFragment", "PlaceholderFragment");
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragmentRC newInstance(int sectionNumber) {
        Log.d("PlaceholderFragment", "newInstance");
        PlaceholderFragmentRC fragment = new PlaceholderFragmentRC();
        fragment.position = sectionNumber - 1;
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("PlaceholderFragment", "onCreateView");

        View rootView = inflater.inflate(R.layout.fragment_reading_question_review, container, false);
        getRefercence(rootView);
        return rootView;

    }

    public void showHideScrollView(boolean visible) {
        if (visible) {
            scrollView.setVisibility(View.VISIBLE);
        } else {
            scrollView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        fillData();
    }

    private void fillData() {
        final QuestionViewModel questionViewModel = new QuestionViewModel(listQuestion.get(position));
        txtReadingText.setText(questionViewModel.getStimulus());
        txtQuestion.setText(questionViewModel.getStem());
        for (int i = 0; i < 5; i++) {
            answerChoiseViewItemArrayList.get(i)
                    .setAnswerModel(questionViewModel.getAnswerChoiceViewModel(i));
            if (questionViewModel.getUserChoise() == i) {
                answerChoiseViewItemArrayList.get(i).setUserChoise(true);
            }
            if (questionViewModel.getQuestion().getRightAnswerIndex() == i) {
                answerChoiseViewItemArrayList.get(i).setRightAnswer(true);
            }
            answerChoiseViewItemArrayList.get(i).setQuestionType(questionViewModel.getQuestion().getType());
            answerChoiseViewItemArrayList.get(i).fillData();
        }
    }

    public QuestionModel getQuestion() {
        return listQuestion.get(position);
    }

    private void getRefercence(View view) {

        txtReadingText = (TextView) view.findViewById(R.id.txtReadingText);
        txtQuestion = (TextView) view.findViewById(R.id.txtQuestion);
        scrollView = (ScrollView) view.findViewById(R.id.scrollViewAnswer1);

        if (answerChoiseViewItemArrayList == null) {
            answerChoiseViewItemArrayList = new ArrayList<AnswerCRQuestionReview>();
            AnswerCRQuestionReview answerChoiseViewItem0 = (AnswerCRQuestionReview) view.findViewById(R.id.answer_queston_review_1);
            AnswerCRQuestionReview answerChoiseViewItem1 = (AnswerCRQuestionReview) view.findViewById(R.id.answer_queston_review_2);
            AnswerCRQuestionReview answerChoiseViewItem2 = (AnswerCRQuestionReview) view.findViewById(R.id.answer_queston_review_3);
            AnswerCRQuestionReview answerChoiseViewItem3 = (AnswerCRQuestionReview) view.findViewById(R.id.answer_queston_review_4);
            AnswerCRQuestionReview answerChoiseViewItem4 = (AnswerCRQuestionReview) view.findViewById(R.id.answer_queston_review_5);
            answerChoiseViewItemArrayList.add(answerChoiseViewItem0);
            answerChoiseViewItemArrayList.add(answerChoiseViewItem1);
            answerChoiseViewItemArrayList.add(answerChoiseViewItem2);
            answerChoiseViewItemArrayList.add(answerChoiseViewItem3);
            answerChoiseViewItemArrayList.add(answerChoiseViewItem4);
        }
    }
}
