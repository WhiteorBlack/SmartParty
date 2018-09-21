package com.qiantang.smartparty.module.mine.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;

import com.orhanobut.logger.Logger;
import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.MyApplication;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.config.CacheKey;
import com.qiantang.smartparty.databinding.ActivitySettingBinding;
import com.qiantang.smartparty.module.mine.viewmodel.SettingViewModel;
import com.qiantang.smartparty.network.URLs;
import com.qiantang.smartparty.utils.ActivityUtil;
import com.qiantang.smartparty.utils.WebUtil;
import com.umeng.message.IUmengCallback;
import com.umeng.message.PushAgent;

/**
 * Created by zhaoyong bai on 2018/5/22.
 */
public class SettingActivity extends BaseBindActivity {
    private SettingViewModel viewModel;
    private ActivitySettingBinding binding;

    @Override
    protected void initBind() {
        viewModel = new SettingViewModel(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting);
        binding.setViewModel(viewModel);
    }

    @Override
    public void initView() {
        binding.toolbar.setIsHide(true);
        binding.toolbar.setTitle("设置");
        binding.sch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    PushAgent.getInstance(SettingActivity.this).enable(new IUmengCallback() {
                        @Override
                        public void onSuccess() {
                            Logger.e("open-onSuccess");
                        }

                        @Override
                        public void onFailure(String s, String s1) {

                        }
                    });
                } else {
                    PushAgent.getInstance(SettingActivity.this).disable(new IUmengCallback() {
                        @Override
                        public void onSuccess() {
                            Logger.e("close-onSuccess");
                        }

                        @Override
                        public void onFailure(String s, String s1) {

                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.getInstall();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.ll_modify_phone:
                ActivityUtil.startModifyPhoneActivity(this);
                break;
            case R.id.btn_confirm:
                viewModel.logout();
                break;
            case R.id.ll_service:
                callPhone(viewModel.servicePhone.get());
                break;
            case R.id.ll_pro:
                WebUtil.jumpWeb(this, URLs.USER_PROTOCOL + 4, "用户协议");
                break;
            case R.id.ll_wechat:
                if (!TextUtils.isEmpty(viewModel.getRxSetting().getWx())) {
                    return;
                }
                viewModel.authWechat();
                break;
            case R.id.ll_qq:
                if (!TextUtils.isEmpty(viewModel.getRxSetting().getQq())) {
                    return;
                }
                viewModel.authQQ();
                break;
            case R.id.ll_modify_pwd:
                ActivityUtil.startModifyPwdActivity(this);
                break;
        }

    }

    private void callPhone(String phone) {
        Intent intent1 = new Intent(Intent.ACTION_DIAL);
        intent1.setData(Uri.parse("tel:" + phone));
        startActivity(intent1);
    }

    @Override
    protected void viewModelDestroy() {
        viewModel.destroy();
    }
}
