package com.qiantang.smartparty.module.push.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.DefaultWebClient;
import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.BaseNotifyBindActivity;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.databinding.ActivityNewWebviewBinding;
import com.qiantang.smartparty.databinding.ActivityNotifyWebviewBinding;
import com.qiantang.smartparty.modle.RxMessageExtra;
import com.qiantang.smartparty.module.push.viewmodel.WebViewModelNotify;
import com.qiantang.smartparty.module.web.viewmodel.WebViewModelNew;
import com.qiantang.smartparty.network.URLs;
import com.qiantang.smartparty.utils.ToastUtil;

import org.android.agoo.common.AgooConstants;

/**
 * Created by zhaoyong bai on 2018/4/28.
 */
public class WebViewNotify extends BaseNotifyBindActivity {
    private ActivityNotifyWebviewBinding binding;
    protected AgentWeb mAgentWeb;
    private WebViewModelNotify viewModel;
    private String url;
    private BaseNotifyBindActivity activity;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void initBind() {
        activity = this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notify_webview);
        viewModel = new WebViewModelNotify(this);
        binding.setViewModel(viewModel);
        initAgentWeb();
    }

    private void initAgentWeb() {
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(binding.llParent, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator(getResources().getColor(R.color.barColor), 1)
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

    @Override
    public void onMessage(Intent intent) {
        super.onMessage(intent);
        String body = intent.getStringExtra(AgooConstants.MESSAGE_ACCS_EXTRA);
        ToastUtil.toast(body);
        if (!TextUtils.isEmpty(body)) {
            RxMessageExtra rxMessageExtra = new Gson().fromJson(body, RxMessageExtra.class);
            if (rxMessageExtra != null) {
                if (rxMessageExtra.getType() == 2) {
                    binding.toolbar.setTitle("公告详情");
                    setUrl(URLs.MESSAGE_DETIAL + rxMessageExtra.getCountId());
                }
            }
        }
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
//        viewModel.initData();
    }

    public void setRes(int resId) {
        binding.toolbar.setResId(resId);
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
                viewModel.share();
                break;
            case R.id.iv_back:
                onBackPressed();
                return;
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
