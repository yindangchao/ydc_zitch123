package com.vanpro.zitech125.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.vanpro.zitech125.R;
import com.vanpro.zitech125.ui.extend.CustomToolbarActivity;
import com.vanpro.zitech125.ui.widget.MyProgress;
import com.vanpro.zitech125.util.StringUtil;


/**
 *
 * Created by Jinsen on 16/5/1.
 */
public class WebviewActivity extends CustomToolbarActivity {

    /**
     * Views
     */
    private WebView webView;
    private MyProgress progressBar;

    String mUrl = null;
    String mTitle = null;

    public static void start(Context context, String url, String title){
        Intent intent = new Intent(context, WebviewActivity.class);
        intent.putExtra("url",url);
        intent.putExtra("title",title);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        mUrl = getIntent().getStringExtra("url");
        mTitle = getIntent().getStringExtra("title");
        setTitle(mTitle);
    }

    @Override
    public void initView() {
        webView = (WebView) findViewById(R.id.wv_webView);

        progressBar = (MyProgress) findViewById(R.id.progress);
        progressBar.setBackgroundColor(getResources().getColor(R.color.white));
        progressBar.setProgressColor(Color.parseColor("#FF6430"));


        webviewSetting();

        if(StringUtil.isNotEmpty(mUrl) && !mUrl.startsWith("http://") && !mUrl.startsWith("https://") && !mUrl.startsWith("file://"))
            mUrl = "http://"+mUrl;

        webView.loadUrl(mUrl);
    }

    @Override
    public void setLisetener() {

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (webView != null) {
            webView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (webView != null) {
            webView.onPause();
        }
    }

    private void webviewSetting() {
        webView.setVerticalScrollBarEnabled(true);
        webView.setHorizontalScrollBarEnabled(false);
        // 自适应屏幕
//        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.getSettings().setBuiltInZoomControls(true);

        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAllowContentAccess(true);

        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT); // 设置缓存模式
        webView.getSettings().setDomStorageEnabled(true);// 开启 DOM storage API 功能
        webView.getSettings().setDatabaseEnabled(true); //开启 database storage API 功能


        WebViewClient client = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("tel:")) { // 使用本地呼叫页面打电话
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                    startActivity(intent);
                } else if (url.startsWith("http:") || url.startsWith("https:")) {//使用当前的WebView加载页面
                    webView.loadUrl(url);
                    return true;
                }
                return true;

            }
        };
        webView.setWebViewClient(client);

        WebChromeClient webChromeClient = new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    progressBar.setProgress(0);
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
//                //当前页没有标题
                if (null == mTitle) {
                    setTitle(title);
//                    titles.add(title);
                }
            }
        };

        webView.setWebChromeClient(webChromeClient);
    }


}
