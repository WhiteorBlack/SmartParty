package com.qiantang.smartparty.module.assistant.view;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.adapter.PublishImgAdapter;
import com.qiantang.smartparty.databinding.ActivityApplyPartyBinding;
import com.qiantang.smartparty.module.assistant.viewmodel.ApplyPartyViewModel;
import com.qiantang.smartparty.utils.ToastUtil;
import com.qiantang.smartparty.utils.permissions.EasyPermission;
import com.qiantang.smartparty.utils.permissions.PermissionCode;

import java.util.List;

/**
 * Created by zhaoyong bai on 2018/5/30.
 */
public class ApplyPartyActivity extends BaseBindActivity implements EasyPermission.PermissionCallback{
    private PublishImgAdapter adapter;
    private ApplyPartyViewModel viewModel;
    private ActivityApplyPartyBinding binding;

    @Override
    protected void initBind() {
        adapter = new PublishImgAdapter(R.layout.item_publish);
        viewModel = new ApplyPartyViewModel(this, adapter);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_apply_party);
        binding.setViewModel(viewModel);
    }

    @Override
    public void initView() {
        binding.toolbar.setTitle("入党申请");
        binding.toolbar.setIsHide(true);
        initRV();
    }

    private void initRV() {
        binding.rv.setLayoutManager(new GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false));
        binding.rv.setAdapter(adapter);
        binding.rv.addOnItemTouchListener(viewModel.itemClickListener());
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.btn_confirm:

                break;
        }
    }

    @Override
    protected void viewModelDestroy() {
        viewModel.destroy();
    }

    @Override
    public void onPermissionGranted(int requestCode, List<String> perms) {
        if (requestCode == PermissionCode.RG_CAMERA_PERM) {
            if (viewModel != null) {
                viewModel.openCamera();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        EasyPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    public void onPermissionDenied(int requestCode, List<String> perms) {
        switch (requestCode) {
            case PermissionCode.RG_CAMERA_PERM:
                ToastUtil.toast(getString(R.string.rationale_camera));
                break;
        }
    }
}
