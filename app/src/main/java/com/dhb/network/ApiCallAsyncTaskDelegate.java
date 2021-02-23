package com.dhb.network;

import org.json.JSONException;

public interface ApiCallAsyncTaskDelegate {
    public void apiCallResult(String json, int statusCode) throws JSONException;
    public void onApiCancelled();
}
