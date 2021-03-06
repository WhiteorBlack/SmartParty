package com.qiantang.smartparty.services;

import android.app.Activity;
import android.app.Dialog;
import android.app.IntentService;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.flyco.animation.BounceEnter.BounceLeftEnter;
import com.flyco.animation.SlideExit.SlideLeftExit;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.qiantang.smartparty.MyApplication;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.config.CacheKey;
import com.qiantang.smartparty.config.Config;
import com.qiantang.smartparty.modle.AppVersionJSON;
import com.qiantang.smartparty.network.FrescoImageLoader;
import com.qiantang.smartparty.network.URLs;
import com.qiantang.smartparty.utils.AppUtil;
import com.qiantang.smartparty.utils.DateUtils;
import com.qiantang.smartparty.utils.NetworkUtil;
import com.qiantang.smartparty.widget.MyMaterialDialog;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import org.lzh.framework.updatepluginlib.UpdateConfig;
import org.lzh.framework.updatepluginlib.creator.DialogCreator;
import org.lzh.framework.updatepluginlib.model.Update;
import org.lzh.framework.updatepluginlib.model.UpdateParser;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ThemeConfig;

public class InitializeService extends IntentService {
    private boolean isConfirm;
    private boolean isUpdateConfirm;
    private String createTime = "";
    public static final String ACTION_INIT_WHEN_APP_CREATE = "com.qiantang.smartparty.services.action.init";
    private static String TAG = "InitializeService";
    public static final String UPDATE_STATUS_ACTION = "com.umeng.message.example.action.UPDATE_STATUS";
    private Handler handler;

    public InitializeService() {
        super("InitializeService");
    }

    public static void start(Context context, String action) {
        Log.e(TAG, "start: ");
        Intent intent = new Intent(context, InitializeService.class);
        intent.setAction(action);
        context.startService(intent);
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand: ");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            Log.e(TAG, "onHandleIntent: " + action);
            if (ACTION_INIT_WHEN_APP_CREATE.equals(action)) {
                performInit();
            }
        }
    }

    private void performInit() {
        if (MyApplication.isInit) {
            return;
        }
        MyApplication.isInit = true;
        UMShareAPI.get(this);
        initLogger();
        initPlatformConfig();
        initGalleryFinal();
        initUpdateConfig();
//        initUMeng();
    }


    /**
     * 初始化友盟统计分析
     */
    private void initUMeng() {
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
//        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, Config.UPUSH_SECRET);
        UMConfigure.setLogEnabled(true);
        UMConfigure.setEncryptEnabled(true);
        UMConfigure.init(this, "5b077233f43e481af30000ae", "party", UMConfigure.DEVICE_TYPE_PHONE,
                "ad77ceb416bdacca751d5909c1c4e361");
        initUpush();
    }

    private void initUpush() {
        PushAgent mPushAgent = PushAgent.getInstance(this);
        handler = new Handler(getMainLooper());
        mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE);

        //注册推送服务 每次调用register都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                Log.i(TAG, "device token: " + deviceToken);
                sendBroadcast(new Intent(UPDATE_STATUS_ACTION));
            }

            @Override
            public void onFailure(String s, String s1) {
                Log.i(TAG, "register failed: " + s + " " + s1);
                sendBroadcast(new Intent(UPDATE_STATUS_ACTION));
            }
        });

        mPushAgent.setPushIntentServiceClass(UmengNotificationService.class);

    }


    private void initLogger() {
        Logger.init(Config.LOG_TAG).setLogLevel(Config.LOG_LEVEL);
    }

    private void initPlatformConfig() {
        //微信 appid appsecret
        PlatformConfig.setWeixin(Config.WX_APP_ID, Config.WX_APP_SECRET);
        PlatformConfig.setQQZone(Config.QQ_APP_ID, Config.QQ_APP_SECRET);
//        PlatformConfig.setSinaWeibo(Config.SINA_APP_ID, Config.SINA_APP_SECRET, Config.SINA_REDICT_URL);

        com.umeng.socialize.Config.DEBUG = true;
    }

    /**
     * 版本升级配置
     */
    private void initUpdateConfig() {
        UpdateConfig
                .getConfig()
                .init(MyApplication.getContext())
                // 必填：数据更新接口
                .url(URLs.GET_VERSION)
                // 必填：用于从数据更新接口获取的数据response中。解析出Update实例。以便框架内部处理
                .jsonParser(new UpdateParser() {
                    @Override
                    public Update parse(String response) {
                        Gson gson = new Gson();
                        AppVersionJSON appVersionJSON = gson.fromJson(response, AppVersionJSON.class);
                        AppVersionJSON.ReturnObjectBean bean = appVersionJSON.getData();
                        if (!TextUtils.equals(bean.getVersionId(), AppUtil.getVerName(MyApplication.getContext()))) {
                            bean.setVersionCode(AppUtil.getVersionCode(MyApplication.getContext()) + 1);
                        }
                        if (appVersionJSON.getStatus().equals(URLs.RESPONSE_OK)) {
                            MyApplication.mCache.put(CacheKey.VERSION, response);
                        }
                        // 此处模拟一个Update对象
                        Update update = new Update(response);
                        try {
                            // 此apk包的更新时间
                            update.setUpdateTime(DateUtils.convert2long(createTime));
                            // 此apk包的下载地址
                            update.setUpdateUrl(bean.getVersionUrl());


                            // 此apk包的版本号
                            update.setVersionCode(bean.getVersionCode());
                            // 此apk包的版本名称
                            update.setVersionName(bean.getVersionId());
                            // 此apk包的更新内容
                            update.setUpdateContent(bean.getVersionExplain());
                            // 此apk包是否为强制更新
//                            update.setForced(false);
                            update.setForced(bean.getConUpdate() == 1);
                            // 是否显示忽略此次版本更新按钮
                            update.setIgnore(true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return update;
                    }
                })
                .updateDialogCreator(new DialogCreator() {
                    @Override
                    public Dialog create(Update update, Activity context) {
                        final NormalDialog updateDialog = new NormalDialog(context);
                        updateDialog.titleTextColor(getResources().getColor(R.color.sale_price))
                                .style(NormalDialog.STYLE_TWO).titleTextSize(18).title("温馨提示")
                                .cornerRadius(0)
                                .content("检测到当前使用的是移动流量，是否确定更新？")

                                .contentGravity(Gravity.START)
                                .showAnim(new BounceLeftEnter()).dismissAnim(new SlideLeftExit())
                                .btnText("取消", "立即更新").setOnBtnClickL(() -> {
                            updateDialog.dismiss();
                            sendUserCancel();
                        }, () -> {
                            isUpdateConfirm = true;
                            updateDialog.dismiss();
                        });
                        updateDialog.setOnDismissListener(dialog1 -> {
                            if (isUpdateConfirm) {
                                isUpdateConfirm = false;
                                sendDownloadRequest(update);
                            } else {
                                sendUserCancel();
                            }
                        });
                        boolean isForced = update.isForced();
                        MyMaterialDialog dialog = new MyMaterialDialog(context, isForced);
                        dialog.title("发现新版本 " + update.getVersionName())
                                .titleTextColor(AppUtil.getColor(R.color.white))
                                .titleTextSize(16)
                                .contentTextSize(14)
                                .cornerRadius(10)
                                .content(update.getUpdateContent()).contentGravity(Gravity.CENTER)
                                .btnNum(isForced ? 1 : 3)
                                .btnTextColor(new int[]{R.color.none_text, R.color.barColor})
                                .btnText(isForced ? new String[]{"立即更新"}
                                        : new String[]{"取消", "", "立即更新"})
                                .showAnim(new BounceLeftEnter())
                                .dismissAnim(new SlideLeftExit())
                                .setOnBtnClickL(isForced ? new OnBtnClickL[]{() -> {
                                    isConfirm = true;
                                    dialog.dismiss();
                                }} : new OnBtnClickL[]{() -> {
                                    dialog.dismiss();
                                    sendUserCancel();
                                }, () -> {
                                }, () -> {
                                    isConfirm = true;
                                    MyApplication.mCache.remove(CacheKey.VERSION);
                                    dialog.dismiss();
                                }});
                        dialog.setOnDismissListener(dialog1 -> {
                            if (isConfirm) {
                                isConfirm = false;
                                if (isForced) {
                                    sendDownloadRequest(update);
                                } else {
                                    boolean wifi = NetworkUtil.isWifi(context);
                                    if (wifi) {
                                        sendDownloadRequest(update);
                                    } else {
                                        updateDialog.show();
                                    }
                                }
                            }
                        });
                        return dialog;
                    }
                });
    }

    /**
     * 图片选择器配置
     */
    private void initGalleryFinal() {
        ThemeConfig themeConfig = ThemeConfig.DEFAULT;
        FunctionConfig functionConfig = new FunctionConfig.Builder().setEnableEdit(true)
                .setEnableCrop(true).setCropSquare(true).setForceCrop(true).setEnablePreview
                        (true).setEnableCamera(false).build();

        FrescoImageLoader imageLoader = new FrescoImageLoader(this);

        CoreConfig coreConfig = new CoreConfig.Builder(this, imageLoader, themeConfig)
                .setFunctionConfig(functionConfig).setNoAnimcation(true).build();
        GalleryFinal.init(coreConfig);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}