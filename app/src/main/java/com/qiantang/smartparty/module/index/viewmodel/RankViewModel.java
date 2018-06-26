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
import com.qiantang.smartparty.modle.RxRankBranch;
import com.qiantang.smartparty.modle.RxRankBranchMap;
import com.qiantang.smartparty.modle.RxRankPersonal;
import com.qiantang.smartparty.module.index.adapter.RankAdapter;
import com.qiantang.smartparty.network.NetworkSubscriber;
import com.qiantang.smartparty.network.retrofit.ApiWrapper;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by zhaoyong bai on 2018/5/28.
 */
public class RankViewModel extends BaseObservable implements ViewModel {
    private BaseBindActivity activity;
    private BaseBindFragment fragment;
    private RankAdapter adapter;
    private ObservableField<RxRankBranchMap> rxBranckMap = new ObservableField<>();
    private ObservableField<String> date = new ObservableField<>();
    private int year;
    private int month;
    private int currentYear;
    private int currentMonth;

    public RankViewModel(BaseBindFragment fragment, RankAdapter adapter) {
        this.fragment = fragment;
        this.adapter = adapter;
        initDate();
    }

    public RankViewModel(BaseBindActivity activity) {
        this.activity = activity;
    }

    private void initDate() {
        Calendar calendar = Calendar.getInstance();
        currentYear = year = calendar.get(Calendar.YEAR);
        currentMonth = month = calendar.get(Calendar.MONTH) + 1;
        if (month < 10) {
            setDate(year + "/0" + month);
        } else {
            setDate(year + "/" + month);
        }
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
        }
        if (month < 10) {
            setDate(year + "/0" + month);
        } else {
            setDate(year + "/" + month);
        }
        getData();

    }

    private void preMonth() {
        month--;
        if (month < 0) {
            month = 12;
            year--;
        }
        if (month < 10) {
            setDate(year + "/0" + month);
        } else {
            setDate(year + "/" + month);
        }
        getData();
    }

    public void getData() {
        ApiWrapper.getInstance().getRankBranch(getDate())
                .compose(fragment.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new NetworkSubscriber<RxRankBranch>() {
                    @Override
                    public void onSuccess(RxRankBranch data) {
                        adapter.setNewData(data.getBranchList());
                        setRxBranckMap(data.getPeopleMap());
                    }
                });
    }

    @Bindable
    public RxRankBranchMap getRxBranckMap() {
        return rxBranckMap.get();
    }

    public void setRxBranckMap(RxRankBranchMap rxBranckMap) {
        this.rxBranckMap.set(rxBranckMap);
        notifyPropertyChanged(BR.rxBranckMap);
    }

    @Bindable
    public String getDate() {
        return date.get();
    }

    public void setDate(String date) {
        this.date.set(date);
        notifyPropertyChanged(BR.date);
    }

    @Override
    public void destroy() {

    }
}
