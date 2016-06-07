package org.iliat.gmat.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import org.iliat.gmat.R;
import org.iliat.gmat.constant.Constant;
import org.iliat.gmat.item_view.AnswerCRQuestionReview;
import org.iliat.gmat.model.QuestionModel;
import org.iliat.gmat.view_model.QuestionPackViewModel;
import org.iliat.gmat.view_model.QuestionViewModel;

import java.util.ArrayList;

import io.github.kexanie.library.MathView;

/**
 * Created by MrBom on 6/3/2016.
 */
public class PlaceholderFragment extends Fragment {

    public void setQuestionPack(QuestionPackViewModel mQuestionPack, int position) {
        this.mQuestionPack = mQuestionPack;
        this.position = position;
    }


    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private int position;
    private QuestionPackViewModel mQuestionPack;
    private WebView contentQuestion;
    private TextView contentQuestionText;
    private CardView cardAnswers;
    private ArrayList<AnswerCRQuestionReview> answerChoiseViewItemArrayList;
    private View contentView;
    public static Context context;
    private QuestionViewModel questionViewModel;

    private static final String ARG_SECTION_NUMBER = "section_number";

    public PlaceholderFragment() {
        Log.d("PlaceholderFragment", "PlaceholderFragment");
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber) {
        Log.d("PlaceholderFragment", "newInstance" + sectionNumber);
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("PlaceholderFragment", "onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_question_review, container, false);
        getRefercence(rootView);
        fillData();
        this.contentView = rootView;
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void fillData() {
        questionViewModel = mQuestionPack.getQuestionViewModel(position);
        if (questionViewModel != null) {
            if (questionViewModel.getTypeQuestion().equals(Constant.TYPE_Q)) {
                contentQuestion.setVisibility(View.VISIBLE);
                contentQuestionText.setVisibility(View.GONE);
                contentQuestion.loadDataWithBaseURL("file:///android_asset/mathscribe",
                        Constant.JS + questionViewModel.getStimulus() +
                                " $$cos^2θ+sin^2θ=1$$ </body></html>",
                        Constant.MIME_TYPE, Constant.HTML_ENCODE, null);
            } else {
                contentQuestion.setVisibility(View.GONE);
                contentQuestionText.setVisibility(View.VISIBLE);
                contentQuestionText.setText(questionViewModel.getStimulus());
            }
            for (int i = 0; i < 5; i++) {
                answerChoiseViewItemArrayList.get(i)
                        .setAnswerModel(questionViewModel.getAnswerChoiceViewModel(i));
                if (questionViewModel.getUserChoise() == i) {
                    answerChoiseViewItemArrayList.get(i).setUserChoise(true);
                }
                if (questionViewModel.getQuestion().getRightAnswerIndex() == i) {
                    answerChoiseViewItemArrayList.get(i).setRightAnswer(true);
                }
                answerChoiseViewItemArrayList.get(i)
                        .setQuestionType(questionViewModel.getTypeQuestion());
                answerChoiseViewItemArrayList.get(i).fillData();
            }
        }

    }

    public QuestionModel getQuestion() {
        return mQuestionPack.getQuestionViewModels().get(position).getQuestion();
    }

    private void getRefercence(View view) {

        cardAnswers = (CardView) view.findViewById(R.id.card_answer);
        contentQuestion = (WebView) view.findViewById(R.id.question_content);

        contentQuestion.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        contentQuestion.getSettings().setJavaScriptEnabled(true);
        contentQuestion.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        contentQuestionText = (TextView) view.findViewById(R.id.question_content_txt);
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
