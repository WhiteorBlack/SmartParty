package com.qiantang.smartparty;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.facebook.common.internal.Supplier;
import com.facebook.common.util.ByteConstants;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.cache.MemoryCacheParams;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.ProgressiveJpegConfig;
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig;
import com.orhanobut.logger.Logger;
import com.qiantang.smartparty.base.ApplicationLike;
import com.qiantang.smartparty.config.CacheKey;
import com.qiantang.smartparty.services.InitializeService;
import com.qiantang.smartparty.utils.ACache;
import com.qiantang.smartparty.utils.StringUtil;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;

public class MyApplication extends ApplicationLike {
    private Application application;

    public MyApplication(Application application, int tinkerFlags,
                         boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime,
                         long applicationStartMillisTime, Intent tinkerResultIntent) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime,
                applicationStartMillisTime, tinkerResultIntent);
    }

    public MyApplication(Application application) {
        super(application);
    }

    public static String INFO = "";
    public static int heightPixels;
    public static int widthPixels;
    public static String TOKEN = "";
    public static String USER_ID = "";
    public static ACache mCache;
    private static Context context;
    public static String info = "";

    public static boolean isInit = false;
    public static ObservableBoolean isRefreshing = new ObservableBoolean(false);
    public static ObservableBoolean isLoginOB = new ObservableBoolean(false);
    public static ObservableBoolean isPop = new ObservableBoolean(false);

    @Override
    public void onCreate() {
        super.onCreate();
        application = getApplication();
        init();
        InitializeService.start(this.getContext(), InitializeService.ACTION_INIT_WHEN_APP_CREATE);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        MultiDex.install(base);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void registerActivityLifecycleCallback(Application.ActivityLifecycleCallbacks callbacks) {
        getApplication().registerActivityLifecycleCallbacks(callbacks);
    }

    private void init() {
        initNative();
        initThirdParty();
    }

    private void initNative() {
        context = application.getApplicationContext();
        mCache = ACache.get(application);
        initCacheData();
        initPixels();
    }

    private void initThirdParty() {
        initFresco();
    }


    //获取屏幕宽高像素
    private void initPixels() {
        widthPixels = application.getResources().getDisplayMetrics().widthPixels;
        heightPixels = application.getResources().getDisplayMetrics().heightPixels;
    }

    public static boolean isLogin() {
        return !StringUtil.isEmpty(MyApplication.USER_ID);
    }


    public static Context getContext() {
        return context;
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Fresco.getImagePipeline().clearMemoryCaches();
    }

    /**
     * 初始化本地缓存策略
     */
    private void initCacheData() {
        String userId = mCache.getAsString(CacheKey.USER_ID);
        if (!StringUtil.isEmpty(userId)) {
            USER_ID = userId;
            MyApplication.isLoginOB.set(true);
        } else {
            USER_ID = "";
        }
    }


    /**
     * 图片框架Fresco 初始化配置
     */
    private void initFresco() {
        int MAX_MEM = 30 * ByteConstants.MB;
        ProgressiveJpegConfig pjpegConfig = new SimpleProgressiveJpegConfig();
        MemoryCacheParams params = new MemoryCacheParams(MAX_MEM, Integer.MAX_VALUE, MAX_MEM, Integer.MAX_VALUE, Integer.MAX_VALUE);
        Supplier<MemoryCacheParams> mSupplierMemoryCacheParams = new Supplier<MemoryCacheParams>() {
            @Override
            public MemoryCacheParams get() {
                return params;
            }
        };
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(application)
                .setDownsampleEnabled(true)
                .setProgressiveJpegConfig(pjpegConfig)
                .setBitmapsConfig(Bitmap.Config.RGB_565)
                .setBitmapMemoryCacheParamsSupplier(mSupplierMemoryCacheParams)
                .build();
        Fresco.initialize(application, config);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Fresco.shutDown();
        isInit = false;
    }
}
