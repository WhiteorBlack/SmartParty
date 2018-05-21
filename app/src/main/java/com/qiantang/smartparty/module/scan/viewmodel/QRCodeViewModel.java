package com.qiantang.smartparty.module.scan.viewmodel;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.utils.LoadingWindow;
import com.qiantang.smartparty.utils.ToastUtil;
import com.qiantang.smartparty.utils.permissions.EasyPermission;
import com.qiantang.smartparty.utils.permissions.PermissionCode;


public class QRCodeViewModel implements ViewModel {

    public static final String RESULT_FLAG = "qrSingResult";
    public static final int RESULT_CODE_SING = 200;
    private final BaseBindActivity activity;
    private final LoadingWindow loadingWindow;
    private String TAG = "QRCodeViewModel";

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public QRCodeViewModel(BaseBindActivity activity) {
        this.activity = activity;
        loadingWindow = new LoadingWindow(activity);
//        checkPermission();
    }

    /**
     * 权限检测
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void checkPermission() {
        EasyPermission.with(activity)
                .rationale("需要访问的相机权限")
                .addRequestCode(PermissionCode.CAMERA)
                .permissions(Manifest.permission.CAMERA).request();
    }

    @Override
    public void destroy() {
        loadingWindow.dismiss();
    }
}
