package org.iliat.gmat.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;

import org.iliat.gmat.GMATApplication;
import org.iliat.gmat.R;
import org.iliat.gmat.db_connect.DBContext;
import org.iliat.gmat.enitity.DownloadJSONTask;
import org.iliat.gmat.enitity.JSONParser;
import org.iliat.gmat.enitity.JSONPostDownloadHandler;
import org.iliat.gmat.enitity.JSONPreDownloadHandler;
import org.iliat.gmat.model.AnswerModel;
import org.iliat.gmat.model.QuestionModel;
import org.iliat.gmat.model.QuestionPackModel;
import org.iliat.gmat.model.QuestionSubTypeModel;
import org.iliat.gmat.model.QuestionTypeModel;
import org.iliat.gmat.network.APIUrls;
import org.iliat.gmat.network.JSONAnswerChoice;
import org.iliat.gmat.network.JSONLogin;
import org.iliat.gmat.network.JSONQuestion;
import org.iliat.gmat.network.JSONQuestionList;
import org.iliat.gmat.network.JSONQuestionPack;
import org.iliat.gmat.network.JSONQuestionPackList;
import org.iliat.gmat.network.JSONQuestionSubType;
import org.iliat.gmat.network.JSONQuestionType;
import org.iliat.gmat.network.JSONQuestionTypeList;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements JSONPreDownloadHandler,
        JSONPostDownloadHandler, JSONParser {

    private static final String TAG = LoginActivity.class.toString();

    private static final String TAG_QUESION_PACK_DOWNLOAD = "question pack download";
    private static final String TAG_QUESION_DOWNLOAD = "question download";
    private static final String TAG_QUESION_TYPE_DOWNLOAD = "question type download";
    private final String DOWNLOAD_QUESTION_TAG = "Download question";
    private final String DOWNLOAD_QUESTION_PACK_TAG = "Download question pack";
    private final String DOWNLOAD_QUESTION_TYPE_TAG = "Download question type";

    private TextInputLayout inputLayoutEmail, inputLayoutPassword;
    private Button mLoginButton;
    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private CoordinatorLayout mCoordinatorLayout;
    private Snackbar mSnackbar;
    private Toast toast;

    private OkHttpClient mHttpClient;
    private DBContext dbContext;

    private boolean mQuestionDownloadCompleted = false;
    private boolean mQuestionPackDownloadCompleted = false;
    private boolean mQuestionTypeDownloadCompleted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferences sharedPreferences = getSharedPreferences(GMATApplication.SHARE_PREFERENCES, MODE_PRIVATE);
        boolean isLogged = sharedPreferences.getBoolean(GMATApplication.LOGIN_SHARE_PREFERENCES, false);

        if (!isLogged) {
            this.initUtils();
            this.initLayout();
            this.registerEvents();

        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            this.finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        dbContext = DBContext.getInst();
    }

    private void initUtils() {
        this.mHttpClient = new OkHttpClient();
    }

    private void initLayout() {
        inputLayoutEmail = (TextInputLayout) this.findViewById(R.id.input_layout_email);
        inputLayoutPassword = (TextInputLayout) this.findViewById(R.id.input_layout_password);
        mEmailEditText = (EditText) this.findViewById(R.id.input_email);
        mPasswordEditText = (EditText) this.findViewById(R.id.input_password);
        mEmailEditText.addTextChangedListener(new MyTextWatcher(mEmailEditText));
        mPasswordEditText.addTextChangedListener(new MyTextWatcher(mPasswordEditText));
        mLoginButton = (Button) findViewById(R.id.button_login);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        toast = Toast.makeText(LoginActivity.this, "Login Fail", Toast.LENGTH_SHORT);
        mSnackbar =
                Snackbar.make(mCoordinatorLayout,
                        getString(R.string.logging_in),
                        Snackbar.LENGTH_INDEFINITE);
        Snackbar.SnackbarLayout snack_view = (Snackbar.SnackbarLayout) mSnackbar.getView();
        snack_view.addView(new ProgressBar(this));
        mHttpClient = new OkHttpClient();

    }

    private void registerEvents() {
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSnackbar.setDuration(Snackbar.LENGTH_INDEFINITE);
                mSnackbar.show();
                login();
            }
        });
    }


    private void login() {
        RequestBody formBody = new FormBody.Builder()
                .add("username", mEmailEditText.getText().toString())
                .add("password", mPasswordEditText.getText().toString())
                .build();
        Request request = new Request.Builder()
                .url(APIUrls.LOGIN_API)
                .post(formBody)
                .build();
        mHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                JSONLogin jsonLogin = (
                        new Gson()).fromJson(response.body().charStream(),
                        JSONLogin.class);
                if (jsonLogin.getLogin_status() == 1) {
                    SharedPreferences sharedPreferences = getSharedPreferences(GMATApplication.SHARE_PREFERENCES, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(GMATApplication.LOGIN_SHARE_PREFERENCES, true);
                    editor.putString(GMATApplication.EMAIL_SHARE_PREFERENCES, mEmailEditText.getText().toString());
                    editor.putString(GMATApplication.PASSWORD_SHARE_PREFERENCES, mPasswordEditText.getText().toString());
                    editor.commit();
                    mSnackbar.setText("Downloading data...");
                    downloadQuestions();
                } else {
                    mSnackbar.dismiss();
                    toast.show();
                }
            }
        });
    }


    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);
        this.finish();
    }

    private boolean validateEmail() {
        String email = mEmailEditText.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(mEmailEditText);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (mPasswordEditText.getText().toString().trim().isEmpty()) {
            inputLayoutPassword.setError(getString(R.string.err_msg_password));
            requestFocus(mPasswordEditText);
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void downloadQuestionPacks() {
        mSnackbar.setDuration(Snackbar.LENGTH_INDEFINITE);
        mSnackbar.show();

        String url = APIUrls.QUESTION_PACKS_API;
        Request request = new Request.Builder()
                .url(url)
                .build();

        mHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onJSONDownloadFinished(TAG_QUESION_PACK_DOWNLOAD, false);
                Log.d(TAG, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                JSONQuestionPackList jsonQuestionPackList = (
                        new Gson()).fromJson(response.body().charStream(),
                        JSONQuestionPackList.class);
                Log.d(TAG, String.valueOf(jsonQuestionPackList.getList().size()));
                saveQuestionPacks(jsonQuestionPackList);

                for (JSONQuestionPack jsonQuestionPack : jsonQuestionPackList.getList()) {
                    Log.d(TAG, jsonQuestionPack.getId());
                }

                onJSONDownloadFinished(TAG_QUESION_PACK_DOWNLOAD, true);
            }
        });
    }

    private void downloadQuestions() {
        mSnackbar.setDuration(Snackbar.LENGTH_INDEFINITE);
        mSnackbar.show();

        String url = APIUrls.QUESTIONS_API;
        Request request = new Request.Builder()
                .url(url)
                .build();

        mHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onJSONDownloadFinished(TAG_QUESION_DOWNLOAD, false);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                JSONQuestionList jsonQuestionList = (
                        new Gson()).fromJson(response.body().charStream(),
                        JSONQuestionList.class);

                saveQuestions(jsonQuestionList);

                Log.d(TAG, "Download question " + jsonQuestionList.getList().size());
                onJSONDownloadFinished(TAG_QUESION_DOWNLOAD, true);
            }
        });
    }

    private void downloadQuestionType() {
        mSnackbar.setDuration(Snackbar.LENGTH_INDEFINITE);
        mSnackbar.show();

        String url = APIUrls.QUESTION_TYPE_API;
        Request request = new Request.Builder()
                .url(url)
                .build();

        mHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onJSONDownloadFinished(TAG_QUESION_TYPE_DOWNLOAD, false);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                JSONQuestionTypeList jsonQuestionTypeList = (
                        new Gson()).fromJson(response.body().charStream(),
                        JSONQuestionTypeList.class);
                saveQuestionType(jsonQuestionTypeList);
                onJSONDownloadFinished(TAG_QUESION_TYPE_DOWNLOAD, true);
            }
        });
    }

    private void saveQuestionType(JSONQuestionTypeList jsonQuestionTypeList) {
        dbContext.saveQuestionType(jsonQuestionTypeList);
    }

    private void saveQuestions(JSONQuestionList jsonQuestionList) {
        dbContext.saveQuestions(jsonQuestionList);
    }

    private void saveQuestionPacks(JSONQuestionPackList jsonQuestionPackList) {
        dbContext.saveQuestionPacks(jsonQuestionPackList);
    }

    private void onJSONDownloadFinished(String tag, boolean result) {
        if (!result) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    mSnackbar.setText(getString(R.string.download_failed));
                    mSnackbar.setDuration(Snackbar.LENGTH_LONG);

                }
            });

        } else {
            mSnackbar.dismiss();
            if (tag == TAG_QUESION_PACK_DOWNLOAD) {
                mQuestionPackDownloadCompleted = true;
            } else if (tag == TAG_QUESION_DOWNLOAD) {
                mQuestionDownloadCompleted = true;
                downloadQuestionPacks();
                downloadQuestionType();
            } else if (tag == TAG_QUESION_TYPE_DOWNLOAD) {
                mQuestionTypeDownloadCompleted = true;
            }
            if (mQuestionDownloadCompleted && mQuestionPackDownloadCompleted) {
                /* Good, move to next screen */
                goToMainActivity();
            }
        }
    }

    @Override
    public void onDownload(InputStreamReader inputStreamReader, String tag) {

    }

    @Override
    public void onPostDownload(JSONObject jsonObject, String tag) {
        switch (tag) {
            case DOWNLOAD_QUESTION_TAG:
                mQuestionDownloadCompleted = true;
                break;
            case DOWNLOAD_QUESTION_PACK_TAG:
                mQuestionPackDownloadCompleted = true;
                break;
            case DOWNLOAD_QUESTION_TYPE_TAG: {
                mQuestionTypeDownloadCompleted = true;
                break;
            }
        }

        if (mQuestionDownloadCompleted && mQuestionPackDownloadCompleted && mQuestionTypeDownloadCompleted) {
            goToMainActivity();
        }
    }

    @Override
    public void onPreDownload(String tag) {
        switch (tag) {
            case DOWNLOAD_QUESTION_TAG:
                break;
            case DOWNLOAD_QUESTION_PACK_TAG:
                break;
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_email:
                    validateEmail();
                    break;
                case R.id.input_password:
                    validatePassword();
                    break;
            }
        }
    }
}
