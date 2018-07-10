package com.qiantang.smartparty.module.search.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.config.Event;
import com.qiantang.smartparty.databinding.ActivitySearchBinding;
import com.qiantang.smartparty.module.search.fragment.FcNoticeFragment;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by zhaoyong bai on 2018/6/8.
 */
public class SearchActivity extends BaseBindActivity {
    private ActivitySearchBinding binding;
    private int type;
    private Fragment fragment;
    private FragmentTransaction transaction;

    @Override
    protected void initBind() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
    }

    @Override
    public void initView() {
        transaction = getSupportFragmentManager().beginTransaction();
        type = getIntent().getIntExtra("type", 0);
        if (type > 0) {
            switch (type) {
                case Event.SEARCH_CHARACTER:
                case Event.SEARCH_MEETING:
                case Event.SEARCH_MIEN:
                case Event.SEARCH_NEWS:
                case Event.SEARCH_STUDY_STATE:
                    fragment = new FcNoticeFragment();
                    break;
            }
            Bundle bundle = new Bundle();
            bundle.putInt("type", type);
            fragment.setArguments(bundle);
            transaction.add(R.id.fl_content, fragment);
            transaction.commit();
        }
        binding.toolbar.et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    String keyword = "";
                    keyword = binding.toolbar.et.getText().toString();
                    EventBus.getDefault().post(keyword);
                    closeInput();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_right:
                String keyword = "";
                keyword = binding.toolbar.et.getText().toString();
                EventBus.getDefault().post(keyword);
                closeInput();
                break;
            case R.id.iv_clear:
                binding.toolbar.et.setText("");
                break;
        }
    }

    @Override
    protected void viewModelDestroy() {

    }
}
