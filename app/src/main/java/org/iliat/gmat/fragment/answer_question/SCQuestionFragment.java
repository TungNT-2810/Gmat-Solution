package org.iliat.gmat.fragment.answer_question;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.iliat.gmat.R;
import org.iliat.gmat.adapter.ListAnswerChoiceAdapter;
import org.iliat.gmat.fragment.BaseFragment;
import org.iliat.gmat.interf.ButtonNextControl;
import org.iliat.gmat.item_view.AnswerCRQuestion;
import org.iliat.gmat.view_model.QuestionViewModel;

import java.util.ArrayList;

import io.github.kexanie.library.MathView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SCQuestionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SCQuestionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SCQuestionFragment extends BaseFragment
        implements  AdapterView.OnItemSelectedListener,
                    View.OnClickListener,
                    ChangeStateOfAnswerItemsInterface{
    private final int ANSWER_CHOICE_NUM = 5;
    private ButtonNextControl buttonNextControl;
    private ArrayList<AnswerCRQuestion> answerCRQuestionArrayList;
    private MathView questionContent;


    public void setButtonNextControl(ButtonNextControl buttonNextControl) {
        this.buttonNextControl = buttonNextControl;
    }

    private ListView ltvQuestion;
    private Button btnSubmit;


    private QuestionViewModel mQuestionCRModel;

    ListAnswerChoiceAdapter adapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SCQuestionFragment() {
        // Required empty public constructor
    }


    public void setQuestion(QuestionViewModel question) {
        mQuestionCRModel = question;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SCQuestionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SCQuestionFragment newInstance(String param1, String param2) {
        SCQuestionFragment fragment = new SCQuestionFragment();
        return fragment;
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
            questionContent =  (MathView)view.findViewById(R.id.question_content);
            answerCRQuestionArrayList = new ArrayList<AnswerCRQuestion>();
            answerCRQuestionArrayList.add((AnswerCRQuestion)view.findViewById(R.id.answer_queston_1));
            answerCRQuestionArrayList.add((AnswerCRQuestion)view.findViewById(R.id.answer_queston_2));
            answerCRQuestionArrayList.add((AnswerCRQuestion)view.findViewById(R.id.answer_queston_3));
            answerCRQuestionArrayList.add((AnswerCRQuestion)view.findViewById(R.id.answer_queston_4));
            answerCRQuestionArrayList.add((AnswerCRQuestion)view.findViewById(R.id.answer_queston_5));
        }
    }

    private void fillData(){
        for (int i = 0; i < ANSWER_CHOICE_NUM; i++) {
            answerCRQuestionArrayList.get(i).setAnswerModel(mQuestionCRModel.getAnswerChoiceViewModel(i));
            answerCRQuestionArrayList.get(i).setmContext(getActivity());
            answerCRQuestionArrayList.get(i).setButtonNextControl(buttonNextControl);
            answerCRQuestionArrayList.get(i).setChangeStateOfAnswerItemsInterface(this);
            answerCRQuestionArrayList.get(i).fillData();
            questionContent.setText(mQuestionCRModel.getStimulus());
        }
    }


/*    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        TextView txvItem = (TextView)view.findViewById(R.id.txv_answer_choice);
        txvItem.setText(Html.fromHtml("<b>" + txvItem.getText().toString() + "</b>"));
        txvItem.setTextColor(ContextCompat.getColor(this.getActivity(), R.color.color_selected_answer));

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    /**
     * Hàm này nhận sự kiện ấn nut NEXT từ thằng Activity gọi vào
     * @param v
     */
    @Override
    public void onClick(View v) {

    }

    @Override
    public void changeState(int index) {
        for (int i = 0; i < ANSWER_CHOICE_NUM; i++) {
            if (i != index) {
                answerCRQuestionArrayList.get(i).setUserChoise(false);
            } else {
                answerCRQuestionArrayList.get(i).setUserChoise(true);
            }
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}


