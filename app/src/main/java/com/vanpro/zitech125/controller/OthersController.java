package com.vanpro.zitech125.controller;

import android.content.Context;

import com.vanpro.data.core.http.XrkDataListener;
import com.vanpro.data.core.http.request.BodyRequest;
import com.vanpro.data.core.http.request.HttpRequest;
import com.vanpro.data.core.http.response.HttpError;
import com.vanpro.data.core.http.response.HttpMethod;
import com.vanpro.zitech125.constants.ApiConstants;
import com.vanpro.zitech125.dao.UpgradeDto;
import com.vanpro.zitech125.location.ZLocation;
import com.vanpro.zitech125.util.AndroidUtils;
import com.vanpro.zitech125.util.LogUtil;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Jinsen on 16/9/20.
 */
public class OthersController extends BaseController {


    public static int checkUpdate(Context context, XrkDataListener listener){
        String url = ApiConstants.UPDATE_URL;
        int code = AndroidUtils.getAppVersionCode(context);

        HttpRequest httpRequest = new HttpRequest(url, HttpMethod.POST,getHeaders(),listener);
        httpRequest.setType(UpgradeDto.class);
        httpRequest.addParam("clientcode","0000");
        httpRequest.addParam("currentVersionCode",String.valueOf(code));
        httpRequest.addParam("packageName",context.getPackageName());
        return mXrkService.addService(httpRequest);

    }


//    /**
//     * 获取下一版更新信息
//     */
//    public static void getNextVersionInfo() {
//        HttpRequest request = new HttpRequest(ApiConstants.GET_UPGRADE_INFO_URL, HttpMethod.GET, getHeaders(), new TTBaseListener() {
//            @Override
//            public void success(Object data) {
//                NextVersionInfoDto dto = (NextVersionInfoDto) data;
//                NextVersionInfoEvent event = new NextVersionInfoEvent();
//                event.id = mTaskId;
//                if (isReqSucc(dto, event)) {
//                    event.data = dto.getBody();
//                }
//
//                EventBus.getDefault().post(event);
//            }
//
//            @Override
//            public void fail(HttpError error) {
//                NextVersionInfoEvent event = new NextVersionInfoEvent();
//                event.state = DataEvent.FAIL;
//                event.id = mTaskId;
//                EventBus.getDefault().post(event);
//            }
//        });
//        request.setType(NextVersionInfoDto.class);
//        mXrkService.addService(request);
//    }

    /**
     * 提交用户的位置信息
     * @param location
     */
    public static void uploadLocationInfo(ZLocation location){
        if(location == null)
            return;

        BodyRequest httpRequest = new BodyRequest(ApiConstants.UPLOAD_LOCATION_URL, HttpMethod.POST, getHeaders(), new XrkDataListener() {
            @Override
            public void setId(int id) {

            }

            @Override
            public void header(Map<String, String> headers) {

            }

            @Override
            public void success(Object data) {

            }

            @Override
            public void fail(HttpError error) {

            }
        });
//        httpRequest.setType(NULLDto.class);
//        Map<String,String> parampost = new HashMap<String,String>();
//        parampost.put("type", "1");// 接口类型1：提交位置信息 // 2为查询等
//        parampost.put("platform", "2");// 平台 1为 IOS ， 2 为安卓
//        parampost.put("latitude", String.valueOf(location.getLatitude()));// 位置
//        parampost.put("longitude", String.valueOf(location.getLongitude()));
//        parampost.put("udid", "888888888888888888888");//唯一的标志
//        httpRequest.setParams(parampost);// 业务数据
//        LogUtil.e(TAG,"latitude" + String.valueOf(location.getLatitude()) +"longitude"+ String.valueOf(location.getLongitude()));
//        mXrkService.addService(httpRequest);
//
//        RequestParams params = new RequestParams();
//        params.put("user", "");//用户信息 暂时不传
//
//        Map<String,Object> parampost = new HashMap<String,Object>();
//        parampost.put("type", "1");// 接口类型1：提交位置信息 // 2为查询等
//        parampost.put("platform", "2");// 平台 1为 IOS ， 2 为安卓
//        parampost.put("latitude", "23.33");// 位置
//        parampost.put("longitude", "113.88");
//        parampost.put("udid", "888888888888888888888");//唯一的标志
//
        JSONObject parampost = new JSONObject();
        JSONObject param = new JSONObject();
        try {
            parampost.put("type", "1");// 接口类型1：提交位置信息 // 2为查询等
            parampost.put("platform", "2");// 平台 1为 IOS ， 2 为安卓
            parampost.put("latitude", "23.33");// 位置
            parampost.put("longitude", "113.88");
            parampost.put("udid", "888888888888888888888");//唯一的标志
            parampost.put("clientcode","0000");
            parampost.put("currentVersionCode","1");
            parampost.put("packageName","com.xxx");
            param.put("parampost", parampost);// 业务数据'
            LogUtil.e(TAG,"xxxx " + param.toString());
        }catch (Exception e){

        }
//        httpRequest.addParam("parampost", parampost.toString());

        httpRequest.setBody(param);
        mXrkService.addService(httpRequest);

    }


}
