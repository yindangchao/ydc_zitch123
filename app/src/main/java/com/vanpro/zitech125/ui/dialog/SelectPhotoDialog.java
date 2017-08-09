package com.vanpro.zitech125.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;


public class SelectPhotoDialog extends Dialog implements View.OnClickListener {
    private Button btnAlbum;
    private Button btnCarmera;
    protected Button btnCancle;

    public SelectPhotoDialog(Context context) {
        super(context);
        initDialog(context);
    }

    public SelectPhotoDialog(Context context, int theme) {
        super(context, theme);
        initDialog(context);
    }

    protected SelectPhotoDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    private void initDialog(Context context) {
//        setContentView(R.layout.dialog_select_photo);
//        initView();
//        initListener();
//        getWindow().setGravity(Gravity.BOTTOM);
//        getWindow().setLayout(-1, -2);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

}
