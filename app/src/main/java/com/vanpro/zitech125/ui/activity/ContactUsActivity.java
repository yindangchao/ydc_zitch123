package com.vanpro.zitech125.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.vanpro.zitech125.R;
import com.vanpro.zitech125.ui.extend.CustomToolbarActivity;

/**
 * Created by Jinsen on 16/7/6.
 */
public class ContactUsActivity extends CustomToolbarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us_layout);
        setTitle(R.string.setting_contact_us);
    }

    @Override
    public void initView() {

    }

    @Override
    public void setLisetener() {
        findViewById(R.id.contact_us_call_phone).setOnClickListener(this);
        findViewById(R.id.contact_us_send_email).setOnClickListener(this);
        findViewById(R.id.contact_us_open_website).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.contact_us_call_phone:
                call();
                break;

            case R.id.contact_us_send_email:
                sendEmail();
                break;

            case R.id.contact_us_open_website:
                mywebsite();
                break;
        }
    }


    private void call(){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + getString(R.string.contact_us_phone));
        intent.setData(data);
        startActivity(intent);
    }

    private void sendEmail(){
        Uri uri = Uri.parse("mailto:"+getString(R.string.contact_us_email));
        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
        startActivity(it);
    }

    private void mywebsite(){
        Intent intent = new Intent(this,WebviewActivity.class);
        intent.putExtra("url",getResources().getString(R.string.contact_us_website));
        startActivity(intent);
    }
}
