package com.qiantang.smartparty.module.main.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qiantang.smartparty.MyApplication;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.module.assistant.view.AssisantFragment;
import com.qiantang.smartparty.module.index.view.IndexFragment;
import com.qiantang.smartparty.module.mine.view.MineFragment;
import com.qiantang.smartparty.module.study.view.StudyFragment;
import com.qiantang.smartparty.utils.AppUtil;
import com.qiantang.smartparty.utils.AutoUtils;
import com.shizhefei.view.indicator.IndicatorViewPager;

/**
 * create by zhaoyong bai on 2017/09/06
 */

public class ViewPagerAdapter extends IndicatorViewPager.IndicatorFragmentPagerAdapter {
    private LayoutInflater inflater;
    private int[] icons;
    private String[] names;
    private SparseArray<Fragment> navigateMap = new SparseArray<>();

    public ViewPagerAdapter(FragmentManager fragmentManager, int[] icons, String[] names) {
        super(fragmentManager);
        inflater = LayoutInflater.from(MyApplication.getContext());
        this.icons = icons;
        this.names = names;
    }

    @Override
    public int getCount() {
        return icons.length;
    }


    @Override
    public View getViewForTab(int position, View convertView, ViewGroup container) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.tab_main, container, false);
        }
        ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_tab);
//        imageView.setImageDrawable(AppUtil.getDrawable(icons[position]));
        imageView.setImageResource(icons[position]);
        TextView textView = convertView.findViewById(R.id.tv_tab);
        textView.setText(names[position]);
        AutoUtils.auto(convertView);
        return convertView;
    }

    @Override
    public Fragment getFragmentForPage(int position) {
        Fragment fragment = navigateMap.get(position);
        if (fragment != null) {
            return fragment;
        }
        fragment = new Fragment();
        switch (position) {
            case 0://首页
                fragment=new IndexFragment();
                break;
            case 1://学习感悟
                fragment=new StudyFragment();
                break;
            case 2://党建助手
                fragment=new AssisantFragment();
                break;
            case 3://我的
                fragment=new MineFragment();
                break;

        }
        navigateMap.put(position, fragment);
        return fragment;
    }
}