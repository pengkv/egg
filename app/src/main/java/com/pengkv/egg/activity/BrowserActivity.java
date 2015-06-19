package com.pengkv.egg.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.pengkv.egg.R;
import com.pengkv.egg.interfaces.IInit;


/**
 * 基于浏览器的活动
 * Created by lyq on 2014/8/20.
 */
public class BrowserActivity extends BaseActivity implements IInit {

    public int pointNum = 0;//网页请求次数
    /**
     * 浏览器默认地址
     */
    protected String mInitUrl = "www.baidu.com";
    /**
     * 浏览器
     */
    protected WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);

        mWebView = (WebView) findViewById(R.id.webview);

        init();
        setWebView();
    }

    /**
     * 设置浏览器配置
     */
    public void setWebView() {
        // 开启Javascript支持,访问的页面中有Javascript，或者需要通过js和页面交互
        mWebView.getSettings().setJavaScriptEnabled(true);
        if (mInitUrl != null && mInitUrl.length() > 0) {
            mWebView.loadUrl(mInitUrl);
        }

        // 使浏览器中的链接地址还是停留在app页面内，不调用系统的浏览器
        mWebView.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 没有Android交互时加载Url
                if (!onOverrideUrl(view, url)) view.loadUrl(url);
                return true;
            }

            // 加载完成时不显示加载的符号
            @Override
            public void onPageFinished(WebView view, String url) {
                toggleProgress(false);//关闭进度圈
                super.onPageFinished(view, url);
            }

            // 加载时显示加载符号
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                toggleProgress(true);//打开进度圈
                pointNum++;
                super.onPageStarted(view, url, favicon);
            }

        });
    }

    /**
     * 覆写该方法支持Js与Android交互
     * @return true：不加载url  false：继续加载Url
     */
    protected boolean onOverrideUrl(WebView view, String url) {
        return false;
    }

    @Override
    public void init() {
    }

}
