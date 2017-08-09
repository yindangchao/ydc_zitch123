//package com.vanpro.data.core.http.request;
//
//import com.android.volley.AuthFailureError;
//import com.android.volley.NetworkResponse;
//import com.android.volley.Request;
//import com.android.volley.Response;
//import com.android.volley.toolbox.StringRequest;
//import com.vanpro.data.core.http.XrkDataListener;
//import com.vanpro.data.core.http.response.HttpMethod;
//import com.vanpro.data.core.http.util.LogUtil;
//
//import org.apache.http.entity.mime.MultipartEntity;
//import org.apache.http.entity.mime.content.FileBody;
//import org.apache.http.entity.mime.content.StringBody;
//
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.nio.charset.Charset;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * 上传文件
// * <p/>
// * Created by Jinsen on 16/1/18.
// */
//public class UploadFileRequest extends XrkRequest {
//
//    private MultipartEntity entity = new MultipartEntity();
//
//
//    public UploadFileRequest(String url, HttpMethod method, HashMap<String, String> headers, XrkDataListener listener) {
//        super(url, method, headers, listener);
//    }
//
//
//    /**
//     * 添加参数
//     *
//     * @param key
//     * @param value
//     * @throws UnsupportedEncodingException
//     */
//    public void addStringParam(String key, String value) throws UnsupportedEncodingException {
//        entity.addPart(key, new StringBody(value, Charset.forName("utf-8")));
//    }
//
//    /**
//     * 添加文件
//     *
//     * @param key
//     * @param file
//     */
//    public void addFileParam(String key, File file) {
//        entity.addPart(key, new FileBody(file));
//    }
//
//    @Override
//    protected Request buildRequest() {
//
//        StringRequest request = new StringRequest(mMethod.getId(), getUrl(), mResponseListener, mErrorListener) {
//            /**
//             * HEAD 参数
//             * @return
//             * @throws com.android.volley.AuthFailureError
//             */
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                return mHeaders;
//            }
//
//            @Override
//            public byte[] getBody() throws AuthFailureError {
//                return getRequestBody();
//            }
//
//            @Override
//            protected Response<String> parseNetworkResponse(NetworkResponse response) {
//                //mListener 不为空
//                //status = [200|201|304]
//                if (mListener != null && (response.statusCode == 200 || response.statusCode == 201
//                        || response.notModified)) {
//                    mListener.header(response.headers);
//                }
//
//                return super.parseNetworkResponse(response);
//            }
//
//            @Override
//            public String getBodyContentType() {
//                return getContentType();
//            }
//        };
//
//        request.setRetryPolicy(getRetryPolicy());
//        request.setShouldCache(isShouldCache());
//        request.setTag(TAG);
//        return request;
//    }
//
//    @Override
//    public String getContentType() {
//        return "";//entity.getContentType().getValue();
//    }
//
//    @Override
//    public byte[] getRequestBody() {
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        try {
//            entity.writeTo(bos);
//        } catch (IOException e) {
//            LogUtil.d("UploadFileRequest", "IOException writing to ByteArrayOutputStream");
//        }
//        return bos.toByteArray();
//    }
//}
