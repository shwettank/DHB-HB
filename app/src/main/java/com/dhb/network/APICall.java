package com.dhb.network;

import com.dhb.request_model.MessageModel;
import com.dhb.utils.AppConstants;
import com.dhb.utils.CommonUtils;
import com.dhb.utils.Logger;
import com.google.android.gms.ads.internal.gmsg.HttpClient;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class APICall implements AppConstants {

    private int timeoutConnection = 10 * 1000;

    private int timeoutSocket = 60 * 1000;

    private CommonUtils commonUtils;

    int statusCode;

    /**
     * Default Constructor initializing CommonUtils singelton object
     */
    public APICall() {

        commonUtils = CommonUtils.getInstance();

    }

    /**
     * Get request api call
     */
   /* public String jsonFromUrlGetRequest(AbstractApiModel requestModel) {

        String responseJson = "";
        StringEntity entity;

        InputStream getResponseInputStream = null;
        Logger.debug(requestModel.getRequestUrl());

        HttpGet httpGet = new HttpGet(requestModel.getRequestUrl());

//        Logger.debug(requestModel.getRequestUrl());

                HttpClient httpClient = new DefaultHttpClient(
                getHttparamsForConnection());

        String userAgent = "NewUseAgent/1.0";

        httpClient.getParams().setParameter(CoreProtocolPNames.USER_AGENT,
                userAgent);

        if (requestModel.getRequestUrl().startsWith("https"))
            httpClient = sslClient(httpClient);

        // For supporting Domain names using emulators
        httpGet.getParams().setParameter(
                CoreProtocolPNames.USE_EXPECT_CONTINUE, Boolean.FALSE);

        HttpResponse httpResponse;

        try {

            if (requestModel.getHeader() != null){


                for (int i = 0; i < requestModel.getHeader().size(); i++){

                    httpGet.addHeader(requestModel.getHeader().get(i)
                            .getHeaderKey(), requestModel.getHeader().get(i)
                            .getHeaderValue());

                    Logger.debug(i + " : header value pair is :"
                            + requestModel.getHeader().get(i).getHeaderKey()
                            + " and "
                            + requestModel.getHeader().get(i).getHeaderValue());

                }
            }


            httpResponse = httpClient.execute(httpGet);


            statusCode = httpResponse.getStatusLine().getStatusCode();

            Logger.debug("statusCode : " + statusCode);

            HttpEntity httpEntity = httpResponse.getEntity();

            getResponseInputStream = httpEntity.getContent();

            // Logger.debug(EntityUtils.toString(httpEntity));

        } catch (ConnectTimeoutException e){

            statusCode=400;

            return commonUtils.getErrorJson(MSG_INTERNET_CONNECTION_SLOW);

        } catch (HttpHostConnectException e){

            statusCode=400;
            return commonUtils.getErrorJson(MSG_COMMUNICATION_PROBLEM);

        } catch (SocketTimeoutException e){

            statusCode=400;
            return commonUtils.getErrorJson(MSG_COMMUNICATION_PROBLEM);

        } catch (ClientProtocolException e){

            statusCode=400;
            return commonUtils.getErrorJson(MSG_NETWORK_ERROR);

        } catch (IOException e){

            statusCode=400;
            return commonUtils.getErrorJson(MSG_NETWORK_ERROR);

        } catch (Exception e){

            statusCode=400;
            Logger.debug("Exception  " + e.toString());

            return commonUtils.getErrorJson(MSG_UNKNOW_ERROR);

        }

        if (getResponseInputStream != null){

            responseJson = getJsonStringFromInputstream(getResponseInputStream);

            Logger.debug("json getResponseInputStream : " + responseJson);

        }

        if (statusCode != 200){

            String validResponseJson = isValidResponse(responseJson);

            if (validResponseJson != null
                    && !validResponseJson.equals(responseJson)){

                return validResponseJson;

            }

        }

        return responseJson;

    }*/

    /**
     * Put request api call
     */
    /*public String jsonFromUrlPutRequest(AbstractApiModel requestModel) {

        String responseJson = "";

        InputStream getResponseInputStream = null;

        HttpPut httpput = new HttpPut(requestModel.getRequestUrl());
        Logger.debug(requestModel.getRequestUrl());


        HttpClient httpClient = new DefaultHttpClient(
                getHttparamsForConnection());

        if (requestModel.getRequestUrl().startsWith("https"))
            httpClient = sslClient(httpClient);

        Logger.debug(requestModel.getRequestUrl());

        // For supporting Domain names using emulators
        httpput.getParams().setParameter(
                CoreProtocolPNames.USE_EXPECT_CONTINUE, Boolean.FALSE);

        HttpResponse httpResponse;

        StringEntity entity;

        try {

            if (requestModel.getHeader() != null){

                for (int i = 0; i < requestModel.getHeader().size(); i++){

                    httpput.addHeader(requestModel.getHeader().get(i)
                            .getHeaderKey(), requestModel.getHeader().get(i)
                            .getHeaderValue());

                    Logger.debug(i + " : header value pair is :"
                            + requestModel.getHeader().get(i).getHeaderKey()
                            + " and "
                            + requestModel.getHeader().get(i).getHeaderValue());

                }
            }

            Logger.debug("Put data:-" + requestModel.getPostData());

            entity = new StringEntity(requestModel.getPostData(),
                    HTTP.UTF_8);

            entity.setContentType("application/json");

            httpput.setEntity(entity);

            httpResponse = httpClient.execute(httpput);

            statusCode = httpResponse.getStatusLine().getStatusCode();

            Logger.debug("statusCode : " + statusCode);

            HttpEntity httpEntity = httpResponse.getEntity();

            getResponseInputStream = httpEntity.getContent();

        } catch (ConnectTimeoutException e){
            statusCode=400;
            return commonUtils.getErrorJson(MSG_INTERNET_CONNECTION_SLOW);

        } catch (HttpHostConnectException e){
            statusCode=400;
            return commonUtils.getErrorJson(MSG_COMMUNICATION_PROBLEM);

        } catch (SocketTimeoutException e){
            statusCode=400;
            return commonUtils.getErrorJson(MSG_COMMUNICATION_PROBLEM);

        } catch (ClientProtocolException e){
            statusCode=400;
            return commonUtils.getErrorJson(MSG_NETWORK_ERROR);

        } catch (IOException e){
            statusCode=400;
            return commonUtils.getErrorJson(MSG_NETWORK_ERROR);

        } catch (Exception e){
            statusCode=400;
            Logger.debug("Exception  " + e.toString());

            return commonUtils.getErrorJson(MSG_UNKNOW_ERROR);

        }

        if (getResponseInputStream != null){

            responseJson = getJsonStringFromInputstream(getResponseInputStream);

            Logger.debug("json getResponseInputStream : " + responseJson);

        }

        if (statusCode != 200){

            String validResponseJson = isValidResponse(responseJson);

            if (validResponseJson != null
                    && !validResponseJson.equals(responseJson)){

                return validResponseJson;

            }

        }

        return responseJson;

    }*/

    /**
     * Post request api call
     */
    /*public String jsonFromUrlPostRequest(AbstractApiModel requestModel) {

        String responseJson = "";

        InputStream postResponseInputStream = null;

        StringEntity entity;

        HttpPost httpPost = new HttpPost(requestModel.getRequestUrl());
        Logger.debug(requestModel.getRequestUrl());

        HttpClient httpClient = new DefaultHttpClient(
                getHttparamsForConnection());

        if (requestModel.getRequestUrl().startsWith("https"))
            httpClient = sslClient(httpClient);

        Logger.debug(requestModel.getRequestUrl());

        // For supporting Domain names using emulators
        httpPost.getParams().setParameter(
                CoreProtocolPNames.USE_EXPECT_CONTINUE, Boolean.FALSE);

        HttpResponse httpResponse;

        try {

            if (requestModel.getHeader() != null){

                for (int i = 0; i < requestModel.getHeader().size(); i++){

                    httpPost.addHeader(requestModel.getHeader().get(i)
                            .getHeaderKey(), requestModel.getHeader().get(i)
                            .getHeaderValue());

                    Logger.debug(i + " : header value pair is :"
                            + requestModel.getHeader().get(i).getHeaderKey()
                            + " and "
                            + requestModel.getHeader().get(i).getHeaderValue());

                }
            }
            Logger.debug("Post data:-" + requestModel.getPostData());
            entity = new StringEntity(requestModel.getPostData(),
                    HTTP.UTF_8);

            entity.setContentType("application/json");

            httpPost.setEntity(entity);

            httpResponse = httpClient.execute(httpPost);

            statusCode = httpResponse.getStatusLine().getStatusCode();

            Logger.debug("statusCode : " + statusCode);

            HttpEntity httpEntity = httpResponse.getEntity();

            postResponseInputStream = httpEntity.getContent();
            Logger.debug("Response-" + postResponseInputStream);

        } catch (ConnectTimeoutException e){
            statusCode=400;
            return commonUtils.getErrorJson(MSG_INTERNET_CONNECTION_SLOW);

        } catch (HttpHostConnectException e){
            statusCode=400;
            return commonUtils.getErrorJson(MSG_COMMUNICATION_PROBLEM);

        } catch (SocketTimeoutException e){
            statusCode=400;
            return commonUtils.getErrorJson(MSG_COMMUNICATION_PROBLEM);

        } catch (ClientProtocolException e){
            statusCode=400;
            return commonUtils.getErrorJson(MSG_NETWORK_ERROR);

        } catch (IOException e){
            statusCode=400;
            return commonUtils.getErrorJson(MSG_NETWORK_ERROR);

        } catch (Exception e){
            statusCode=400;
            Logger.debug("Exception  " + e.toString());

            return commonUtils.getErrorJson(MSG_UNKNOW_ERROR);

        }

        if (postResponseInputStream != null){

            responseJson = getJsonStringFromInputstream(postResponseInputStream);

            Logger.debug("json postResponseInputStream : " + responseJson);

        }

        if (statusCode != 200){

            String validResponseJson = isValidResponse(responseJson);

            if (validResponseJson != null
                    && !validResponseJson.equals(responseJson)){

                return validResponseJson;

            }

        }

        return responseJson;

    }*/

    /**
     * Delete request api call
     */
   /* public String jsonFromUrlDeleteRequest(AbstractApiModel requestModel) {

        String responseJson = "";

        StringEntity entity;

        InputStream postResponseInputStream = null;

        HttpDelete httpDelete = new HttpDelete(requestModel.getRequestUrl());
        Logger.debug(requestModel.getRequestUrl());
        HttpClient httpClient = new DefaultHttpClient(
                getHttparamsForConnection());

        if (requestModel.getRequestUrl().startsWith("https"))
            httpClient = sslClient(httpClient);

        // For supporting Domain names using emulators
        httpDelete.getParams().setParameter(
                CoreProtocolPNames.USE_EXPECT_CONTINUE, Boolean.FALSE);

        HttpResponse httpResponse;

        try {

            if (requestModel.getHeader() != null){

                for (int i = 0; i < requestModel.getHeader().size(); i++){

                    httpDelete.addHeader(requestModel.getHeader().get(i)
                            .getHeaderKey(), requestModel.getHeader().get(i)
                            .getHeaderValue());

                    Logger.debug(i + " : header value pair is :"
                            + requestModel.getHeader().get(i).getHeaderKey()
                            + " and "
                            + requestModel.getHeader().get(i).getHeaderValue());

                }
            }
            Logger.debug("Post data:-" + requestModel.getPostData());
            entity = new StringEntity(requestModel.getPostData(),
                    HTTP.UTF_8);

            entity.setContentType("application/json");


            httpResponse = httpClient.execute(httpDelete);

            statusCode = httpResponse.getStatusLine().getStatusCode();

            Logger.debug("statusCode : " + statusCode);

            HttpEntity httpEntity = httpResponse.getEntity();

            postResponseInputStream = httpEntity.getContent();

        } catch (ConnectTimeoutException e){

            statusCode=400;
            return commonUtils.getErrorJson(MSG_INTERNET_CONNECTION_SLOW);

        } catch (HttpHostConnectException e){

            statusCode=400;
            return commonUtils.getErrorJson(MSG_COMMUNICATION_PROBLEM);

        } catch (SocketTimeoutException e){

            statusCode=400;
            return commonUtils.getErrorJson(MSG_COMMUNICATION_PROBLEM);

        } catch (ClientProtocolException e){

            statusCode=400;
            return commonUtils.getErrorJson(MSG_NETWORK_ERROR);

        } catch (IOException e){

            statusCode=400;
            return commonUtils.getErrorJson(MSG_NETWORK_ERROR);

        } catch (Exception e){

            Logger.debug("Exception  " + e.toString());

            statusCode=400;
            return commonUtils.getErrorJson(MSG_UNKNOW_ERROR);

        }

        if (postResponseInputStream != null){

            responseJson = getJsonStringFromInputstream(postResponseInputStream);

            // Logger.debug("json postResponseInputStream : " + responseJson);

        }

        if (statusCode != 200){

            String validResponseJson = isValidResponse(responseJson);

            if (validResponseJson != null
                    && !validResponseJson.equals(responseJson)){

                return validResponseJson;

            }

        }
        return responseJson;

    }*/

    /**
     * Reading json from Input stream response
     */
    private String getJsonStringFromInputstream(InputStream inputStream) {

        String json = "";

        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    //  inputStream, "iso-8859-1"), 8);
                    inputStream, "UTF-8"), 8);


            StringBuilder sb = new StringBuilder();

            String line = null;

            while ((line = reader.readLine()) != null){

                sb.append(line + "\n");

            }

            inputStream.close();

            json = String.valueOf(sb);

        } catch (Exception e){

            Logger.debug("Buffer Error Error converting result " + e.toString());

        }

        return json;

    }

    /**
     * Connection Timeout and socket timeout http parameters
     */
    private HttpParams getHttparamsForConnection()

    {

        HttpParams httpParameters = new BasicHttpParams();

        HttpConnectionParams.setConnectionTimeout(httpParameters,
                timeoutConnection);

        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

        return httpParameters;

    }

    /*private HttpClient sslClient(HttpClient client) {

        try {

            X509TrustManager tm = new X509TrustManager() {

                public void checkClientTrusted(X509Certificate[]	xcs,
                                               String			string) throws CertificateException {

                }

                public void checkServerTrusted(X509Certificate[]	xcs,
                                               String			string) throws CertificateException {

                }

                public X509Certificate[] getAcceptedIssuers() {

                    return null;

                }

            };

            SSLContext ctx = SSLContext.getInstance("TLS");

            ctx.init(null, new TrustManager[] {tm}, null);

            SSLSocketFactory ssf = new MySSLSocketFactory(ctx);

            ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            ClientConnectionManager ccm = client.getConnectionManager();

            SchemeRegistry sr = ccm.getSchemeRegistry();

            sr.register(new Scheme("https", ssf, 443));

            return new DefaultHttpClient(ccm, client.getParams());

        } catch (Exception ex){

            return null;

        }
    }*/

    public class MySSLSocketFactory extends SSLSocketFactory {

        SSLContext sslContext = SSLContext.getInstance("TLS");

        public MySSLSocketFactory(KeyStore truststore)
                throws NoSuchAlgorithmException, KeyManagementException,
                KeyStoreException, UnrecoverableKeyException {

            super(truststore);

            TrustManager tm = new X509TrustManager() {

                public void checkClientTrusted(X509Certificate[]	chain,
                                               String			authType) throws CertificateException {

                }

                public void checkServerTrusted(X509Certificate[]	chain,
                                               String			authType) throws CertificateException {

                }

                public X509Certificate[] getAcceptedIssuers() {

                    return null;

                }

            };

            sslContext.init(null, new TrustManager[] {tm}, null);

        }

        public MySSLSocketFactory(SSLContext context)
                throws KeyManagementException, NoSuchAlgorithmException,
                KeyStoreException, UnrecoverableKeyException {

            super(null);

            sslContext = context;

        }

        @Override
        public Socket createSocket(Socket socket, String host, int port,
                                   boolean autoClose) throws IOException, UnknownHostException {

            return sslContext.getSocketFactory().createSocket(socket, host,
                    port, autoClose);

        }

        @Override
        public Socket createSocket() throws IOException {

            return sslContext.getSocketFactory().createSocket();

        }

    }

    private String isValidResponse(String responseJson) {

        try {

            MessageModel messageModel = new Gson().fromJson(responseJson,
                    MessageModel.class);

            if (messageModel != null && messageModel.getType() != null
                    && messageModel.getType().equals("error")
                    || messageModel.getType().equals(AppConstants.ERROR_MESSAGE)){

                messageModel.setStatusCode(statusCode);

                return new Gson().toJson(messageModel);

            }

        } catch (Exception e){

            return null;

        }

        return responseJson;
    }

    public int getStatusCode() {
        return statusCode;
    }

}
