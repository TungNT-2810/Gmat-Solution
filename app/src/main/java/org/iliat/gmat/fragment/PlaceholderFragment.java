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

    public void setQuestionPack(QuestionPackViewModel mQuestionPack) {
        this.mQuestionPack = mQuestionPack;
    }


    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private int position;
    private QuestionPackViewModel mQuestionPack;
    private WebView contentQuestion;
    private MathView stemQuestion;
    private CardView cardAnswers;
    private ArrayList<AnswerCRQuestionReview> answerChoiseViewItemArrayList;
    private View contentView;
    public static Context context;

    private static final String ARG_SECTION_NUMBER = "section_number";

    public PlaceholderFragment() {
        Log.d("PlaceholderFragment", "PlaceholderFragment");
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber) {
        Log.d("PlaceholderFragment", "newInstance");
        PlaceholderFragment fragment = new PlaceholderFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_question_review, container, false);
        getRefercence(rootView);
        this.contentView = rootView;
        return rootView;

    }

    @Override
    public void onResume() {
        super.onResume();
        fillData();
        for (int i = 0; i < 5; i++) {
            answerChoiseViewItemArrayList.get(i)
                    .setAnswerModel(mQuestionPack.getQuestionViewModels().get(position).getAnswerChoiceViewModel(i));
            if (mQuestionPack.getQuestionViewModels().get(position).getUserChoise() == i) {
                answerChoiseViewItemArrayList.get(i).setUserChoise(true);
            }
            if (mQuestionPack.getQuestionViewModels().get(position).getQuestion().getRightAnswerIndex() == i) {
                answerChoiseViewItemArrayList.get(i).setRightAnswer(true);
            }
            answerChoiseViewItemArrayList.get(i).setQuestionType(mQuestionPack.getQuestionViewModels().get(position).getQuestion().getType());
            answerChoiseViewItemArrayList.get(i).fillData();
        }
    }

    private void fillData() {
        final QuestionViewModel questionViewModel = (mQuestionPack.getQuestionViewModels().get(position));
        if (questionViewModel != null) {
            contentQuestion.loadDataWithBaseURL("file:///android_asset/mathscribe",
                    Constant.js + questionViewModel.getStimulus() +
                            " $$cos^2θ+sin^2θ=1$$ </body></html>", "text/html; charset=utf-8", "UTF-8", null);
        }
    }

    public QuestionModel getQuestion() {
        return mQuestionPack.getQuestionViewModels().get(position).getQuestion();
    }

    private void getRefercence(View view) {
        cardAnswers = (CardView) view.findViewById(R.id.card_answer);
        contentQuestion = (WebView) view.findViewById(R.id.question_content);
        if (Build.VERSION.SDK_INT >= 19) {
            contentQuestion.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            contentQuestion.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        contentQuestion.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        contentQuestion.getSettings().setJavaScriptEnabled(true);

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
