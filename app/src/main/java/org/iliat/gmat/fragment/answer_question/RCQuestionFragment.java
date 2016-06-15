package org.iliat.gmat.fragment.answer_question;

import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ScrollView;
import android.widget.TextView;

import org.iliat.gmat.R;
import org.iliat.gmat.interf.ButtonControl;
import org.iliat.gmat.interf.ChangeStateOfAnswerItemsInterface;
import org.iliat.gmat.item_view.AnswerCRQuestion;
import org.iliat.gmat.model.QuestionModel;
import org.iliat.gmat.view_model.QuestionViewModel;

import java.util.ArrayList;


public class RCQuestionFragment extends Fragment
        implements AdapterView.OnItemSelectedListener,
        ChangeStateOfAnswerItemsInterface {

    private final int ANSWER_CHOICE_NUM = 5;

    //view
    private ButtonControl buttonControl;
    private TextView txtReadingText;
    private ScrollView scrollView;
    private TextView txtQuestion;

    //
    private QuestionViewModel mQuestionCRModel;
    private ArrayList<AnswerCRQuestion> answerCRQuestionArrayList;
    private QuestionModel question;

    public QuestionViewModel getmQuestionCRModel() {
        return mQuestionCRModel;
    }

    public void setQuestion(QuestionModel question) {
        this.question = question;
    }

    public void setButtonControl(ButtonControl buttonControl) {
        this.buttonControl = buttonControl;
    }

    public void setQuestion(QuestionViewModel question) {
        mQuestionCRModel = question;
    }

    /**change question without change stimulus
     *
     * @param questionViewModel
     */
    public void setStem(QuestionViewModel questionViewModel) {
        this.mQuestionCRModel = questionViewModel;
        for (int i = 0; i < ANSWER_CHOICE_NUM; i++) {
            answerCRQuestionArrayList.get(i).setAnswerModel(mQuestionCRModel.getAnswerChoiceViewModel(i));
            answerCRQuestionArrayList.get(i).setmContext(getActivity());
            answerCRQuestionArrayList.get(i).setButtonControl(buttonControl);
            answerCRQuestionArrayList.get(i).setChangeStateOfAnswerItemsInterface(this);
            answerCRQuestionArrayList.get(i).setUserChoise(false);
            answerCRQuestionArrayList.get(i).fillData();
        }
        txtQuestion.setText(Html.fromHtml(mQuestionCRModel.getStem()));
        buttonControl.setButtonNextState(0);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reading_question, container, false);

        initLayout(view);
        fillData();

        return view;
    }

    private void initLayout(View view) {
        if (answerCRQuestionArrayList == null) {

            txtReadingText = (TextView) view.findViewById(R.id.txtReadingText);
            txtQuestion = (TextView) view.findViewById(R.id.txtQuestion);
            scrollView = (ScrollView) view.findViewById(R.id.scrollViewAnswer);

            answerCRQuestionArrayList = new ArrayList<>();

            answerCRQuestionArrayList.add((AnswerCRQuestion) view.findViewById(R.id.answer_queston_1));
            answerCRQuestionArrayList.add((AnswerCRQuestion) view.findViewById(R.id.answer_queston_2));
            answerCRQuestionArrayList.add((AnswerCRQuestion) view.findViewById(R.id.answer_queston_3));
            answerCRQuestionArrayList.add((AnswerCRQuestion) view.findViewById(R.id.answer_queston_4));
            answerCRQuestionArrayList.add((AnswerCRQuestion) view.findViewById(R.id.answer_queston_5));
        }
    }
    public void setQuestionState(boolean isGone){
        if (isGone) {
            scrollView.setVisibility(View.GONE);
        } else {
            scrollView.setVisibility(View.VISIBLE);
        }
    }
    private void fillData() {

        for (int i = 0; i < ANSWER_CHOICE_NUM; i++) {
            answerCRQuestionArrayList.get(i).setAnswerModel(mQuestionCRModel.getAnswerChoiceViewModel(i));
            answerCRQuestionArrayList.get(i).setmContext(getActivity());
            answerCRQuestionArrayList.get(i).setButtonControl(buttonControl);
            answerCRQuestionArrayList.get(i).setChangeStateOfAnswerItemsInterface(this);
            answerCRQuestionArrayList.get(i).fillData();
            answerCRQuestionArrayList.get(i).setUserChoise(false);
            txtReadingText.setText(Html.fromHtml(mQuestionCRModel.getStimulus()));
            txtQuestion.setText(Html.fromHtml(mQuestionCRModel.getStem()));
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        TextView txvItem = (TextView) view.findViewById(R.id.txv_answer_choice);
        txvItem.setText(Html.fromHtml("<b>" + txvItem.getText().toString() + "</b>"));
        txvItem.setTextColor(ContextCompat.getColor(this.getActivity(), R.color.color_selected_answer));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void changeState(int index) {
        mQuestionCRModel.getQuestion().setUserAnswer(index);
        for (int i = 0; i < ANSWER_CHOICE_NUM; i++) {
            if (i != index) {
                answerCRQuestionArrayList.get(i).setUserChoise(false);
            } else {
                answerCRQuestionArrayList.get(i).setUserChoise(true);
            }
        }
    }

}


