package com.vanpro.zitech125.ui.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.vanpro.zitech125.R;
import com.vanpro.zitech125.ui.extend.BaseDialog;
import com.vanpro.zitech125.util.LogUtil;


/**
 * Loading - 自定义加载信息框
 * <p/>
 * Created by tony on 8/25/14.
 */
public class LoadingDialog extends BaseDialog {
    private static LoadingDialog loadingDialog;

    private TextView tvMsg;
    private String msg;

    public LoadingDialog(Context context, String msg) {
        super(context, R.style.loadingDialog);
        this.msg = msg;
    }

    @Override
    protected void beforeInitView() {
        setContentView(R.layout.dialog_loading);
    }

    @Override
    protected void initView() {
        tvMsg = (TextView) findViewById(R.id.loadingTextView);
    }

    @Override
    protected void initListener() {
        setCancelable(false);
        getWindow().getAttributes().gravity = Gravity.CENTER;
    }

    @Override
    protected void initData() {
        tvMsg.setText(msg);
        tvMsg.setVisibility(TextUtils.isEmpty(msg) ? View.GONE : View.VISIBLE);
    }

    protected void setTvMsg(String msg){
        tvMsg.setText(msg);
    }

    /**
     * 显示 - loading
     *
     * @param context
     * @param msg
     * @return
     */
    public static LoadingDialog show(Context context, String msg) {
        safeCloseDialog();
        loadingDialog = new LoadingDialog(context, msg);
        loadingDialog.setCancelable(false);

        loadingDialog.show();
        return loadingDialog;
    }

    /**
     * 显示 - loading
     * loading 按返回键不能取消
     * @param context
     * @param msg
     * @return
     */
    public static LoadingDialog showForce(Context context, String msg){
        safeCloseDialog();
        loadingDialog = new LoadingDialog(context, msg);
        loadingDialog.setCancelable(false);
        loadingDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return true;
            }
        });
        loadingDialog.show();
        return loadingDialog;
    }

    /**
     * 持续显示Diaglog切换文字
     * @param context
     * @param msg
     * @return
     */
    public static LoadingDialog showLast(Context context, String msg){
        if (loadingDialog != null){
            loadingDialog.setTvMsg(msg);
            if (!loadingDialog.isShowing())
                loadingDialog.show();
        }else {
            loadingDialog = new LoadingDialog(context, msg);
            loadingDialog.show();
        }
        return loadingDialog;
    }

    /**
     * 销毁 - loading
     */
    public static void dispose() {
        safeCloseDialog();
    }


    /**
     * 安全关闭dialog
     * 防止暴ILSE
     */
    private static void safeCloseDialog(){
        if (loadingDialog != null) {
            try {
                if (loadingDialog.isShowing())
                    loadingDialog.dismiss();
            }catch (Exception e){
                LogUtil.e("LoadingDialog","safeCloseDialog Exception " + e.getMessage());
            }
            loadingDialog = null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dispose();
    }
}
