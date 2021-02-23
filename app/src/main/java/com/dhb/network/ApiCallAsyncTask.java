package com.dhb.network;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;
import android.os.Process;


import com.dhb.request_model.MessageModel;
import com.dhb.utils.AppConstants;
import com.dhb.utils.CommonUtils;
import com.dhb.utils.NetworkUtils;
import com.google.gson.JsonSyntaxException;


public class ApiCallAsyncTask extends AsyncTask<Void, Void, String> implements
        AppConstants, OnDismissListener {

    private AbstractApiModel requestModel;

    private ProgressDialog progressDialog;

    private Activity activity;

    private String jsonPostResponse;

    private ApiCallAsyncTaskDelegate delegate;

    private String progressBarTitle;

    private String progressBarMsg;

    private boolean isProgressBarCancellable;

    private boolean isProgressBarVisible = true;

    private int httpMethod;

    private ProgressBarDismissListener progressBarDismissListener;

    private boolean restrictProgressBarDissmiss;

    private Context context;

    private int statusCode;

    public void setContext(Context context) {
        this.context = context;
    }

    protected static final String X_API_VERSION = "x-api-version";
    private CommonUtils commonUtils;

    /**
     * Default constructor initializing values
     */
    public ApiCallAsyncTask(Activity activity) {

        this.activity = activity;

        this.context = (Context) activity;

        this.progressBarTitle = "";

        this.progressBarMsg = "Loading...";

        this.isProgressBarCancellable = false;

        commonUtils = CommonUtils.getInstance();

    }

    public ApiCallAsyncTask(Context context) {

        this.context = context;

        this.progressBarTitle = "";

        this.progressBarMsg = "Loading...";

        this.isProgressBarCancellable = false;

        commonUtils = CommonUtils.getInstance();

    }

    /**
     * Sets the progress bar visible
     */
    public void setProgressBarVisible(boolean isProgressBarVisible) {

        this.isProgressBarVisible = isProgressBarVisible;

    }

    /**
     * Sets the progress bar title
     */
    public void setProgressBarTitle(String progressBarTitle) {

        this.progressBarTitle = progressBarTitle;

    }

    /**
     * Sets progress bar message if not set ; default will be set to
     * "Loading..."
     */
    public void setProgressBarMessage(String progressBarMessage) {

        this.progressBarMsg = progressBarMessage;

    }

    /**
     * Sets whether this dialog is cancelable with the BACK key ; default will
     * be set to false
     */
    public void setProgressBarCancellable(boolean isCancellable) {

        this.isProgressBarCancellable = isCancellable;

    }

    /**
     * Sets the http method to be used for api call
     */
    public void setHttpMethod(int httpMethod) {

        this.httpMethod = httpMethod;

    }

    /**
     * Sets the api model for this AsyncTask
     */
    public void setApiModel(AbstractApiModel requestModel) {

        this.requestModel = requestModel;

    }

    /**
     * Sets the asyncTask delegate; callback method is apiCallResult(String
     * json)
     */
    public void setApiCallAsyncTaskDelegate(ApiCallAsyncTaskDelegate delegate) {

        this.delegate = delegate;

    }

    public void setActivity(Activity activity) {

        this.activity = activity;

    }

    /**
     * Sets the progress dialog dismiss listener
     */
    public void setOnProgressBarDismissListener(ProgressBarDismissListener progressBarDismissListener) {

        this.progressBarDismissListener = progressBarDismissListener;

    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();

        if (activity != null && !activity.isFinishing()){

            progressDialog = new ProgressDialog(activity);

            progressDialog.setTitle(progressBarTitle);

            progressDialog.setOnDismissListener(this);

            progressDialog.setMessage(progressBarMsg);

            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            progressDialog.setCancelable(isProgressBarCancellable);

            progressDialog.setCanceledOnTouchOutside(false);

            if (isProgressBarVisible && !activity.isFinishing()){

                try {
                    progressDialog.show();
                } catch (Exception e){
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }

        }

    }

    @Override
    protected String doInBackground(Void ... params) {

        Process.setThreadPriority(Process.THREAD_PRIORITY_MORE_FAVORABLE);

        /** Initializing APICall object to call api */
        APICall apiCall = new APICall();
        jsonPostResponse="";

        /** If network not availbalbe then return Network error json */
        if (!NetworkUtils.isNetworkAvailable(context)){
            jsonPostResponse=commonUtils.getErrorJson(
                    "Please check your network connection.");
            statusCode=400;
            return jsonPostResponse;

        } else {

            /**
             * httpMethod value sent from activity which decide which http
             * method to be called
             */
            try {

                if (httpMethod == GET_METHOD){

                    jsonPostResponse = apiCall
                            .jsonFromUrlGetRequest(requestModel);

                } else if (httpMethod == POST_METHOD){

                    jsonPostResponse = apiCall
                            .jsonFromUrlPostRequest(requestModel);

                } else if (httpMethod == PUT_METHOD){

                    jsonPostResponse = apiCall
                            .jsonFromUrlPutRequest(requestModel);

                } else if (httpMethod == DELETE_METHOD){

                    jsonPostResponse = apiCall
                            .jsonFromUrlDeleteRequest(requestModel);

                }
                statusCode=apiCall.getStatusCode();

                return jsonPostResponse;

            } catch (JsonSyntaxException net){

                net.printStackTrace();

                return jsonPostResponse;

            }

        }

    }

    @Override
    protected void onPostExecute(String jsonPostResponse) {

        restrictProgressBarDissmiss = true;

        MessageModel messageModel = null;


        /** Return result to activity using delegate */

        try {

            if (progressDialog != null && progressDialog.isShowing()){
                if (!activity.isFinishing()){
                    progressDialog.dismiss();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        if (isCancelled()){

            return;

        }

        try {

            if (delegate != null && activity != null && !activity.isFinishing()){

                delegate.apiCallResult(jsonPostResponse,statusCode);

            } else if (delegate != null && context != null){

                delegate.apiCallResult(jsonPostResponse,statusCode);

            }

        } catch (JsonSyntaxException jsonSyntaxException){

            jsonSyntaxException.printStackTrace();

        } catch (IllegalArgumentException illegarArgumentException){

            illegarArgumentException.printStackTrace();

        } catch (WindowManager.BadTokenException badTokenException){

            badTokenException.printStackTrace();

        } catch (IllegalStateException e){
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    protected void onCancelled() {

        if (progressDialog != null){

            progressDialog.dismiss();

        }

        super.onCancelled();

    }

    public interface ProgressBarDismissListener {

        public void onProgressBarDismiss();

    }

    @Override
    public void onDismiss(DialogInterface dialog) {

        if (progressBarDismissListener != null && !restrictProgressBarDissmiss){

            if (jsonPostResponse != null){

                progressBarDismissListener.onProgressBarDismiss();

            }

        }

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void execute(AsyncTask<Void, Void, String> as) {

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.HONEYCOMB_MR1){
            as.execute();
        } else {
            as.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }
}
