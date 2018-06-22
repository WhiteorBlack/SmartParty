package com.qiantang.smartparty.module.web.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.just.agentweb.AgentWeb;
import com.just.agentweb.DefaultWebClient;
import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.databinding.ActivityNewWebviewBinding;
import com.qiantang.smartparty.module.web.viewmodel.WebViewModelNew;

/**
 * Created by zhaoyong bai on 2018/4/28.
 */
public class WebViewNew extends BaseBindActivity {
    private ActivityNewWebviewBinding binding;
    protected AgentWeb mAgentWeb;
    private WebViewModelNew viewModel;
    private String url;
    private BaseBindActivity activity;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void initBind() {
        activity = this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_webview);
        viewModel = new WebViewModelNew(this);
        binding.setViewModel(viewModel);
        initAgentWeb();
    }

    private void initAgentWeb() {
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(binding.llParent, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator(getResources().getColor(R.color.colorPrimary), 1)
                .setWebChromeClient(mWebChromeClient)
                .setWebViewClient(viewModel.getWebViewClient())
                .addJavascriptInterface("puxiang", new JsToAndroid())
                .setMainFrameErrorView(R.layout.agentweb_error_page, -1)
                .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
                .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)//打开其他应用时，弹窗咨询用户是否前往其他应用
                .interceptUnkownUrl() //拦截找不到相关页面的Scheme
                .createAgentWeb()
                .go(url);
    }

    private String getUrl() {
        return url;
    }

    @SuppressLint("ResourceAsColor")
    public void setUrl(String url) {
        this.url = url;
        if (mAgentWeb != null) {
            mAgentWeb.getUrlLoader().loadUrl(url);
        } else {
            initAgentWeb();
        }

    }

    @Override
    public void initView() {
        binding.toolbar.setResId(R.mipmap.icon_share_white);
        viewModel.initData();
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_right:

                break;
        }
    }

    public void goBack() {
        if (!mAgentWeb.getWebCreator().getWebView().canGoBack()) {
            onBackPressed();
        } else {
            mAgentWeb.back();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        mAgentWeb.getWebLifeCycle().onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAgentWeb.getWebLifeCycle().onResume();
    }

    @Override
    protected void viewModelDestroy() {
        if (viewModel != null) {
            viewModel.destroy();
        }
        mAgentWeb.clearWebCache();
        mAgentWeb.getWebLifeCycle().onDestroy();
    }

    private void setResult(String data) {
        Intent intent = new Intent();
        intent.putExtra("data", data);
        this.setResult(RESULT_OK, intent);
        finish();
    }

    private class JsToAndroid {

        @JavascriptInterface
        public void back() {
            finish();
        }

        /**
         * 刷新 native 数据
         *
         * @param data
         */
        @JavascriptInterface
        public void refresh(String data) {
            setResult(data);
        }
    }
}
