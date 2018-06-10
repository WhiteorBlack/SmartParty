package com.qiantang.smartparty.module.web.viewmodel;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.config.Event;
import com.qiantang.smartparty.module.web.view.WebViewNew;
import com.qiantang.smartparty.network.URLs;
import com.qiantang.smartparty.utils.ActivityUtil;
import com.qiantang.smartparty.utils.LoadingWindow;
import com.qiantang.smartparty.utils.StringUtil;
import com.qiantang.smartparty.utils.ToastUtil;
import com.qiantang.smartparty.utils.WebUtil;
import com.qiantang.smartparty.utils.location.LocationUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

import static com.qiantang.smartparty.utils.WebUtil.verifyUrlSuffixed;


public class WebViewModelNew implements ViewModel {
    private final LoadingWindow loadingWindow;
    public ObservableField<String> loadUrl = new ObservableField<>();
    public ObservableBoolean isShowIcon = new ObservableBoolean(false);
    public ObservableBoolean isShowText = new ObservableBoolean(false);
    public ObservableField<String> toolBarText = new ObservableField<>();
    public ObservableField<String> toolBarTitle = new ObservableField<>();
    public ObservableBoolean isError = new ObservableBoolean(false);
    public ObservableInt webProgress = new ObservableInt(-1);
    public String url = "";
    private String TAG = "WebViewModel";
    private WebViewNew activity;
    private boolean initOK = false;

    public WebViewModelNew(WebViewNew activity) {
        EventBus.getDefault().register(this);
        this.activity = activity;
        loadingWindow = new LoadingWindow(activity);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Integer i) {
        if (i == Event.RELOAD || i == Event.RELOAD_WEB) {
//            WebUtil.syncCookie(url);
            if (loadUrl.get().equals(url)) {
                loadUrl.notifyChange();
            } else {
                loadUrl.set(url);
            }
            activity.setUrl(url);
        } else if (i == Event.WEB_BACK) {
            activity.goBack();
        } else if (i == Event.KILL_WEB) {
            activity.finish();
        } else if (i == Event.GO_MALL) {
            activity.finish();
        } else if (i == Event.GO_HOME) {
            activity.finish();
        }
    }

    public void initData() {
        Map<String, String> extraMap = ActivityUtil.getExtraMap(activity);
        String url;
        if (extraMap != null) {
            url = extraMap.get(WebUtil.URL);
        } else {
            url = activity.getIntent().getStringExtra(WebUtil.URL);
        }
        WebUtil.syncCookie(url);
        activity.setUrl(verifyUrlSuffixed(url));
    }

    private void setTitle(String title) {
        toolBarTitle.set(title);
    }

    @Override
    public void destroy() {
        EventBus.getDefault().unregister(this);
        if (loadingWindow != null) loadingWindow.dismiss();
    }

    private boolean isLoading = false;

    public WebViewClient getWebViewClient() {
        return new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (!isLoading) {
                    isLoading = true;
                    // 设置是否阻塞图片加载
                    view.getSettings().setBlockNetworkImage(true);
                    if (initOK) {
                    } else {
                        initOK = true;
                    }
                    isError.set(false);
                }
                if (url.contains(URLs.NOTICE_DETIAL)) {
                    setTitle("通知详情");
                }
            }


            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                isLoading = false;
                if (view.getSettings().getLoadsImagesAutomatically()) {
                    view.getSettings().setLoadsImagesAutomatically(true);
                }
//                loadingWindow.hidWindow();
                // 设置是否阻塞图片加载
                view.getSettings().setBlockNetworkImage(false);
                WebViewModelNew.this.url = url;

            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                isError.set(true);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                isError.set(true);
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
                url = verifyUrlSuffixed(url);
                activity.setUrl(url);
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


    private String getDecoderUrl(String url) {
        try {
            url = URLDecoder.decode(url, "UTF-8");
            url = URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }

    private void checkCard() {
        //检测GPS
        boolean isEnabled = LocationUtil.checkGPS(activity);
        if (isEnabled) {
            requestGPSPermission();
        }
    }

    private void checkLocation() {
        //获取地理位置信息
        Location location = LocationUtil.getLocation();
        double latitude;
        double longitude;
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        } else {
            latitude = LocationUtil.LATITUDE;
            longitude = LocationUtil.LONGITUDE;
        }
        if (latitude == 0 && longitude == 0) {
            ToastUtil.toast("获取位置信息失败，添加的GPS权限后重试~");
        } else {
            String gid = StringUtil.getUrlValue(loadUrl.get(), "gid=");
        }
    }

    /**
     * 请求文件读写权限。
     */
    private void requestGPSPermission() {
        new RxPermissions(activity)
                .request(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        checkLocation();
                    } else {
                        ToastUtil.toast("需要访问的GPS权限~");
                    }
                });
    }

    private void showText(String text) {
        isShowText.set(true);
        isShowIcon.set(false);
        toolBarText.set(text);
    }
}
