package com.vanpro.data.core.http.request;


import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.vanpro.data.core.http.XrkDataListener;
import com.vanpro.data.core.http.response.HttpMethod;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * 主体请求类
 */
public class BodyRequest extends XrkRequest {

    private byte[] mBytes = null;

    public BodyRequest(String url, HttpMethod method, HashMap<String, String> headers, XrkDataListener listener) {
        super(url, method,headers,listener);
    }

    @Override
    public String getContentType() {
        return "application/json; charset=" + getParamsEncoding();
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

    @Override
    public byte[] getRequestBody() {
        return mBytes;
    }

    public void setBody(byte[] bytes) {
        this.mBytes = bytes;
    }

    public void setBody(String bodyStr) {
        try {
            this.mBytes = bodyStr.getBytes(getParamsEncoding());
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + getParamsEncoding(), uee);
        }
    }

    public void setBody(JSONObject bodyJson) {
        try {
            this.mBytes = bodyJson.toString().getBytes(getParamsEncoding());
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + getParamsEncoding(), uee);
        }
    }

    public void setBody(JSONArray bodyJson) {
        try {
            this.mBytes = bodyJson.toString().getBytes(getParamsEncoding());
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + getParamsEncoding(), uee);
        }
    }
}
