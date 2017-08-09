package com.vanpro.zitech125.service;

import android.content.Context;
import android.os.Handler;
import android.view.View;

import com.vanpro.zitech125.R;
import com.vanpro.zitech125.event.VerifyStoragePermissionEvent;
import com.vanpro.zitech125.manage.NotificationMgr;
import com.vanpro.zitech125.util.Config;
import com.vanpro.zitech125.controller.OthersController;
import com.vanpro.zitech125.controller.TTBaseListener;
import com.vanpro.zitech125.dao.StatusEntity;
import com.vanpro.zitech125.dao.UpgradeDto;
import com.vanpro.zitech125.entity.UpgradeEntity;
import com.vanpro.zitech125.ui.dialog.CommAlertDialog;
import com.vanpro.zitech125.ui.dialog.UpgradeDialog;
import com.vanpro.zitech125.util.AndroidUtils;
import com.vanpro.zitech125.util.AppDataManager;
import com.vanpro.zitech125.util.FileUtils;
import com.vanpro.zitech125.util.NetworkUtils;
import com.vanpro.zitech125.util.StringUtil;
import com.vanpro.zitech125.util.UIHelper;
import com.vanpro.data.core.http.response.HttpError;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import de.greenrobot.event.EventBus;

/**
 *
 * app 升级
 *
 * Created by Jinsen on 16/9/20.
 */
public class UpgradeManager {


    private Context mContext;

    Handler handler = new Handler();

    public UpgradeManager(Context context){
        this.mContext = context;
    }

    public void checkUpdate(UpdateListener listener){
        updatess(listener);
    }

    public void update(){
        updatess(null);
    }

    private void updatess(final UpdateListener listener){
        OthersController.checkUpdate(mContext, new TTBaseListener(){
            @Override
            public void success(Object data) {
                UpgradeDto dto = (UpgradeDto) data;
                if(dto != null && dto.getHead() != null
                        && StatusEntity.SUCC.equals(dto.getHead().getStatus())){

                    if(listener != null)
                        listener.resule(dto.getBody());
                    else{
                        a(dto.getBody());
                    }

                }else{
                    if(listener != null)
                        listener.fail();
                    else{

                    }
                }
            }

            @Override
            public void fail(HttpError error) {
                if(listener != null)
                    listener.fail();
                else{

                }
            }
        });
    }

    private void a(UpgradeEntity data){
        if(data == null || StringUtil.isEmpty(data.getDownloadUrl()))
            return;

        if(data.getVersionCode() <= AndroidUtils.getAppVersionCode(mContext))
            return;

        if(ignoreTheVersion(data))
            return;

        if(hasDownload(data)){
            showUpgradeInfo(data);
        }else {
            if (isWifi()) {
                downloadApk(data);
            } else {
                showUpgradeInfo(data);
            }
        }
    }

    //是否忽略当前版本的更新
    private boolean ignoreTheVersion(UpgradeEntity data){
        String value = data.getAppName()+data.getReleaseDate()+data.getFilesize()+data.getVersionCode();
        return value.equals(AppDataManager.getInstance().getString(AppDataManager.KEY.IGNORE_UPDATE_INFO_KEY));
    }

    //设置忽略当前版本更新
    private void ignoreUpgrade(UpgradeEntity data){
        String value = data.getAppName()+data.getReleaseDate()+data.getFilesize()+data.getVersionCode();
        AppDataManager.getInstance().setData(AppDataManager.KEY.IGNORE_UPDATE_INFO_KEY, value);
    }

    private boolean isWifi(){
        return NetworkUtils.NETTYPE_WIFI == NetworkUtils.getNetworkType(mContext);
    }

    public void showUpgradeInfo(final UpgradeEntity data){
        final UpgradeDialog dialog = new UpgradeDialog(mContext);
        dialog.setUpgradeData(data);
        boolean done = hasDownload(data);
        String name = null;
        if(done)
            name = mContext.getString(R.string.upgrade_dialog_btn_install);
        dialog.setOkBtn(name,new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hasDownload(data)){
                    AndroidUtils.installApk(mContext,createFile(data.getDownloadUrl()).getPath());
                }else{
                    gotoDownload(data);
                }
                dialog.destory();
            }
        });

        dialog.setCancelBtn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ignoreUpgrade(data);
                dialog.destory();
            }
        });

        dialog.show();
    }

    public boolean hasDownload(UpgradeEntity data){
        File file = createFile(data.getDownloadUrl());

        return file != null && file.exists() && file.length() == data.getFilesize();
    }


    //下载apk
    private void gotoDownload(UpgradeEntity data){
        if (isWifi()) {
            downloadApk(data);
        } else {
            netStateAlert(data);
        }
    }

    private void netStateAlert(final UpgradeEntity data){
        final CommAlertDialog dialog = new CommAlertDialog(mContext);
        dialog.setMessage(mContext.getString(R.string.upgrade_dialog_gps_title));
        dialog.setRightBtn(mContext.getString(R.string.ok), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadApk(data);
                dialog.destory();
            }
        });
        dialog.setLeftBtn(mContext.getString(R.string.cancel),null);
        dialog.show();
    }

    private void downloadApk(final UpgradeEntity data){
        EventBus.getDefault().post(new VerifyStoragePermissionEvent());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(data.getDownloadUrl());
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5000);
                    FileOutputStream fileOutputStream = null;
                    InputStream inputStream;
                    if (connection.getResponseCode() == 200) {
                        inputStream = connection.getInputStream();

                        if (inputStream != null) {
                            File file = createFile(data.getDownloadUrl());
                            fileOutputStream = new FileOutputStream(file);
                            byte[] buffer = new byte[4096];
                            int length = 0;
                            int last = 0;
                            int downloadLength = 0;
                            NotificationMgr.getInstance().showDownloadProgress(length,data.getFilesize());
                            while ((length = inputStream.read(buffer)) != -1) {
                                fileOutputStream.write(buffer, 0, length);
                                downloadLength = downloadLength + length;
                                if ( 100 * (downloadLength - last) / data.getFilesize() > 2) {
                                    NotificationMgr.getInstance().showDownloadProgress(downloadLength, data.getFilesize());
                                    last = downloadLength;
                                }
                            }
                            fileOutputStream.close();
                            fileOutputStream.flush();
                        }
                        inputStream.close();
                    }

                    NotificationMgr.getInstance().showDownloadDone(data.getFilesize());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            showUpgradeInfo(data);
                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            UIHelper.toastMessage(mContext,R.string.upgrade_notify_download_fail);
                        }
                    });
                }
            }
        }).start();
    }

    private File createFile(String url){
        if(StringUtil.isEmpty(url))
            return null;

        String name = url.substring(url.lastIndexOf("/"));
        if(!name.endsWith(".apk")){
            name = name + ".apk";
        }

        File file = FileUtils.createFile(Config.getDownloadPath(),name);
        return file;
    }


    public interface UpdateListener{
        void resule(UpgradeEntity data);

        void fail();
    }

}
