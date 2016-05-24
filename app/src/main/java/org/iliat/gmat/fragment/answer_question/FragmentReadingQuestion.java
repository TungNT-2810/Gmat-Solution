package org.iliat.gmat.fragment.answer_question;

import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.iliat.gmat.R;
import org.iliat.gmat.adapter.ListAnswerChoiceAdapter;
import org.iliat.gmat.fragment.BaseFragment;
import org.iliat.gmat.interf.ButtonNextControl;
import org.iliat.gmat.item_view.AnswerCRQuestion;
import org.iliat.gmat.model.QuestionModel;
import org.iliat.gmat.view_model.QuestionViewModel;
import org.w3c.dom.Document;

import java.util.ArrayList;

import io.github.kexanie.library.MathView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentReadingQuestion.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentReadingQuestion#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentReadingQuestion extends BaseFragment
        implements AdapterView.OnItemSelectedListener,
        View.OnClickListener,
        ChangeStateOfAnswerItemsInterface, View.OnTouchListener {
    private final int ANSWER_CHOICE_NUM = 5;
    private ButtonNextControl buttonNextControl;
    private ArrayList<AnswerCRQuestion> answerCRQuestionArrayList;
    private TextView txtReadingText;
    private FloatingActionButton floatingActionButton;
    private ScrollView scrollView;
    private QuestionViewModel mQuestionCRModel;
    private boolean isGone = false;
    private ImageButton imageButton;
    private float mPrevX;
    private float mPrevY;
    private TextView txtQuestion;
    private QuestionModel question;
    public static boolean isSame=false;

    public QuestionViewModel getmQuestionCRModel() {
        return mQuestionCRModel;
    }

    public void setQuestion(QuestionModel question){
        this.question=question;
    }
    public void setButtonNextControl(ButtonNextControl buttonNextControl) {
        this.buttonNextControl = buttonNextControl;
    }

    private OnFragmentInteractionListener mListener;

    public FragmentReadingQuestion() {
        // Required empty public constructor
    }


    public void setQuestion(QuestionViewModel question) {
        mQuestionCRModel = question;
    }

    public static FragmentReadingQuestion newInstance(String param1, String param2) {
        FragmentReadingQuestion fragment = new FragmentReadingQuestion();
        return fragment;
    }
    public void setStem(QuestionViewModel questionViewModel){
        this.mQuestionCRModel=questionViewModel;
        for (int i = 0; i < ANSWER_CHOICE_NUM; i++) {
            answerCRQuestionArrayList.get(i).setAnswerModel(mQuestionCRModel.getAnswerChoiceViewModel(i));
            answerCRQuestionArrayList.get(i).setmContext(getActivity());
            answerCRQuestionArrayList.get(i).setButtonNextControl(buttonNextControl);
            answerCRQuestionArrayList.get(i).setChangeStateOfAnswerItemsInterface(this);
            answerCRQuestionArrayList.get(i).setUserChoise(false);
            answerCRQuestionArrayList.get(i).fillData();
        }
        txtQuestion.setText(Html.fromHtml(mQuestionCRModel.getStem()));
        buttonNextControl.setButtonNextState(0);
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
            imageButton = (ImageButton) view.findViewById(R.id.btnImgButton);
            txtQuestion=(TextView)view.findViewById(R.id.txtQuestion);
            //imageButton.setOnTouchListener(this);
            //floatingActionButton=(FloatingActionButton)view.findViewById(R.id.btnFloatingButton);
            scrollView = (ScrollView) view.findViewById(R.id.scrollViewAnswer);
            answerCRQuestionArrayList = new ArrayList<>();
            answerCRQuestionArrayList.add((AnswerCRQuestion) view.findViewById(R.id.answer_queston_1));
            answerCRQuestionArrayList.add((AnswerCRQuestion) view.findViewById(R.id.answer_queston_2));
            answerCRQuestionArrayList.add((AnswerCRQuestion) view.findViewById(R.id.answer_queston_3));
            answerCRQuestionArrayList.add((AnswerCRQuestion) view.findViewById(R.id.answer_queston_4));
            answerCRQuestionArrayList.add((AnswerCRQuestion) view.findViewById(R.id.answer_queston_5));
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!isGone){
                        scrollView.setVisibility(View.GONE);
                        imageButton.setImageResource(R.drawable.ic_library_add_white_24dp);
                        isGone=true;
                    }else{
                        scrollView.setVisibility(View.VISIBLE);
                        imageButton.setImageResource(R.drawable.ic_clear_white_24dp);
                        isGone=false;
                    }
                }
            });
        }
    }
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        float currX,currY;
        int action = event.getAction();
        switch (action ) {
            case MotionEvent.ACTION_DOWN: {

                mPrevX = event.getX();
                mPrevY = event.getY();
                break;
            }

            case MotionEvent.ACTION_MOVE:
            {

                currX = event.getRawX();
                currY = event.getRawY();


                ViewGroup.MarginLayoutParams marginParams = new ViewGroup.MarginLayoutParams(view.getLayoutParams());
                marginParams.setMargins((int)(currX ), (int)(currY ),(int)(view.getWidth()-currX), (int)(view.getHeight()-currY));
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(marginParams);
                view.setLayoutParams(layoutParams);


                break;
            }



            case MotionEvent.ACTION_CANCEL:
                break;

            case MotionEvent.ACTION_UP:

                break;
        }

        return true;
    }
    private void fillData() {

        for (int i = 0; i < ANSWER_CHOICE_NUM; i++) {
            answerCRQuestionArrayList.get(i).setAnswerModel(mQuestionCRModel.getAnswerChoiceViewModel(i));
            answerCRQuestionArrayList.get(i).setmContext(getActivity());
            answerCRQuestionArrayList.get(i).setButtonNextControl(buttonNextControl);
            answerCRQuestionArrayList.get(i).setChangeStateOfAnswerItemsInterface(this);
            answerCRQuestionArrayList.get(i).fillData();
            answerCRQuestionArrayList.get(i).setUserChoise(false);
            txtReadingText.setText(Html.fromHtml(mQuestionCRModel.getStimulus()));
            txtQuestion.setText(Html.fromHtml(mQuestionCRModel.getStem()));
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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


    /**
     * Hàm này nhận sự kiện ấn nut NEXT từ thằng Activity gọi vào
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.btnFloatingButton:{
//                if(!isGone){
//                    scrollView.setVisibility(View.GONE);
//                    floatingActionButton.setBackgroundResource(R.drawable.ic_clear_white_24dp);
//                    isGone=true;
//                }else{
//                    scrollView.setVisibility(View.VISIBLE);
//                    floatingActionButton.setBackgroundResource(R.drawable.ic_library_add_white_24dp);
//                    isGone=false;
//                }
//                break;
//            }
            default:
                break;
        }
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}


