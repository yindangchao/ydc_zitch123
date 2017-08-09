package com.vanpro.data.core.http;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.squareup.okhttp.OkHttpClient;
//import com.vanpro.data.core.http.request.OkHttpStack;
import com.vanpro.data.core.http.request.XrkRequest;

/**
 * 数据管理器
 *
 * Created by Jinsen on 2015/3/26.
 */
public class XrkService{
    private String TAG = XrkService.class.getName();
    /** Default on-disk cache directory. */
    private static final String DEFAULT_CACHE_DIR = "volley";
    private static XrkService _instance = null;

    private Context mContext = null;

    private RequestQueue mRequestQueue = null;

    private int mTaskId = 0x100;

    private XrkService(Context context){
        mContext = context;
        mRequestQueue = Volley.newRequestQueue(mContext);
//        mRequestQueue = Volley.newRequestQueue(mContext, new OkHttpStack(new OkHttpClient()));
    }

    public static XrkService createInstance(Context context){
        if(_instance == null){
            _instance = new XrkService(context);
        }
        return _instance;
    }

    public static XrkService getInstance(){
        return _instance;
    }

    /**
     * 添加获取数据请求
     * @param task
     */
    public int addService(final XrkRequest task){
        if(task == null)
            return -1;

        int taskId = ++mTaskId;
        task.setId(taskId);
        //通过volley 获取网络数据
        mRequestQueue.add(task.getRequest());
        return taskId;
    }


    /**
     * 取消任务
     * @param task
     */
    public  void cancelTask(XrkRequest task){
        if(task != null) task.cancel();
    }

    /**
     * 取消所有任务
     */
    public void cancelAllTask(){
        mRequestQueue.cancelAll(XrkRequest.TAG);
    }

    /**
     * 清除缓存
     */
    public void clearCache(){
        mRequestQueue.getCache().clear();
    }

    /**
     * 获取缓存路径
     */
    public String getCachePath(){
        return mContext.getCacheDir().getAbsolutePath() + DEFAULT_CACHE_DIR;
    }

}
