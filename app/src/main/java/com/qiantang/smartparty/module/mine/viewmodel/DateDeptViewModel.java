package com.qiantang.smartparty.module.mine.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.graphics.Color;
import android.view.View;

import com.bigkoo.pickerview.OptionsPickerView;
import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.MyApplication;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.config.CacheKey;
import com.qiantang.smartparty.modle.HttpResult;
import com.qiantang.smartparty.modle.RxDateDay;
import com.qiantang.smartparty.modle.RxDateMonth;
import com.qiantang.smartparty.modle.RxDateYear;
import com.qiantang.smartparty.modle.RxDeptName;
import com.qiantang.smartparty.modle.RxMyUserInfo;
import com.qiantang.smartparty.network.NetworkSubscriber;
import com.qiantang.smartparty.network.retrofit.ApiWrapper;
import com.qiantang.smartparty.network.retrofit.RetrofitUtil;
import com.qiantang.smartparty.utils.StringUtil;
import com.qiantang.smartparty.utils.ToastUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DateDeptViewModel extends BaseObservable implements ViewModel, OptionsPickerView.OnOptionsSelectListener {

    private final BaseBindActivity activity;
    public ObservableField<String> info = new ObservableField<>();
    public ObservableBoolean checkSelection = new ObservableBoolean();
    public ObservableField<String> infoTitle = new ObservableField<>();
    private OptionsPickerView pickerView;
    private List<RxDateYear> dateList = new ArrayList<>();
    private List<RxDeptName> deptNames = new ArrayList<>();
    private String deptId = "";
    private int type = 0;

    public DateDeptViewModel(BaseBindActivity activity) {
        this.activity = activity;
        type = activity.getIntent().getIntExtra("type", 0);
        initCache();
        initPartyView();
    }

    private void initPartyView() {
        pickerView = new OptionsPickerView.Builder(activity, this)
                .setTitleText("身份")
                .setDividerColor(Color.BLACK)
                .setContentTextSize(16)
                .build();
    }

    private void initCache() {
        String data = activity.getIntent().getStringExtra("info");
        if (!StringUtil.isEmpty(data)) {
            info.set(data);
        }
    }

    private int year;
    private int month;
    private int day;

    public void initDateData() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        new Thread(() -> {
            for (int i = 0; i < 80; i++) {
                year -= i;
                RxDateYear rxDateYear = new RxDateYear();
                rxDateYear.setYear(year);
                List<RxDateMonth> dateMonths = new ArrayList<>();
                int month = 0;
                if (i == 0) {
                    month = this.month;
                } else {
                    month = 12;
                }
                for (int j = 0; j < month; j++) {
                    RxDateMonth rxDateMonth = new RxDateMonth();
                    rxDateMonth.setMonth(j + 1);
                    List<RxDateDay> dateDays = new ArrayList<>();
                    int day = 0;
                    if (i == 0 && j == this.month - 1) {
                        day = this.day;
                    } else {
                        if (j == 1) {
                            //二月
                            if ((year % 100 == 0 && year % 400 == 0) || (year % 100 != 0 && year % 4 == 0)) {  //闰年
                                day = 29;
                            } else {
                                day = 28;
                            }
                        } else if (j == 0 || j == 2 || j == 4 || j == 6 || j == 7 || j == 9 || j == 11) { //1,3,5,7,8,10,12 月
                            day = 31;
                        } else {
                            day = 30;
                        }
                    }
                    for (int k = 0; k < day; k++) {
                        RxDateDay rxDateDay = new RxDateDay();
                        rxDateDay.setDay(k + 1);
                        dateDays.add(rxDateDay);
                    }
                    rxDateMonth.setDateDays(dateDays);
                    dateMonths.add(rxDateMonth);
                }
                rxDateYear.setMonth(dateMonths);
                dateList.add(rxDateYear);
            }
            List<List<RxDateMonth>> monthList = new ArrayList<>();
            List<List<List<RxDateDay>>> dayList = new ArrayList<>();
            for (int i = 0; i < dateList.size(); i++) {
                monthList.add(dateList.get(i).getMonth());
                List<List<RxDateDay>> days = new ArrayList<>();
                for (int j = 0; j < dateList.get(i).getMonth().size(); j++) {
                    days.add(dateList.get(i).getMonth().get(j).getDateDays());
                }
                dayList.add(days);
            }
            if (pickerView != null) {
                pickerView.setPicker(dateList, monthList, dayList);
            }
        }).start();

    }


    public void getOrgData() {
        ApiWrapper.getInstance().deptName()
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<List<RxDeptName>>() {
                    @Override
                    public void onSuccess(List<RxDeptName> data) {
                        deptNames.clear();
                        deptNames.addAll(data);

                        if (pickerView != null) {
                            pickerView.setPicker(deptNames);
                        }
                    }
                });
    }


    /**
     * 所属组织
     */
    public void modifyName() {
        String realName = info.get();
        ApiWrapper.getInstance()
                .modifyArchives("", "", "", deptId)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<HttpResult>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onSuccess(HttpResult bean) {
                        ToastUtil.toast("修改成功");
                        MyApplication.mCache.getAsJSONBean(CacheKey.USER_INFO, RxMyUserInfo.class, rxMyUserInfo -> {
                            rxMyUserInfo.setDeptId(deptId);
                            rxMyUserInfo.getDept().setDeptId(deptId);
                            rxMyUserInfo.getDept().setDeptName(realName);
                            MyApplication.mCache.put(CacheKey.USER_INFO, rxMyUserInfo);
                            activity.finish();
                        });

                    }
                });
    }

    public void showPop() {
        if (pickerView != null) {
            pickerView.show();
        }
    }

    /**
     * 修改日期
     */
    public void modifyNick() {
        String nickName = info.get();

        ApiWrapper.getInstance().modifyArchives(nickName, "", "", "")
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<HttpResult>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onSuccess(HttpResult bean) {
                        ToastUtil.toast("修改成功");
                        MyApplication.mCache.getAsJSONBean(CacheKey.USER_INFO, RxMyUserInfo.class, rxMyUserInfo -> {
                            rxMyUserInfo.setJoinpatryTime(nickName);
                            MyApplication.mCache.put(CacheKey.USER_INFO, rxMyUserInfo);
                            activity.finish();
                        });
                    }
                });
    }

    @Override
    public void destroy() {
    }

    @Override
    public void onOptionsSelect(int options1, int options2, int options3, View v) {
        if (type == 0) {
            info.set(deptNames.get(options1).getDept_name());
            deptId = deptNames.get(options1).getDept_id();
        } else {
            info.set(dateList.get(options1).getYear() + "-" + dateList.get(options1).getMonth().get(options2).getMonth() + "-" + dateList.get(options1).getMonth().get(options2).getDateDays().get(options3).getDay());
        }
    }
}
