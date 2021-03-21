package com.dhb.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dhb.R;
import com.dhb.network.ApiCallAsyncTask;
import com.dhb.network.ApiCallAsyncTaskDelegate;
import com.dhb.network.AsyncTaskForRequest;
import com.dhb.network.ResponseParser;
import com.dhb.uiutils.AbstractActivity;
import com.dhb.utils.AlertDialogMessage;
import com.dhb.utils.AppConstants;

import org.json.JSONException;

public class ForgotPasswordActivity extends AbstractActivity {

    private EditText edtMobile;
    private Button btnForgotPassword;
    private RelativeLayout rltLayoutParent;
    private Activity activity;
    private ApiCallAsyncTask forgotApiCallAsyncTask;
    private TextView txtMobile;
    private TextView txtDigital;
    private TextView txtHealthbook;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activirt_forgot_password);
        initUI();
        setListener();
        setFont();
        if (AppConstants.LOGIN_TYPE.equalsIgnoreCase("Reception")){
            showScreenId(getResources().getString(R.string.screen_no_rec_forgot_password));
        } else if (AppConstants.LOGIN_TYPE.equalsIgnoreCase("Doctor")){
            showScreenId(getResources().getString(R.string.screen_no_doc_forgot_password));
        }
    }

    @Override
    public void initUI() {
        super.initUI();
        activity = this;
        edtMobile = (EditText) findViewById(R.id.et_mobile);
        txtMobile = (TextView) findViewById(R.id.txt_mobile);
        btnForgotPassword = (Button) findViewById(R.id.btn_forgot_password);
        rltLayoutParent = (RelativeLayout) findViewById(R.id.rlt_layout_forgot_password);
        txtDigital = (TextView) findViewById(R.id.txt_power_digital);
        txtHealthbook = (TextView) findViewById(R.id.txt_power_heath_book);
    }

    private void setListener() {

        btnForgotPassword.setOnClickListener(new ForgotPasswordOnclickListener());
        rltLayoutParent.setOnClickListener(new ParentRelativeLayoutOnclickListener());
        edtMobile.setOnEditorActionListener(new EditorActionListener());

    }

    private void setFont() {
        txtMobile.setTypeface(fontOpenRobotoRegular);
        edtMobile.setTypeface(fontOpenRobotoRegular);
        btnForgotPassword.setTypeface(fontOpenRobotoRegular);
        txtHealthbook.setTypeface(fontArialBold);
        txtDigital.setTypeface(fontArialBold);
    }

    private class ForgotPasswordOnclickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

           /* if (validate()){
                AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
                try {
                    forgotApiCallAsyncTask = asyncTaskForRequest.getForgotLoginAsyncTask(edtMobile.getText().toString());
                } catch (JSONException e){
                    e.printStackTrace();
                }
                forgotApiCallAsyncTask.setApiCallAsyncTaskDelegate(new ForgotPasswordApiCallDelegate());
                forgotApiCallAsyncTask.execute(forgotApiCallAsyncTask);
            }*/

        }

    }

    private class EditorActionListener implements TextView.OnEditorActionListener {

        @Override
        public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
            if ((keyEvent != null && (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)){
                btnForgotPassword.performClick();
            }
            return false;
        }

    }

    private class ParentRelativeLayoutOnclickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            hideKeyboard(view);
        }

    }


    private class ForgotPasswordApiCallDelegate implements ApiCallAsyncTaskDelegate {

        @Override
        public void apiCallResult(String json, int statusCode) {
            ResponseParser responseParser = new ResponseParser(activity);
           /* ForgotPasswordResponseModel successResponseModel = responseParser.getForgotPasswordSuccessResponseModel(json, statusCode);
            if (successResponseModel == null){
                return;
            }
            if (successResponseModel != null && successResponseModel.getMessage() == null){
                return;
            }
            Toast.makeText(activity, successResponseModel.getMessage(), Toast.LENGTH_LONG).show();
//			finish();
            Bundle bundle = new Bundle();
            bundle.putString("mobile",edtMobile.getText().toString());
            bundle.putString(AppConstants.TRANSACTION_ID_KEY, successResponseModel.getForgotPasswordTransactionModel().getTransactionId());
            bundle.putString(AppConstants.USER_ID_KEY, successResponseModel.getForgotPasswordTransactionModel().getUserId());
            switchToActivity(activity, VerifyOtpAndChangePasswordActivity.class, bundle);*/
//			AlertDialogMessage alertDialogMessage=new AlertDialogMessage();
//			alertDialogMessage.showAlert(activity,successResponseModel.getMessages().getMessage(),false);
//			alertDialogMessage.setAlertDialogOkListener(new DialogOkButtonListener());
        }

        @Override
        public void onApiCancelled() {

        }

    }

    private class DialogOkButtonListener implements AlertDialogMessage.AlertDialogOkListener {
        @Override
        public void onAlertDialogOkButtonListener() {
            finish();
        }

    }


    private boolean validate() {
        String mobile = edtMobile.getText().toString();
        AlertDialogMessage alertDialogMessage = new AlertDialogMessage();
        if (mobile == null || mobile != null && mobile.isEmpty()){
            Toast.makeText(activity, getResources().getString(R.string.alert_enter_mobile), Toast.LENGTH_LONG).show();
            //alertDialogMessage.showAlert(activity, getResources().getString(R.string.alert_enter_mobile), true);
            return false;
        } else if (!validatePhoneNumber(mobile)){
            Toast.makeText(activity, getResources().getString(R.string.alert_invalid_mobile), Toast.LENGTH_LONG).show();
            //alertDialogMessage.showAlert(activity, getResources().getString(R.string.alert_invalid_mobile), true);
            return false;
        }
        return true;

    }

}
