package org.iliat.gmat.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.iliat.gmat.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ExplanationAnswerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ExplanationAnswerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExplanationAnswerFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String content;
    String answerChoice;
    TextView txtContent;
    TextView txtAnswer;
    Button btnClose;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public void setContent(String content, String answerChoice) {
        this.answerChoice = answerChoice;
        this.content = content;
        if(txtContent !=  null){
            txtContent.setText(content);
        }
    }

    private OnFragmentInteractionListener mListener;

    public ExplanationAnswerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExplanationAnswerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExplanationAnswerFragment newInstance(String param1, String param2) {
        ExplanationAnswerFragment fragment = new ExplanationAnswerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.dialog_question_explanation, container, false);
        connectView(view);
        return view;
    }

    private void connectView(View view){
        txtContent = (TextView) view.findViewById(R.id.txt_explantion);
        txtAnswer = (TextView) view.findViewById(R.id.txt_answer);
        txtAnswer.setText(answerChoice);
        txtContent.setText(content);
        btnClose = (Button) view.findViewById(R.id.btn_ok_dialog_quesiton_explantion);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExplanationAnswerFragment.this.dismiss();
            }
        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        this.getDialog().setTitle(R.string.explanation);
        super.onViewCreated(view, savedInstanceState);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

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
