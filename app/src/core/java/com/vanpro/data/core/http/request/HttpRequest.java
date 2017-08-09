package com.vanpro.data.core.http.request;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.vanpro.data.core.http.XrkDataListener;
import com.vanpro.data.core.http.response.HttpMethod;
import com.vanpro.data.core.http.util.HttpUtils;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * HTTP请求
 * url, method
 * headerMap,  postParams
 */
public class HttpRequest extends XrkRequest {

    /**
     * 请求参数
     */
    private Map<String, String> mRequestParams = new HashMap<String, String>();

    public HttpRequest(String url, HttpMethod method, HashMap<String, String> headers, XrkDataListener listener) {
        super(url, method, headers, listener);
    }

    /**
     * HTTP BODY
     * 处理HTTP中的主体内容
     * 包括POST和PUT参数，以及一些主体内容
     *
     * @return body
     */
    @Override
    public byte[] getRequestBody() {
        if (getParams() != null && getParams().size() > 0 && !isUrlRequest()) {
            return HttpUtils.encodeParametersToBytes(getParams(), getParamsEncoding());
        }
        return null;
    }

    @Override
    protected Request buildRequest() {
        StringRequest request = new StringRequest(mMethod.getId(), getUrl(), mResponseListener, mErrorListener) {
            /**
             * HEAD 参数
             *
             * @return
             * @throws com.android.volley.AuthFailureError
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return mHeaders;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return getRequestBody();
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                //mListener 不为空
                //status = [200|201|304]
                if (mListener != null && (response.statusCode == 200 || response.statusCode == 201
                        || response.notModified)) {
                    mListener.header(response.headers);
                }

                return super.parseNetworkResponse(response);
            }

            @Override
            public String getBodyContentType() {
                return getContentType();
            }
        };

        request.setRetryPolicy(getRetryPolicy());
        request.setShouldCache(isShouldCache());
        request.setTag(TAG);
        return request;
    }

    /**
     * @return the params body request
     */
    public Map<String, String> getParams() {
        if (isUrlRequest()) {
            return Collections.emptyMap();
        }
        return mRequestParams;
    }

    public void setParams(Map<String, String> params) {
        this.mRequestParams = params;
    }

    /**
     * name=value
     * if array
     * name[0]=value
     * name[1]=value
     *
     * @param key
     * @param value
     */
    public void addParam(String key, String value) {
        this.mRequestParams.put(key, value);
    }

    /**
     * name[0]=value1
     * name[1]=value2
     *
     * @param key
     * @param values
     */
    public void addParam(String key, String... values) {
        for (int i = 0; i < values.length; i++) {
            this.mRequestParams.put(key + "[" + i + "]", values[i]);
        }
    }


    /**
     * @return the url with params
     */
    public String getUrl() {
        if (isUrlRequest()) {
            if (mUrl.contains("?")) {
                mUrl += "&";
            } else {
                mUrl += "?";
            }
            return mUrl += HttpUtils.encodeParameters(mRequestParams, getParamsEncoding());
        }

        return mUrl;
    }

    private boolean isUrlRequest() {
        return this.mRequestParams.size() > 0
                && (getMethod() == HttpMethod.GET || getMethod() == HttpMethod.HEAD || getMethod() == HttpMethod.DELETE);
    }

}
