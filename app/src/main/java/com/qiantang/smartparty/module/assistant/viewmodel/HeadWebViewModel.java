package com.qiantang.smartparty.module.assistant.viewmodel;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.just.agentweb.AgentWeb;
import com.just.agentweb.DefaultWebClient;
import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.module.web.view.WebViewNew;
import com.qiantang.smartparty.module.web.viewmodel.WebViewModelNew;
import com.qiantang.smartparty.network.URLs;

import static com.qiantang.smartparty.utils.WebUtil.verifyUrlSuffixed;

/**
 * Created by zhaoyong bai on 2018/6/10.
 */
public class HeadWebViewModel implements ViewModel {
    private BaseBindActivity activity;
    private AgentWeb mAgentWeb;
    private String url;

    public HeadWebViewModel(BaseBindActivity activity) {
        this.activity = activity;
    }

    public void initWev(ViewGroup viewGroup) {
        mAgentWeb = AgentWeb.with(activity)
                .setAgentWebParent(viewGroup, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator(activity.getResources().getColor(R.color.colorPrimary), 1)
                .setWebChromeClient(mWebChromeClient)
                .setWebViewClient(getWebViewClient())
                .setMainFrameErrorView(R.layout.agentweb_error_page, -1)
                .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
                .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)//打开其他应用时，弹窗咨询用户是否前往其他应用
                .interceptUnkownUrl() //拦截找不到相关页面的Scheme
                .createAgentWeb()
                .go(url);
    }

    private WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {

        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }
    };


    public WebViewClient getWebViewClient() {
        return new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }


            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

            }


            @Override //网页加载 禁止在浏览器打开在本应用打开
            public boolean shouldOverrideUrlLoading(WebView web, String url) {
                if (!url.contains("http://")) {
                    if (url.contains("tel:")) {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(intent);
                    }
                    return true;
                }
                return true;
            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {

                // ------5.0以上手机执行------
                Uri uri = request.getUrl();
                String url = uri.toString();
                return shouldInterceptRequest(view, url);
            }


        };
    }


    @Override
    public void destroy() {

    }
}
