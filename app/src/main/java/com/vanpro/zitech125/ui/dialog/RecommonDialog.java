package com.vanpro.zitech125.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;

import de.greenrobot.event.EventBus;
import com.vanpro.zitech125.R;
import com.vanpro.zitech125.event.StatusChangeEvent;
import com.vanpro.zitech125.location.ZLocation;
import com.vanpro.zitech125.manage.StatusManage;
import com.vanpro.zitech125.present.LocationMgr;
import com.vanpro.zitech125.util.AndroidUtils;

/**
 * Created by Jinsen on 16/7/6.
 */
public class RecommonDialog extends Dialog implements View.OnClickListener {

    Context mContext;

    View mOpenMapApp = null;

    public RecommonDialog(Context context) {
        this(context, R.style.dialogButtom);
    }

    public RecommonDialog(Context context, int themeResId) {
        super(context, themeResId);
        initDialog(context);
        EventBus.getDefault().register(this);
    }

    private void initDialog(Context context) {
        mContext = context;
        setContentView(R.layout.dialog_recommon_layout);
        initView();
        initListener();
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setLayout(-1, -2);
    }

    private void initView() {
        mOpenMapApp = findViewById(R.id.recommon_open_map_app);
        setOpenMapAppViewState();
    }

    private void initListener() {
        findViewById(R.id.recommon_open_map_app).setOnClickListener(this);
        findViewById(R.id.recommon_share).setOnClickListener(this);
        findViewById(R.id.recommon_cancel).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.recommon_open_map_app:
                openMap();
                dismiss();
                break;

            case R.id.recommon_share:
                shareDialog();
                break;

            case R.id.recommon_cancel:
                dismiss();
                break;
        }
    }

    private void shareDialog() {
        dismiss();
        ShareDialog shareDialog = new ShareDialog(mContext);
        shareDialog.show();
    }

    private void openMap() {
        Uri mUri = null;
        ZLocation location = null;
        if (StatusManage.getInstance().getStatus() == StatusManage.Status.LOCATION_SUCC) {
            location = StatusManage.getInstance().getCarLocation();
            if(location == null || location.getLocation() == null)
                return;
            if("ZH".equals(AndroidUtils.getLanguage(getContext()))){
                mUri = Uri.parse("geo:" + location.getLocation().getLatitude() + "," + location.getLocation().getLongitude());
                Intent mIntent = new Intent(Intent.ACTION_VIEW, mUri);
                mContext.startActivity(mIntent);
            }else {
                openGoogleNavi(location.getLocation().getLatitude(), location.getLocation().getLongitude());
            }
            return;
        } else {
            location = LocationMgr.getInstance().getLocation();
        }
    }

    /**
     * 打开google地图客户端开始导航
     * q:目的地
     * mode：d驾车 默认
     */
    private void openGoogleNavi(double lat, double lng) {
        if (isGoogleMapInstalled()) {
            StringBuffer stringBuffer = new StringBuffer("google.navigation:q=").append(lat).append(",").append(lng).append("&mode=d");
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(stringBuffer.toString()));
            i.setPackage("com.google.android.apps.maps");
            mContext.startActivity(i);
        } else {
            /**
             * 打开google Web地图导航
             */
            StringBuffer stringBuffer = new StringBuffer("http://ditu.google.cn/maps?hl=")
                    .append(AndroidUtils.getLanguage(mContext)).append("&mrt=loc&q=").append(lat).append(",").append(lng);
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(stringBuffer.toString()));
            mContext.startActivity(i);
        }
    }

    private boolean isGoogleMapInstalled() {
        String pkgName = "com.google.android.apps.maps";
        PackageInfo packageInfo = null;
        try {
            packageInfo = mContext.getPackageManager().getPackageInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null) {
            return false;
        } else {
            return true;
        }
    }

    public void onEventMainThread(StatusChangeEvent event) {
        setOpenMapAppViewState();
    }

    private void setOpenMapAppViewState(){
        if(mOpenMapApp == null)
            return;
        if(StatusManage.getInstance().getStatus() == StatusManage.Status.LOCATION_SUCC){
            mOpenMapApp.setVisibility(View.VISIBLE);
        }else{
            mOpenMapApp.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        EventBus.getDefault().unregister(this);
    }
}
