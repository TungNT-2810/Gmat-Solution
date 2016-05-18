package org.iliat.gmat.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.iliat.gmat.R;

/**
 * Created by hungtran on 4/3/16.
 */
public class DialogFragmentExplantionQuestion extends DialogFragment {
    TextView txtExplantion;
    Button btnOK;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_question_explanation, container);
        getRefercenceView(view);
        return view;
    }

    public void getRefercenceView(View view){
        txtExplantion = (TextView) view.findViewById(R.id.txt_explantion);
        txtExplantion.setText("asdashfsadjlhfkasdjhfkjldsahflhasdkjthew;thsdhfkjfdhgkljdfhgkjsadhgkjlashg kjah");
        btnOK = (Button) view.findViewById(R.id.btn_ok_dialog_quesiton_explantion);
    }
}
