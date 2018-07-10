package com.qiantang.smartparty.module.pay.viewmodel;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableDouble;
import android.databinding.ObservableField;
import android.text.TextUtils;
import android.util.Log;

import com.alipay.sdk.app.PayTask;
import com.orhanobut.logger.Logger;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.config.Config;
import com.qiantang.smartparty.config.Event;
import com.qiantang.smartparty.modle.RxPayChannel;
import com.qiantang.smartparty.modle.RxWXPayInfo;
import com.qiantang.smartparty.module.pay.alipay.PayResult;
import com.qiantang.smartparty.module.pay.view.PayActivity;
import com.qiantang.smartparty.network.retrofit.ApiWrapper;
import com.qiantang.smartparty.network.retrofit.RetrofitUtil;
import com.qiantang.smartparty.utils.LoadingWindow;
import com.qiantang.smartparty.utils.ToastUtil;
import com.qiantang.smartparty.widget.dialog.DefaultDialog;
import com.qiantang.smartparty.widget.dialog.OnDialogExecuteListener;
import com.qiantang.smartparty.wxapi.WeChatPay;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PayViewModel implements ViewModel {
    public static final int PLUGIN_VALID = 0;
    private static final int PLUGIN_NOT_INSTALLED = -1;
    private static final int PLUGIN_NEED_UPGRADE = 2;
    private final String mMode = "00";

    private final PayActivity activity;
    public ObservableDouble totalPrices = new ObservableDouble(0.00);
    public ObservableBoolean isInitData = new ObservableBoolean();
    public ObservableField<RxPayChannel> aliPayChannel = new ObservableField<>();
    public ObservableField<RxPayChannel> wxPayChannel = new ObservableField<>();
    public ObservableField<RxPayChannel> unionPayChannel = new ObservableField<>();
    public ObservableBoolean isLoading = new ObservableBoolean(true);
    private String orderId;

    private LoadingWindow loadingWindow;
    private String TAG = "PayViewModel";
    private DefaultDialog dialog;

    public PayViewModel(PayActivity activity) {
        EventBus.getDefault().register(this);
        this.activity = activity;
        loadingWindow = new LoadingWindow(activity);
        loadingWindow.delayedShowWindow();
        getCache();
        getPayInfo(Config.PRODUCTORDER, orderId);
        createDialog();
        EventBus.getDefault().post(Event.RELOAD_WEB);
    }

    private void createDialog() {
        dialog = new DefaultDialog(activity, "网络连接失败,请重试!", new OnDialogExecuteListener() {
            @Override
            public void execute() {

            }

            @Override
            public void cancel() {

            }
        });
    }

    private void getCache() {
        orderId = activity.getIntent().getStringExtra("orderId");
        activity.setPageTag("orderId:", orderId);
        Log.e(TAG, "getCache: " + orderId);
    }


    private void wxPay(RxWXPayInfo bean) {
        WeChatPay weChatPay = new WeChatPay(activity, bean);
        weChatPay.payWechat();
    }

    private void getPayInfo(String orderType, String orderId) {

//        ApiWrapper.getInstance()
//                .getPayInfo(orderType, orderId)
//                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
//                .doOnTerminate(loadingWindow::delayHideWindow)
//                .doOnComplete(() -> isInitData.set(true))
//                .subscribe(new NetworkSubscriber<RxPayPrice>() {
//                    @Override
//                    public void onError(Throwable e) {
//                        super.onError(e);
//                        Logger.e(e.toString());
//                    }
//
//                    @Override
//                    public void onFail(RetrofitUtil.APIException e) {
//                        super.onFail(e);
//                        activity.showNoneView("当前网络不可用~");
//                    }
//
//                    @Override
//                    public void onSuccess(RxPayPrice bean) {
//                        Logger.e("totalPrice--" + bean.getTotalPrice());
//                        totalPrices.set(bean.getTotalPrice());
//                    }
//                });
    }


    /**
     * call alipay sdk pay. 调用SDK支付
     */
    public void aliPay() {
//        ApiWrapper.getInstance()
//                .alipaySign(Config.PRODUCTORDER, orderId)
//                .subscribeOn(Schedulers.io())
//                .map((String alipaySign) -> {
//                    PayTask alipay = new PayTask(activity);
//                    Logger.e("alipaySign--" + alipaySign);
//                    // 调用支付接口，获取支付结果
//                    return alipay.pay(alipaySign, true);
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new NetworkSubscriber<String>() {
//                    @Override
//                    public void onSuccess(String result) {
//                        aliPayResult(result);
//                    }
//                });
    }

    private void aliPayResult(String result) {
        isLoading.set(true);
        PayResult payResult = new PayResult(result);
//TODO:同步返回的结果必须放置到服务端进行验证
//（验证的规则请看https://doc.open.alipay.com/doc2/detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&docType=1)
//建议商户依赖异步通知

        Log.e("333", "handleMessage: " + payResult.getResult());
        String resultInfo = payResult.getResult();// 同步返回需要验证的信息
        String resultStatus = payResult.getResultStatus();
        // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
        if (TextUtils.equals(resultStatus, "9000")) {
            ToastUtil.toast("支付成功");

        } else {
            // 判断resultStatus 为非"9000"则代表可能支付失败
            // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
            if (TextUtils.equals(resultStatus, "8000")) {
                ToastUtil.toast("支付结果确认中");
            } else {
                // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                ToastUtil.toast("支付失败");
            }
        }
    }

    /**
     * 获取微信支付信息
     */
    public void getWeixinPayInfo() {
        loadingWindow.showWindow();
//        ApiWrapper.getInstance()
//                .weixinPaySign(Config.PRODUCTORDER, 0, orderId)
//                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
//                .doOnTerminate(() -> loadingWindow.hidWindow())
//                .subscribe(new NetworkSubscriber<RxWXPayInfo>() {
//                    @Override
//                    public void onError(Throwable e) {
//                        super.onError(e);
//                        Logger.e("wx" + e.toString());
//                    }
//
//                    @Override
//                    public void onFail(RetrofitUtil.APIException e) {
//                        super.onFail(e);
//                        activity.showNoneView("当前网络不可用~");
//                    }
//
//                    @Override
//                    public void onSuccess(RxWXPayInfo bean) {
//                        Logger.e("wx" + bean.toString());
//                        wxPay(bean);
//                    }
//                });
    }

    /**
     * 微信支付结果回调
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BaseResp resp) {
        isLoading.set(true);
        int errCode = resp.errCode;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Integer i) {
        if (i == Event.FINISH || i == Event.GO_HOME) {
            activity.finish();
        }
    }

    @Override
    public void destroy() {
        EventBus.getDefault().unregister(this);
        loadingWindow.dismiss();
    }

}
