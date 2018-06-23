package com.qiantang.smartparty.module.scan.view;

import android.Manifest;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;

import com.flyco.dialog.widget.NormalDialog;
import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.databinding.ActivityQrcodeBinding;
import com.qiantang.smartparty.module.scan.viewmodel.QRCodeViewModel;
import com.qiantang.smartparty.utils.ActivityUtil;
import com.qiantang.smartparty.utils.ToastUtil;
import com.qiantang.smartparty.utils.permissions.EasyPermission;
import com.qiantang.smartparty.utils.permissions.PermissionCode;
import com.qiantang.smartparty.widget.dialog.DefaultDialog;
import com.qiantang.smartparty.widget.dialog.OnDialogExecuteListener;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zbar.ZBarView;

/**
 * Desc : 二维码扫描
 * version : v1.0
 */

public class QRCodeActivity extends BaseBindActivity implements QRCodeView.Delegate, EasyPermission.PermissionCallback {
    private ActivityQrcodeBinding binding;
    private ZBarView mQRCodeView;
    private QRCodeViewModel viewModel;
    private NormalDialog dialog;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void initBind() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_qrcode);
        mQRCodeView = binding.zScan;
        viewModel = new QRCodeViewModel(this);
    }

    @Override
    public void initView() {
        binding.toolbar.setTitle("扫码");
        binding.toolbar.setIsHide(true);
        mQRCodeView.setDelegate(this);
    }

    /**
     * 请求相机权限。
     */
    private void requestCAMERAPermission() {
        new RxPermissions(this)
                .request(Manifest.permission.CAMERA)
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        mQRCodeView.startCamera();
                        mQRCodeView.showScanRect();
                        mQRCodeView.startSpot();
                    } else {
                        ToastUtil.toast("需要访问的相机~");
                    }
                });
    }


    @Override
    protected void onStart() {
        super.onStart();
        requestCAMERAPermission();
//        mQRCodeView.startCamera();
//        mQRCodeView.showScanRect();
//        mQRCodeView.startSpot();
    }

    @Override
    protected void onStop() {
        mQRCodeView.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mQRCodeView.onDestroy();
        super.onDestroy();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        ActivityUtil.startSignActivity(this, result);
        mQRCodeView.stopCamera();
        onBackPressed();
//        vibrate();
//        mQRCodeView.startSpotDelay(3000);
    }

    private void createDialog() {
        dialog = new DefaultDialog(this, "为了安全，暂不支持第三方二维码扫描。", new OnDialogExecuteListener() {
            @Override
            public void execute() {

            }

            @Override
            public void cancel() {

            }
        });
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Log.e(TAG, "打开相机出错");
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }

    @Override
    protected void viewModelDestroy() {
        if (viewModel != null)
            viewModel.destroy();
    }

    @Override
    public void onPermissionGranted(int requestCode, List<String> perms) {
        if (requestCode == PermissionCode.CAMERA) {
            mQRCodeView.startCamera();
            mQRCodeView.showScanRect();
            mQRCodeView.startSpot();
        }
    }

    @Override
    public void onPermissionDenied(int requestCode, List<String> perms) {
        if (requestCode == PermissionCode.CAMERA) {
            ToastUtil.toast("需要访问的相机~");
        }
    }
}
