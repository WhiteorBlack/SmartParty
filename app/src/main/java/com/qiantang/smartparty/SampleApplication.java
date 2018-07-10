package com.qiantang.smartparty;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;


import com.qiantang.smartparty.base.ApplicationLike;
import com.qiantang.smartparty.services.UmengNotificationService;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;

import org.android.agoo.huawei.HuaWeiRegister;
import org.android.agoo.xiaomi.MiPushRegistar;

import java.lang.reflect.Constructor;


public class SampleApplication extends Application {
    private ApplicationLike applicationLike;
    public static final int TINKER_DISABLE = 0x00;
    public static final int TINKER_DEX_MASK = 0x01;
    public static final int TINKER_NATIVE_LIBRARY_MASK = 0x02;
    public static final int TINKER_RESOURCE_MASK = 0x04;
    public static final int TINKER_DEX_AND_LIBRARY = TINKER_DEX_MASK | TINKER_NATIVE_LIBRARY_MASK;
    public static final int TINKER_ENABLE_ALL = TINKER_DEX_MASK | TINKER_NATIVE_LIBRARY_MASK | TINKER_RESOURCE_MASK;
    private long applicationStartElapsedTime;
    private long applicationStartMillisTime;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationLike.onCreate();
//        InitializeService.start(this, InitializeService.ACTION_INIT_WHEN_APP_CREATE);
        initUMeng();
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
        mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE);

        //注册推送服务 每次调用register都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                MyApplication.TOKEN = deviceToken;
            }

            @Override
            public void onFailure(String s, String s1) {
            }
        });

        mPushAgent.setPushIntentServiceClass(UmengNotificationService.class);

        MiPushRegistar.register(this, "2882303761517834569", "5911783494569");
        HuaWeiRegister.register(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        applicationStartElapsedTime = SystemClock.elapsedRealtime();
        applicationStartMillisTime = System.currentTimeMillis();
        this.ensureDelegate();

    }

    private synchronized void ensureDelegate() {
        if (this.applicationLike == null) {
            this.applicationLike = this.createDelegate();
        }
    }

    private ApplicationLike createDelegate() {
        try {
            Class<?> delegateClass = Class.forName("com.qiantang.smartparty.MyApplication", false, getClassLoader());
            Constructor<?> constructor = delegateClass.getConstructor(Application.class, int.class, boolean.class,
                    long.class, long.class, Intent.class);
            return (ApplicationLike) constructor.newInstance(this, TINKER_ENABLE_ALL, false,
                    applicationStartElapsedTime, applicationStartMillisTime, null);
        } catch (Throwable var3) {
            throw new RuntimeException("createDelegate failed", var3);
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (this.applicationLike != null) {
            this.applicationLike.onTerminate();
        }

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (this.applicationLike != null) {
            this.applicationLike.onLowMemory();
        }

    }

    @TargetApi(14)
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (this.applicationLike != null) {
            this.applicationLike.onTrimMemory(level);
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (this.applicationLike != null) {
            this.applicationLike.onConfigurationChanged(newConfig);
        }

    }

    @Override
    public Resources getResources() {
        Resources var1 = super.getResources();
        return this.applicationLike != null ? this.applicationLike.getResources(var1) : var1;
    }

    @Override
    public ClassLoader getClassLoader() {
        ClassLoader classLoader = super.getClassLoader();
        if (applicationLike != null) {
            return applicationLike.getClassLoader(classLoader);
        }
        return classLoader;
    }

    @Override
    public AssetManager getAssets() {
        AssetManager var1 = super.getAssets();
        return this.applicationLike != null ? this.applicationLike.getAssets(var1) : var1;
    }

    @Override
    public Object getSystemService(String name) {
        Object var2 = super.getSystemService(name);
        return this.applicationLike != null ? this.applicationLike.getSystemService(name, var2) : var2;
    }

    @Override
    public Context getBaseContext() {
        Context var1 = super.getBaseContext();
        return this.applicationLike != null ? this.applicationLike.getBaseContext(var1) : var1;
    }

}