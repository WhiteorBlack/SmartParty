package com.qiantang.smartparty.module.index.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;
import android.view.View;

import com.qiantang.smartparty.BR;
import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.BaseBindFragment;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.modle.RxRankPersonal;
import com.qiantang.smartparty.modle.RxRankPersonalMap;
import com.qiantang.smartparty.module.index.adapter.RankAdapter;
import com.qiantang.smartparty.module.index.adapter.RankPersonalAdapter;
import com.qiantang.smartparty.network.NetworkSubscriber;
import com.qiantang.smartparty.network.retrofit.ApiWrapper;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by zhaoyong bai on 2018/5/28.
 */
public class RankPersonalViewModel extends BaseObservable implements ViewModel {
    private BaseBindActivity activity;
    private BaseBindFragment fragment;
    private RankPersonalAdapter adapter;
    private ObservableField<RxRankPersonalMap> personalMap = new ObservableField<>();
    private ObservableField<String> date = new ObservableField<>();
    private int year;
    private int month;
    private int currentYear;
    private int currentMonth;

    public RankPersonalViewModel(BaseBindFragment fragment, RankPersonalAdapter adapter) {
        this.fragment = fragment;
        this.adapter = adapter;
        initDate();
    }

    private void initDate() {
        Calendar calendar = Calendar.getInstance();
        currentYear = year = calendar.get(Calendar.YEAR);
        currentMonth = month = calendar.get(Calendar.MONTH) + 1;
        setDate(year + "" + month);
        getData();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_pre:
                preMonth();
                break;
            case R.id.iv_next:
                nextMonth();
                break;
        }
    }

    private void nextMonth() {
        month++;
        if (month > 12) {
            month = 1;
            year++;
        }
        if (year > currentYear) {
            year = currentYear;
        } else if (year == currentYear) {
            if (month > currentMonth) {
                month = currentMonth;
            }
        }else {
            setDate(year+""+month);
            getData();
        }
    }

    private void preMonth() {
        month--;
        if (month<0){
            month=12;
            year--;
        }
        setDate(year+""+month);
        getData();
    }

    public void getData() {
        ApiWrapper.getInstance().getRankPersonal(getDate())
                .compose(fragment.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new NetworkSubscriber<RxRankPersonal>() {
                    @Override
                    public void onSuccess(RxRankPersonal data) {
                        adapter.setNewData(data.getPeopleList());
                        setPersonalMap(data.getMyPeopleMap());
                    }
                });
    }

    @Bindable
    public String getDate() {
        return date.get();
    }

    public void setDate(String date) {
        this.date.set(date);
        notifyPropertyChanged(BR.date);
    }

    @Bindable
    public RxRankPersonalMap getPersonalMap() {
        return personalMap.get();
    }

    public void setPersonalMap(RxRankPersonalMap personalMap) {
        this.personalMap.set(personalMap);
        notifyPropertyChanged(BR.personalMap);
    }

    @Override
    public void destroy() {

    }
}
