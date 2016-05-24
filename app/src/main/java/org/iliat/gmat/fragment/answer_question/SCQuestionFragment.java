package org.iliat.gmat.fragment.answer_question;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import org.iliat.gmat.R;
import org.iliat.gmat.fragment.BaseFragment;
import org.iliat.gmat.interf.ButtonNextControl;
import org.iliat.gmat.item_view.AnswerCRQuestion;
import org.iliat.gmat.view_model.QuestionViewModel;

import java.util.ArrayList;

import io.github.kexanie.library.MathView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link SCQuestionFragment} factory method to
 * create an instance of this fragment.
 */
public class SCQuestionFragment extends BaseFragment
        implements AdapterView.OnItemSelectedListener,
        ChangeStateOfAnswerItemsInterface {
    private final int ANSWER_CHOICE_NUM = 5;
    private ButtonNextControl buttonNextControl;
    private ArrayList<AnswerCRQuestion> answerCRQuestionArrayList;
    private MathView questionContent;


    public void setButtonNextControl(ButtonNextControl buttonNextControl) {
        this.buttonNextControl = buttonNextControl;
    }

    private QuestionViewModel mQuestionCRModel;

    // TODO: Rename and change types of parameters

    public SCQuestionFragment() {
        // Required empty public constructor
    }

    public void setQuestion(QuestionViewModel question) {
        mQuestionCRModel = question;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_single_question, container, false);
        initLayout(view);
        fillData();
        return view;
    }

    private void initLayout(View view) {
        if (answerCRQuestionArrayList == null) {
            questionContent = (MathView) view.findViewById(R.id.question_content);
            answerCRQuestionArrayList = new ArrayList<>();
            answerCRQuestionArrayList.add((AnswerCRQuestion) view.findViewById(R.id.answer_queston_1));
            answerCRQuestionArrayList.add((AnswerCRQuestion) view.findViewById(R.id.answer_queston_2));
            answerCRQuestionArrayList.add((AnswerCRQuestion) view.findViewById(R.id.answer_queston_3));
            answerCRQuestionArrayList.add((AnswerCRQuestion) view.findViewById(R.id.answer_queston_4));
            answerCRQuestionArrayList.add((AnswerCRQuestion) view.findViewById(R.id.answer_queston_5));
        }
    }

    private void fillData() {
        for (int i = 0; i < ANSWER_CHOICE_NUM; i++) {
            answerCRQuestionArrayList.get(i).setAnswerModel(mQuestionCRModel.getAnswerChoiceViewModel(i));
            answerCRQuestionArrayList.get(i).setmContext(getActivity());
            answerCRQuestionArrayList.get(i).setButtonNextControl(buttonNextControl);
            answerCRQuestionArrayList.get(i).setChangeStateOfAnswerItemsInterface(this);
            answerCRQuestionArrayList.get(i).fillData();
            questionContent.setText(mQuestionCRModel.getStimulus());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
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


