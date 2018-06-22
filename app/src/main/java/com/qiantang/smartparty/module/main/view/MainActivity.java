package com.qiantang.smartparty.module.main.view;

import android.Manifest;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.view.WindowManager;

import com.orhanobut.logger.Logger;
import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.MyApplication;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.config.Config;
import com.qiantang.smartparty.config.updata.NotificationDownloadCreator;
import com.qiantang.smartparty.config.updata.NotificationInstallCreator;
import com.qiantang.smartparty.databinding.ActivityMainBinding;
import com.qiantang.smartparty.module.main.adapter.ViewPagerAdapter;
import com.qiantang.smartparty.module.main.viewmodel.MainViewModel;
import com.qiantang.smartparty.utils.ActivityUtil;
import com.qiantang.smartparty.utils.WebUtil;
import com.qiantang.smartparty.utils.permissions.EasyPermission;
import com.shizhefei.view.indicator.IndicatorViewPager;

import org.lzh.framework.updatepluginlib.UpdateBuilder;
import org.lzh.framework.updatepluginlib.model.Update;
import org.lzh.framework.updatepluginlib.strategy.UpdateStrategy;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by zhaoyong bai on 2018/5/19.
 */
public class MainActivity extends BaseBindActivity implements IndicatorViewPager.OnIndicatorPageChangeListener, EasyPermission.PermissionCallback {
    public static Handler handler = new Handler();
    private ActivityMainBinding binding;
    private MainViewModel viewModel;
    private IndicatorViewPager indicatorViewPager;
    private String[] names = {"首页", "学习感悟", "党建助手", "我的"};
    private int[] icons = {R.drawable.index_selector, R.drawable.study_selector, R.drawable.assiant_selector, R.drawable.mine_selector};

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void initBind() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        viewModel = new MainViewModel(this);
        binding.setViewModel(viewModel);
    }

    @Override
    public void initView() {
        indicatorViewPager = new IndicatorViewPager(binding.indicator, binding.viewpager);
        binding.viewpager.setCanScroll(false);
        binding.viewpager.setOffscreenPageLimit(icons.length);
        indicatorViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), icons, names));
        indicatorViewPager.setOnIndicatorPageChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (MyApplication.isLogin()){
            viewModel.getUserInfo();
        }
    }

    @Override
    protected void viewModelDestroy() {
        WebUtil.removeCookie();
        viewModel.destroy();
    }

    @Override
    public void onIndicatorPageChange(int preItem, int currentItem) {
    }

    @Override
    public void onPermissionGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionDenied(int requestCode, List<String> perms) {

    }
}
