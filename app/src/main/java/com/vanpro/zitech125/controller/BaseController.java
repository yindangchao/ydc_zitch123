package com.vanpro.zitech125.controller;

import android.content.Context;

//import com.vanpro.zitech125.AccountManager;
//import ApiConstants;
import com.vanpro.zitech125.dao.BaseDTO;
import com.vanpro.zitech125.dao.StatusEntity;
import com.vanpro.zitech125.event.DataEvent;
import com.vanpro.data.core.http.XrkService;
import com.vanpro.data.core.http.response.HttpError;

import java.util.HashMap;

import de.greenrobot.event.EventBus;

/**
 * Created by Jinsen on 16/6/7.
 */
public class BaseController {

    protected static final String TAG = BaseController.class.getSimpleName();

    protected static XrkService mXrkService = null;

    protected static HashMap<String, String> mHeaders = new HashMap<String, String>();

    public static void addHeader(String key, String value) {
        mHeaders.remove(key);
        mHeaders.put(key, value);
    }

    public static HashMap<String, String> getHeaders() {
        return mHeaders;
    }


    protected static void addTokenInHead(String token){
//        addHeader(ApiConstants.LOGIN_TOKEN_NAME, token);
    }

    public static void logout(){
//        addHeader(ApiConstants.LOGIN_TOKEN_NAME, "");
    }

    /**
     * 初始化
     *
     * @param context
     */
    public static void init(Context context) {
        mXrkService = XrkService.createInstance(context);
//        addTokenInHead(AccountManager.getInstance().getAccount().token);
    }


    public static boolean isReqSucc(BaseDTO dto, DataEvent event){
        if(dto != null && dto.getHead() != null && StatusEntity.SUCC.equals(dto.getHead().getStatus())){
            event.state = DataEvent.SUCC;
            return true;
        }else{
            event.state = DataEvent.FAIL;
            if(dto != null && dto.getHead() != null){
                event.msg = dto.getHead().getError_msg();
                event.error = dto.getHead().getError_code();
            }
        }
        return false;
    }

    protected static String getPageUrl(String url, int page){
        if(url.contains("?"))
            return url + "&page="+page;
        return url + "?page="+page;
    }

    protected static void sendFailEvent(DataEvent event, HttpError error, int id){
        event.id = id;
        event.msg = error != null ? error.getMessage() : null;
        event.state = DataEvent.FAIL;
        EventBus.getDefault().post(event);
    }

}
