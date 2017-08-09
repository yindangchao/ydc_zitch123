package com.vanpro.data.core.http.request;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.vanpro.data.core.http.Global;
import com.vanpro.data.core.http.XrkDataListener;
import com.vanpro.data.core.http.response.HttpError;
import com.vanpro.data.core.http.response.HttpMethod;
import com.vanpro.data.core.http.util.GsonUtils;
import com.vanpro.data.core.http.util.LogUtil;
import com.vanpro.data.core.http.util.StringUtils;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Administrator on 2015/3/26.
 */
public abstract class XrkRequest {

    public static String TAG = "XrkRequest";

    /**
     * URL
     */
    protected String mUrl;

    /**
     * Method
     */
    protected HttpMethod mMethod;

    /**
     * 请求的头部
     */
    protected HashMap<String, String> mHeaders = new HashMap<String, String>();

    /**
     * 缓存有效时间
     * 默认有效时长为10分钟
     */
    protected int mCacheTime = 10 * 60 * 1000;

    /**
     * 超时时间
     */
    protected int mTimeout = Global.REQUEST_TIMEOUT_MS;

    /**
     * 最多重复次数
     */
    protected int mMaxRetries = Global.REQUEST_MAX_RETRIES;

    /**
     * 编码
     */
    protected String mParamsEncoding = Global.DEFAULT_PARAMS_ENCODING;


    protected String mContentType = Global.DEFAULT_CONTENTTYPE;


    private RetryPolicy mRetryPolicy = null;

    //从网络上获取到的数据是否需要缓存,默认不缓存
    private boolean mShouldCache = false;

    //是否是刷新数据,如果是刷新数据则不读取缓存数据
    private boolean mRefresh = false;

    protected XrkDataListener mListener = null;

    protected Request mRequest = null;

    protected Response.Listener<String> mResponseListener = null;
    protected Response.ErrorListener mErrorListener = null;

    private Class mClazz = null;

    private int mId = 0;


    public XrkRequest(String url, HttpMethod method, HashMap<String, String> headers, XrkDataListener listener) {
        this.mUrl = url;
        this.mMethod = method;
        this.mHeaders = headers;
        this.mListener = listener;
    }


    public void setParamsEncoding(String encoding) {
        this.mParamsEncoding = encoding;
    }

    public String getParamsEncoding() {
        return mParamsEncoding;
    }

    public void setRetryPolicy(RetryPolicy retryPolicy) {
        this.mRetryPolicy = retryPolicy;
    }

    public RetryPolicy getRetryPolicy() {
        if (mRetryPolicy == null)
            mRetryPolicy = new DefaultRetryPolicy(mTimeout, mMaxRetries, 2f);
        return mRetryPolicy;
    }

    public void setMaxRetries(int retries) {
        this.mMaxRetries = retries;
    }

    /**
     * 设置数据缓存有效时长
     *
     * @param cacheTime
     */
    public void setCacheTime(int cacheTime) {
        this.mCacheTime = cacheTime;
    }

    public int getCacheTime() {
        return mCacheTime;
    }

    public void setId(int id) {
        this.mId = id;
        mListener.setId(id);
    }

    /**
     * 获取任务id
     *
     * @return
     */
    public int getId() {
        return mId;
    }

    public void setTimeout(int timeout) {
        this.mTimeout = timeout;
    }

    public int getTimeout() {
        return mTimeout;
    }

    public void setType(Class clazz) {
        this.mClazz = clazz;
    }

    public Class getType() {
        return this.mClazz;
    }

    public void setContentType(String contentType) {
        if (StringUtils.isNotEmpty(contentType))
            this.mContentType = contentType;
    }

    public String getContentType() {
        return mContentType;
    }

    public HttpMethod getMethod() {
        return mMethod;
    }

    public String getUrl() {
        return mUrl;
    }

    public XrkDataListener getListener() {
        return mListener;
    }

    public Request getRequest() {
        if (mRequest == null) {
            mResponseListener = createResponse();
            mErrorListener = createErrorListener();
            mRequest = buildRequest();
        }
        return mRequest;
    }

    public void setShouldCache(boolean shouldCache) {
        this.mShouldCache = shouldCache;
    }

    public boolean isShouldCache() {
        return mShouldCache;
    }

    public void setRefresh(boolean refresh) {
        this.mRefresh = refresh;
    }

    public boolean isRefresh() {
        return mRefresh;
    }

    /**
     * 取消任务
     */
    public void cancel() {
        mRequest.cancel();

        destroy();
    }

    /**
     * 销毁任务
     */
    public void destroy() {
        mResponseListener = null;
        mErrorListener = null;
        mRequest = null;
    }

    protected abstract Request buildRequest();

    private Response.Listener<String> createResponse() {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if (StringUtils.isEmpty(response)) {
                        HttpError httpError = new HttpError(HttpError.Error.RESPONSE_NULL.getCode(),
                                HttpError.Error.RESPONSE_NULL.getMessage());
                        mListener.fail(httpError);
                        return;
                    }

                    LogUtil.d(TAG, "response success " + response);
//                    System.out.println("----------------" + response);
                    if (mClazz != null)
                        mListener.success(GsonUtils.fromJson(response, mClazz));
                    else
                        mListener.success(response);
                } finally {
                    destroy();
                }
            }
        };
        return responseListener;
    }

    /**
     * 创建错误监听器
     *
     * @return
     */
    private Response.ErrorListener createErrorListener() {
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                HttpError httpError = makeError(error);

                if (error.networkResponse != null && error.networkResponse.data != null) {
                    // 错误内容
                    int statusCode = error.networkResponse.statusCode;
                    String response = new String(error.networkResponse.data);
                    String message = null;
                    LogUtil.d("xxxxxxxxxxxxxx", response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        message = jsonObject.optString("message");
                    } catch (Exception e) {
                    }
                    // 转换错误信息
                    if (StringUtils.isNotEmpty(message)) {
                        httpError = new HttpError(statusCode, message);
                    } else {
                        httpError = new HttpError(statusCode, response);
                    }
                }
                if (error.getCause() instanceof IOException
                        && error.getMessage().indexOf("No authentication challenges found") > -1) {
                    httpError.setCode(401);
                    httpError.setMessage(error.getMessage());
                }

                try {
                    mListener.fail(httpError);
                } finally {
                    destroy();
                }
            }
        };
        return errorListener;
    }

    /**
     * 产生一个错误信息
     *
     * @param error
     * @return
     */
    private HttpError makeError(VolleyError error) {
        HttpError httpError = new HttpError();
        //请求超时
        if (error instanceof TimeoutError) {
            httpError.setCode(HttpError.Error.TIMEOUT_ERROR.getCode());
            httpError.setMessage(HttpError.Error.TIMEOUT_ERROR.getMessage());
        }
        //无法链接
        else if (error instanceof NoConnectionError) {
            httpError.setCode(HttpError.Error.NO_CONNECTION_ERROR.getCode());
            httpError.setMessage(HttpError.Error.NO_CONNECTION_ERROR.getMessage());
        }
        //身份验证失败
        else if (error instanceof AuthFailureError) {
            httpError.setCode(HttpError.Error.AUTH_FAILURE_ERROR.getCode());
            httpError.setMessage(HttpError.Error.AUTH_FAILURE_ERROR.getMessage());
        }
        //服务器错误
        else if (error instanceof ServerError) {
            httpError.setCode(HttpError.Error.SERVER_ERROR.getCode());
            httpError.setMessage(HttpError.Error.SERVER_ERROR.getMessage());
        }
        //网络错误
        else if (error instanceof NetworkError) {
            httpError.setCode(HttpError.Error.NETWORK_ERROR.getCode());
            httpError.setMessage(HttpError.Error.NETWORK_ERROR.getMessage());
        }
        //其他类型
        else {
            httpError.setCode(HttpError.Error.DEFUALT_ERROR.getCode());
            httpError.setMessage(HttpError.Error.DEFUALT_ERROR.getMessage());
        }
        return httpError;
    }

    /**
     * HTTP BODY
     * 处理HTTP中的主体内容
     *
     * @return body
     */
    public abstract byte[] getRequestBody();

}